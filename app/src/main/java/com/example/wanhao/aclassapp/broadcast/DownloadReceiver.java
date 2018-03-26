package com.example.wanhao.aclassapp.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.wanhao.aclassapp.config.ApiConstant;

public class DownloadReceiver extends BroadcastReceiver {
    private static final String TAG = "DownloadReceiver";

    private onDownloadStateChangeLinser stateChangeLinser;

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        if(stateChangeLinser!=null) {
            stateChangeLinser.onDownloadStateChange(intent.getStringExtra(ApiConstant.DOWNLOAD_STATE),intent.getIntExtra(ApiConstant.Document_ID,-1));
        }

    }

    public interface onDownloadStateChangeLinser{
        void onDownloadStateChange(String state, int ID);
    }

    public void setDownloadStateChangeLinser(onDownloadStateChangeLinser linser){
        this.stateChangeLinser = linser;
    }

}
