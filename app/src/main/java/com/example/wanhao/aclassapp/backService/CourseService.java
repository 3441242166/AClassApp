package com.example.wanhao.aclassapp.backService;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.example.wanhao.aclassapp.bean.ChatBean;
import com.example.wanhao.aclassapp.bean.ChatResult;
import com.example.wanhao.aclassapp.config.ApiConstant;
import com.example.wanhao.aclassapp.config.Constant;
import com.example.wanhao.aclassapp.db.ChatDB;
import com.example.wanhao.aclassapp.util.SaveDataUtil;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.Arrays;
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

import static com.example.wanhao.aclassapp.config.ApiConstant.CHAT_URL;

public class CourseService extends Service {
    private static final String TAG = "CourseService";

    private Map<String,ClientBean> clientMap = new HashMap<>();
    private Realm realm;

    @Override
    public IBinder onBind(Intent intent) {
       return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        EventBus.getDefault().register(this);
        realm = Realm.getDefaultInstance();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent == null){
            return super.onStartCommand(null, flags, startId);
        }

        int code = intent.getIntExtra(ApiConstant.IM_ACTION,-1);
        Log.i(TAG, "onStartCommand: code = "+ code);
        switch (code){
            case ApiConstant.MESSAGE_CHAT:
                sendMessage(intent);
                break;
            case ApiConstant.MESSAGE_HOMEWORK:

                break;
            case ApiConstant.MESSAGE_NOTICE:

                break;
            case ApiConstant.MESSAGE_SIGN:

                break;
            case ApiConstant.MESSAGE_CONNECT:
                connect(intent);
                break;
            case ApiConstant.MESSAGE_DISCONNECT:

                break;
        }

        return super.onStartCommand(intent, flags, startId);
    }

    boolean isConnect(String id){

        if(!clientMap.containsKey(id)){
            return false;
        }

        ClientBean bean = clientMap.get(id);
        if(bean.client == null){
            return false;
        }
        if(bean.sendHeader == null){
            return false;
        }
        if(bean.header == null){
            return false;
        }

        return true;
    }

    void connect(Intent intent){
        Log.i(TAG, "connect");
        String id = intent.getStringExtra(ApiConstant.COURSE_ID);

        //如果MAP包含此ID
        if(clientMap.containsKey(id)){
            ClientBean bean = clientMap.get(id);
            bean.client.connect();
        }else {
            ClientBean bean = new ClientBean(id);
            startConnect(bean);
            clientMap.put(id,bean);
        }
    }

    @SuppressLint("CheckResult")
    void startConnect(ClientBean client){
        Log.i(TAG, "startConnect");
        String groupUrl = "/app/group/"+ client.groupID;
        String responseUrl = "/g/"+ client.groupID;

        Log.i(TAG, " init: groupUrl = " + groupUrl + "\n init: responseUrl = " + responseUrl);

        client.sendHeader = new StompHeader("destination", groupUrl);
        client.header = new StompHeader("Authorization", SaveDataUtil.getValueFromSharedPreferences(this, ApiConstant.USER_TOKEN));

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
                        Log.i(TAG, "Connect: stomp connection closed!");
                        break;
                }
            });

        client.client.topic(responseUrl)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(stompMessage -> {
                    String message = stompMessage.getPayload();
                    Log.i(TAG, "message = " + message);

                    ChatResult result = new Gson().fromJson(message, ChatResult.class);
                    ChatBean bean = result.getMessage();
                    String count = SaveDataUtil.getValueFromSharedPreferences(this,ApiConstant.USER_COUNT);
                    if (result.getStatus().equals(ApiConstant.RETURN_SUCCESS)) {
                        ChatDB data = new ChatDB(bean);

                        if(count.equals(data.getUser().getCount())){
                            data.setItemType(ChatDB.USER_ME);
                        }else {
                            data.setItemType(ChatDB.USER_OTHER);
                        }

                        saveMessageDB(data);
                        EventBus.getDefault().post(data);
                    } else {
                        Log.i(TAG, "startConnect: error = "+result.getStatus());
                    }
                });

    }

    private void saveMessageDB(ChatDB bean){
        Log.i(TAG, "saveMessageDB");
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(bean);

        realm.commitTransaction();
    }

    void sendMessage(Intent intent){
        Log.i(TAG, "sendMessage");
        String courseID = intent.getStringExtra(ApiConstant.COURSE_ID);
        String message = intent.getStringExtra(ApiConstant.COURSE_MESSAGE);

        if(clientMap.get(courseID) ==null){
            ClientBean bean = new ClientBean(courseID);
            startConnect(bean);
            clientMap.put(courseID,bean);
            StompMessage stompMessage = new StompMessage(StompCommand.SEND, Arrays.asList(bean.sendHeader, bean.header) , message);
            bean.client.send(stompMessage).subscribe();
        }else {
            ClientBean bean = clientMap.get(courseID);
            StompMessage stompMessage = new StompMessage(StompCommand.SEND, Arrays.asList(bean.sendHeader, bean.header) , message);
            bean.client.send(stompMessage).subscribe();
        }

    }

    @Subscribe(priority = 777)
    public void handleIMMessage(ChatBean chatBean) {
        Log.i(TAG, "handleIMMessage");
        Log.i(Constant.TAG_EVENTBUS, "CourseService: content = "+chatBean.getContent());
        EventBus.getDefault().cancelEventDelivery(chatBean);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
//    private void notificationMessage(ChatDB bean){
//        Log.i(TAG, "notificationMessage: message = "+bean.getContent());
//        Realm realm = Realm.getDefaultInstance();
//        realm.beginTransaction();
//        Course course = realm.where(Course.class)
//                .equalTo("id",bean.getCourseID()+"")
//                .findFirst();
//        course.setUnReadNum(course.getUnReadNum()+1);
//        realm.commitTransaction();
//        //==========================================================================================
//        NotificationManager notificationManager= (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        createNotificationChanne(CHANNE_IM_ID, CHANNE_IM_NAME, NotificationManager.IMPORTANCE_HIGH,notificationManager);
//        Notification.Builder builder = new Notification.Builder(this,CHANNE_IM_ID);
//
//        Intent documentIntent = new Intent(this, CourseActivity.class);
//        documentIntent.putExtra(ApiConstant.COURSE_ID,bean.getCourseID());
//        PendingIntent mainPendingIntent = PendingIntent.getActivity(this, 0, documentIntent, PendingIntent.FLAG_CANCEL_CURRENT);
//
//        builder.setSmallIcon(R.mipmap.ic_launcher)
//                .setContentTitle(bean.getUser().getUserName() + ": " +bean.getContent())
//                .setAutoCancel(true)
//                .setContentIntent(mainPendingIntent);
//
//        notificationManager.notify(Integer.valueOf(bean.getChatID()),builder.build());
//    }

    @Override

    public void onDestroy() {
        Log.i(TAG+"aaaaa", "onDestroy");
        super.onDestroy();
        EventBus.getDefault().unregister(this);
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
}

