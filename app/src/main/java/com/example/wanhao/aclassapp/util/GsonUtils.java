package com.example.wanhao.aclassapp.util;

import android.util.Log;

import com.google.gson.Gson;

import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class GsonUtils {
    private static final String TAG = "GsonUtils";

    private static Gson gson = new Gson();


    /**
     * 对象转Json字符串
     *
     * @param object
     * @return
     */
    public static String toJson(Object object) {
        return gson.toJson(object);
    }

    /**
     * 字符串转Json对象
     *
     * @param json
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T fromJson(String json, Class<T> clazz) {
        return gson.fromJson(json, clazz);
    }

    /**
     * @param map
     * @param <K,V>
     * @return
     */
    public static <K,V>RequestBody toBody(Map<K,V> map){
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        String json = toJson(map);
        Log.i(TAG, "toBody: "+ json);
        return RequestBody.create(JSON, json);
    }

}
