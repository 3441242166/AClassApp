package com.example.wanhao.aclassapp.presenter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.example.wanhao.aclassapp.SQLite.ChatDao;
import com.example.wanhao.aclassapp.bean.sqlbean.ChatBean;
import com.example.wanhao.aclassapp.bean.sqlbean.ChatResult;
import com.example.wanhao.aclassapp.config.ApiConstant;
import com.example.wanhao.aclassapp.util.SaveDataUtil;
import com.example.wanhao.aclassapp.view.ChatView;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.w3c.dom.ls.LSException;

import java.util.Arrays;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import okhttp3.WebSocket;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompHeader;
import ua.naiksoftware.stomp.client.StompClient;
import ua.naiksoftware.stomp.client.StompCommand;
import ua.naiksoftware.stomp.client.StompMessage;

/**
 * Created by wanhao on 2018/4/8.
 */

public class ChatPresenter {
    private static final String TAG = "ChatPresenter";

    private String groupUrl;
    private String responreUrl;

    private Context context;
    private ChatView chatView;

    private StompClient stompClient;
    private String courseID;
    private StompHeader authorizationHeader;

    private ChatDao dao;
    private MyThread thread;

    public ChatPresenter(Context context,ChatView chatView,String courseID){
        this.chatView = chatView;
        this.context = context;
        this.courseID = courseID;
        dao = new ChatDao(context);
        init();
    }

    private void init(){
        groupUrl = "/group/"+courseID;
        responreUrl = "/g/"+courseID;

        Log.i(TAG, "init: groupUrl = "+ groupUrl);
        Log.i(TAG, "init: responreUrl      "+ responreUrl);
        authorizationHeader = new StompHeader("Authorization", SaveDataUtil.getValueFromSharedPreferences(context,ApiConstant.USER_TOKEN));

        stompClient = Stomp.over(WebSocket.class, ApiConstant.CHAT_URL);
        stompClient.connect();

        stompClient.lifecycle().subscribe(lifecycleEvent -> {
            switch (lifecycleEvent.getType()) {
                case OPENED:
                    Log.i(TAG, "Connect: stomp connection opened!");
                    break;
                case ERROR:
                    Log.i(TAG, "Connect: Error occured!", lifecycleEvent.getException());
                    break;
                case CLOSED:
                    Log.i(TAG+"dd", "Connect: stomp connection closed!");
                    break;
            }
        });

        stompClient.topic(responreUrl)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(stompMessage -> {
            String message =stompMessage.getPayload();

            ChatResult result = new Gson().fromJson(message,ChatResult.class);

            if(!result.getStatus().equals(ApiConstant.RETURN_SUCCESS)){
                chatView.tokenError();
            }else {

                if (result.getMessage().getUser().getNickName().equals(SaveDataUtil.getValueFromSharedPreferences(context, ApiConstant.USER_NAME))) {
                    result.getMessage().setItemType(ChatBean.ME);
                } else {
                    result.getMessage().setItemType(ChatBean.OTHER);
                }
                Log.i(TAG, "init: thread = " + Thread.currentThread());
                chatView.newNewMessage(result.getMessage());
            }
        });
        thread = new MyThread();
        thread.start();
    }

    public void getHistoryMessage(){
        List<ChatBean> list = dao.alterChatBean(SaveDataUtil.getValueFromSharedPreferences(context,ApiConstant.COUNT),courseID,0,0);
        chatView.getHistoryMessage(list);
    }

    public void addChat(ChatBean chatBean){
        Log.i(TAG, "addChat: chatContent "+chatBean.getContent());
        dao.addchat(chatBean,SaveDataUtil.getValueFromSharedPreferences(context,ApiConstant.COUNT),courseID);
    }

    public void sendMessage(String message){
        if(TextUtils.isEmpty(message)){
            return;
        }
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("content", message);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        StompMessage stompMessage = new StompMessage(
                StompCommand.SEND,
                // 第一个 StompHeader 是必需的，第二个是我们手动添加的 Authorization 字段
                Arrays.asList(new StompHeader(StompHeader.DESTINATION, groupUrl), authorizationHeader),jsonObject.toString());

        stompClient.send(stompMessage).subscribe(new Subscriber<Void>() {
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

    public void deleteChat(int chatId){
        dao.deleteChat(String.valueOf(chatId),SaveDataUtil.getValueFromSharedPreferences(context,ApiConstant.COUNT),courseID);
        chatView.newNewMessage(null);
    }

    public void stopThread(){
        thread.state = false;
        stompClient.disconnect();
    }

    public class MyThread extends Thread {
        public boolean state = true;

        @Override
        public void run() {
            // TODO Auto-generated method stub
            while (state) {
                try {
                    stompClient.send("", "").subscribe();
                    Thread.sleep(20000);// 线程暂停20秒，单位毫秒
                    if(!state)
                        break;
                    //sendMessage("");
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

    }

}
