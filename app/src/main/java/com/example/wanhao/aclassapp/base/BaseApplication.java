package com.example.wanhao.aclassapp.base;

import android.app.Application;
import android.content.Context;
import android.util.DisplayMetrics;

import com.liulishuo.filedownloader.FileDownloader;

import io.realm.Realm;
import io.realm.RealmConfiguration;

import static com.example.wanhao.aclassapp.config.Constant.SCREEN_HEIGHT;
import static com.example.wanhao.aclassapp.config.Constant.SCREEN_WIDTH;
import static com.example.wanhao.aclassapp.util.SaveDataUtil.saveToSharedPreferences;

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
        FileDownloader.setup(this);
        Realm.init(this);
        Realm.setDefaultConfiguration(new RealmConfiguration.Builder().build());

        DisplayMetrics dm = getResources().getDisplayMetrics();
        int heigth = dm.heightPixels;
        int width = dm.widthPixels;
        saveToSharedPreferences(this,SCREEN_HEIGHT, String.valueOf(heigth));
        saveToSharedPreferences(this,SCREEN_WIDTH, String.valueOf(width));
    }

    /**
     * 获取context
     * @return
     */
    public static Context getContext(){
        return mContext;
    }

}