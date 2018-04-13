package com.example.wanhao.aclassapp.presenter;

import android.content.Context;
import android.util.Log;

import com.example.wanhao.aclassapp.config.ApiConstant;
import com.example.wanhao.aclassapp.util.SaveDataUtil;
import com.example.wanhao.aclassapp.view.ChatView;

import org.json.JSONException;
import org.json.JSONObject;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.Arrays;

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
    StompHeader authorizationHeader;

    public ChatPresenter(Context context,ChatView chatView,String courseID){
        this.chatView = chatView;
        this.context = context;
        this.courseID = courseID;
        init();
    }

    private void init(){
        groupUrl = ApiConstant.CHAT_URL+"group/"+courseID;
        responreUrl = ApiConstant.CHAT_URL+"g/"+courseID;

        Log.i(TAG, "init: groupUrl = "+ groupUrl);
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
                    Log.i(TAG, "Connect: stomp connection closed!");
                    break;
            }
        });

        stompClient.topic(responreUrl).subscribe(stompMessage -> {
            Log.i(TAG, "init: subscribe ");
            JSONObject jsonObject = new JSONObject(stompMessage.getPayload());
            Log.i(TAG, "Receive: " + stompMessage.getPayload());
            Log.i(TAG, "response = "+jsonObject.getString("response"));

        });

        new Thread(new MyThread()).start();
    }

    public void sendMessage(String message){
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

    public class MyThread implements Runnable {
        @Override
        public void run() {
            // TODO Auto-generated method stub
            while (true) {
                try {
                    Thread.sleep(20000);// 线程暂停10秒，单位毫秒
                    //sendMessage("");
                    stompClient.send("", "").subscribe();
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

}
