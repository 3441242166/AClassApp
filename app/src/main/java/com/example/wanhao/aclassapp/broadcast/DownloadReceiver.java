package com.example.wanhao.aclassapp.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.wanhao.aclassapp.config.ApiConstant;
import com.example.wanhao.aclassapp.service.DownDocumentService;

import java.io.Serializable;

import static com.example.wanhao.aclassapp.config.ApiConstant.DOWNED_BEAN;

public class DownloadReceiver extends BroadcastReceiver {
    private static final String TAG = "DownloadReceiver";

    private onDownloadStateChangeListener stateChangeListener;

    @Override
    public void onReceive(Context context, Intent intent) {
        if(stateChangeListener!=null && intent!= null) {
            stateChangeListener.onDownloadStateChange((DownDocumentService.DownLoadBean) intent.getSerializableExtra(DOWNED_BEAN));
        }
    }

    public interface onDownloadStateChangeListener{
        void onDownloadStateChange(DownDocumentService.DownLoadBean data);
    }

    public void setDownloadStateChangeListener(onDownloadStateChangeListener listener){
        this.stateChangeListener = listener;
    }

}
