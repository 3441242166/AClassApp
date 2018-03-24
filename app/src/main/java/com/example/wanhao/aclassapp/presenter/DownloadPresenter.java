package com.example.wanhao.aclassapp.presenter;

import android.content.Context;

import com.example.wanhao.aclassapp.view.IDownloadView;

/**
 * Created by wanhao on 2018/3/24.
 */

public class DownloadPresenter {
    private static final String TAG = "DownloadPresenter";

    private Context context;
    private IDownloadView downloadView;

    public DownloadPresenter(Context context,IDownloadView view){
        this.context = context;
        this.downloadView = view;
    }

    public void downloadDocument(){

    }

    public void saveDocument(){

    }

}
