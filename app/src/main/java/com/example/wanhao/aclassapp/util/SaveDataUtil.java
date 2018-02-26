package com.example.wanhao.aclassapp.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.wanhao.aclassapp.config.ApiConstant;

/**
 * Created by szh on 2017/5/14.
 */

public class SaveDataUtil {
    /**
     * 保存一对键值对到指定的SharedPreferences文件中
     * @param context
     * @param key
     * @param value
     * @return
     */
    public static boolean saveToSharedPreferences(Context context, String key, String value){
        SharedPreferences sharedPreferences = context.getSharedPreferences(ApiConstant.FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        return editor.commit();
    }

    /**
     * 从指定的文件中读取指定key的value
     * @param context
     * @param key
     * @return
     */
    public static String getValueFromSharedPreferences(Context context, String key){
        String value = null;
        SharedPreferences sharedPreferences = context.getSharedPreferences(ApiConstant.FILE_NAME, Context.MODE_PRIVATE);
        if (sharedPreferences != null){
            value = sharedPreferences.getString(key, null);
        }
        return value;
    }
}
