package com.example.wanhao.aclassapp.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.wanhao.aclassapp.bean.ChatBean;
import com.example.wanhao.aclassapp.config.ApiConstant;
import com.example.wanhao.aclassapp.util.SaveDataUtil;

import io.realm.Realm;

import static com.example.wanhao.aclassapp.config.ApiConstant.DOWNED_BEAN;

public class CourseReceiver extends BroadcastReceiver {
    private static final String TAG = "CourseReceiver";

    private onNewMessageListener listener;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "onReceive: ");

        if(listener !=null && intent!= null) {
            ChatBean bean = (ChatBean) intent.getSerializableExtra(ApiConstant.COURSE_BEAN);
            listener.onNewMessage(bean);
        }
        abortBroadcast();
    }


    public interface onNewMessageListener{
        void onNewMessage(ChatBean data);
    }

    public void setOnNewMessageListener(onNewMessageListener listener){
        this.listener = listener;
    }
}
