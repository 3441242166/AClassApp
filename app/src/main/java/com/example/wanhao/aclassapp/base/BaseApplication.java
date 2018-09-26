package com.example.wanhao.aclassapp.base;

import android.app.Application;
import android.content.Context;

import com.liulishuo.filedownloader.FileDownloader;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by wanhao on 2018/2/27.
 */

public class BaseApplication extends Application {
    /**
     * 全局的上下文
     */
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        FileDownloader.init(this);
        Realm.init(this);
        Realm.setDefaultConfiguration(new RealmConfiguration.Builder().build());
    }

    /**
     * 获取context
     * @return
     */
    public static Context getContext(){
        return mContext;
    }

}