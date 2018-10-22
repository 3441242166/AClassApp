package com.example.wanhao.aclassapp.util;

import android.annotation.TargetApi;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;


public class NotificationUtils {



    @TargetApi(Build.VERSION_CODES.O)
    public static void createNotificationChanne(String channelId, String channelName, int importance, NotificationManager notificationManager) {
        NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
        notificationManager.createNotificationChannel(channel);
    }



}
