package com.example.wanhao.aclassapp.base;

import android.app.Application;
import android.content.Context;

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
    }

    /**
     * 获取context
     * @return
     */
    public static Context getContext(){
        return mContext;
    }

}