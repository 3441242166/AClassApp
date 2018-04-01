package com.example.wanhao.aclassapp.util;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity集合类统一管理Activity
 */

public class ActivityCollector {
    private static List<Activity> mActivityList = new ArrayList<>();

    /**
     * 添加一个Activity
     * @param activity
     */
    public static void addActivity(Activity activity){
        mActivityList.add(activity);
    }

    /**
     * 移除一个Activity
     * @param activity
     */
    public static void removeActivity(Activity activity){
        mActivityList.remove(activity);
    }

    /**
     * finish所有的Activity
     */
    public static void finishAll(){
        for (Activity activity : mActivityList) {
            if (!activity.isFinishing()){
                activity.finish();
            }
        }
    }
}
