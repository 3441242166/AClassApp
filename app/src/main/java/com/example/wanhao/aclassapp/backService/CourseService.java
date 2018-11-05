package com.example.wanhao.aclassapp.backService;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.Log;
import android.view.TextureView;

import com.example.wanhao.aclassapp.R;
import com.example.wanhao.aclassapp.activity.BrowseDocumentActivity;
import com.example.wanhao.aclassapp.activity.CourseActivity;
import com.example.wanhao.aclassapp.bean.ChatBean;
import com.example.wanhao.aclassapp.bean.ChatResult;
import com.example.wanhao.aclassapp.bean.Course;
import com.example.wanhao.aclassapp.broadcast.BackReceiver;
import com.example.wanhao.aclassapp.config.ApiConstant;
import com.example.wanhao.aclassapp.util.SaveDataUtil;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.realm.Realm;
import okhttp3.WebSocket;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompHeader;
import ua.naiksoftware.stomp.client.StompClient;
import ua.naiksoftware.stomp.client.StompCommand;
import ua.naiksoftware.stomp.client.StompMessage;

import static com.chad.library.adapter.base.listener.SimpleClickListener.TAG;
import static com.example.wanhao.aclassapp.config.ApiConstant.CHANNE_DOWNED_ID;
import static com.example.wanhao.aclassapp.config.ApiConstant.CHANNE_DOWNED_NAME;
import static com.example.wanhao.aclassapp.config.ApiConstant.CHANNE_IM_ID;
import static com.example.wanhao.aclassapp.config.ApiConstant.CHANNE_IM_NAME;
import static com.example.wanhao.aclassapp.config.ApiConstant.CHAT_URL;
import static com.example.wanhao.aclassapp.util.NotificationUtils.createNotificationChanne;

public class CourseService extends Service {
    private static final String TAG = "CourseService";

    private static final String MESSAGE_CHAT = "chat";
    private static final String MESSAGE_HOMEWORK = "homework";
    private static final String MESSAGE_NOTICE = "notice";
    private static final String MESSAGE_SIGN = "sign";
    private static final String MESSAGE_CONNECT = "connect";

    Map<String,ClientBean> clientMap = new HashMap<>();
    private Intent broadcastIntent;
    private Realm realm;
    private BackReceiver backReceiver;

    @Override
    public IBinder onBind(Intent intent) {
       return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        broadcastIntent = new Intent(ApiConstant.COURSE_ACTION);

        realm = Realm.getDefaultInstance();
        backReceiver = new BackReceiver();
        IntentFilter filter = new IntentFilter(ApiConstant.COURSE_ACTION);
        filter.setPriority(777);
        registerReceiver(backReceiver,filter);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            backReceiver.setOnNewMessageListener(this::notificationMessage);
        }else {

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void notificationMessage(ChatBean bean){
        Log.i(TAG, "notificationMessage: message = "+bean.getContent());
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        Course course = realm.where(Course.class)
                .equalTo("id",bean.getCourseID()+"")
                .findFirst();
        course.setUnReadNum(course.getUnReadNum()+1);
        realm.commitTransaction();
        //==========================================================================================
        NotificationManager notificationManager= (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        createNotificationChanne(CHANNE_IM_ID, CHANNE_IM_NAME, NotificationManager.IMPORTANCE_HIGH,notificationManager);
        Notification.Builder builder = new Notification.Builder(this,CHANNE_IM_ID);

        Intent documentIntent = new Intent(this, CourseActivity.class);
        documentIntent.putExtra(ApiConstant.COURSE_ID,bean.getCourseID());
        PendingIntent mainPendingIntent = PendingIntent.getActivity(this, 0, documentIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        builder.setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(bean.getUser().getNickName() + ": " +bean.getContent())
                .setAutoCancel(true)
                .setContentIntent(mainPendingIntent);

        notificationManager.notify(bean.getId(),builder.build());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent==null){
            return super.onStartCommand(intent, flags, startId);
        }
        String code = intent.getStringExtra("");
        switch (code){
            case MESSAGE_CHAT:

                break;

        }

        String id = intent.getStringExtra(ApiConstant.COURSE_ID);
        String message  = intent.getStringExtra(ApiConstant.COURSE_MESSAGE);

        if(!clientMap.containsKey(id)){

            ClientBean bean = new ClientBean(id);
            startConnect(bean);
            clientMap.put(id,bean);

            if(!TextUtils.isEmpty(message)){
                sendMessage(message,id);
            }
        }else {
            sendMessage(message,id);
        }
        return super.onStartCommand(intent, flags, startId);
    }


    @SuppressLint("CheckResult")
    void startConnect(ClientBean client){
        String groupUrl = "/app/group/"+ client.groupID;
        String responseUrl = "/g/"+ client.groupID;

        Log.i(TAG, "init: groupUrl = " + groupUrl);
        Log.i(TAG, "init: responseUrl = " + responseUrl);

        client.sendHeader = new StompHeader("destination", groupUrl);
        client.header = new StompHeader("Authorization", SaveDataUtil.getValueFromSharedPreferences(this, ApiConstant.USER_TOKEN));

        if(!client.client.isConnected()) {
            client.client = Stomp.over(WebSocket.class, CHAT_URL);

            client.client.connect();

            client.client.lifecycle().subscribe(lifecycleEvent -> {
                switch (lifecycleEvent.getType()) {
                    case OPENED:
                        Log.i(TAG, "Connect: stomp connection opened!");
                        break;
                    case ERROR:
                        Log.i(TAG, "Connect: Error occured!" + lifecycleEvent.getException());
                        break;
                    case CLOSED:
                        Log.i(TAG + "dd", "Connect: stomp connection closed!");
                        break;
                }
            });

            client.client.topic(responseUrl)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(stompMessage -> {
                        String message = stompMessage.getPayload();
                        Log.i(TAG, "init: message = " + message);
                        ChatResult result = new Gson().fromJson(message, ChatResult.class);
                        ChatBean bean = result.getMessage();
                        if (!result.getStatus().equals(ApiConstant.RETURN_SUCCESS)) {

                        } else {
                            if (bean.getUser().getCount().equals(SaveDataUtil.getValueFromSharedPreferences(this, ApiConstant.USER_COUNT))) {
                                bean.setItemType(ChatBean.ME);
                            } else {
                                bean.setItemType(ChatBean.OTHER);
                            }
                            addMessage(bean);
                            broadcastIntent.putExtra(ApiConstant.COURSE_BEAN, bean);
                            sendOrderedBroadcast(broadcastIntent, null);
                        }
                    });
        }else {

        }
    }

    void sendMessage(String message,String courseID){
        ClientBean bean = clientMap.get(courseID);

        StompMessage stompMessage = new StompMessage(StompCommand.SEND, Arrays.asList(bean.sendHeader, bean.header) , message);

        bean.client.send(stompMessage).subscribe(new Subscriber<Void>() {
            @Override
            public void onSubscribe(Subscription s) {
                Log.i(TAG, "订阅成功");
            }

            @Override
            public void onNext(Void aVoid) {

            }

            @Override
            public void onError(Throwable t) {
                t.printStackTrace();
                Log.e(TAG, "发生错误：", t);
            }

            @Override
            public void onComplete() {
                Log.i(TAG, "发送消息成功！");
            }
        });
    }


    private void addMessage(ChatBean bean){
        realm.beginTransaction();
        realm.copyToRealm(bean);
        realm.commitTransaction();
    }

    static class ClientBean{
        StompClient client;
        StompHeader header;
        StompHeader sendHeader;
        String groupID;

        ClientBean(String groupID){
            this.groupID = groupID;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(backReceiver);
    }

}

