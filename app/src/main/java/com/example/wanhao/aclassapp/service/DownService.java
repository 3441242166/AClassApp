package com.example.wanhao.aclassapp.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.example.wanhao.aclassapp.R;
import com.example.wanhao.aclassapp.config.ApiConstant;

/**
 * Created by wanhao on 2018/3/24.
 */

public class DownService extends Service {
    private static final String TAG = "DownService";

    private int documentID;
    private Intent broadcastIntent;
    //  标记是否有任务在进行
    private boolean isUse;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        broadcastIntent = new Intent(ApiConstant.DOWNLOAD_STATE);
        isUse = false;
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {

//        int choose = 0;
//        switch (choose){
//            case 0:
//                break;
//            case 1:
//                break;
//            case 2:
//                break;
//            case 3:
//                break;
//        }

        //如果有任务正在进行
        if(isUse) {
            Toast.makeText(this,"已有一个任务在进行,清稍后重试",Toast.LENGTH_SHORT).show();
            return super.onStartCommand(intent, flags, startId);
        }
        //如果没有任务进行 则获取文件id 启动下载
        documentID = intent.getIntExtra(ApiConstant.Document_ID,-1);
        show();
        return super.onStartCommand(intent, flags, startId);
    }


    public  void show(){
        isUse = true;
        final Notification.Builder builder = new Notification.Builder(this);
        builder.setSmallIcon(R.drawable.icon_pdf);
        builder.setContentTitle("原子弹制作原理.PDF");
        builder.setContentText("正在下载");
        final NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(1, builder.build());
        builder.setProgress(100,0,false);
        //下载以及安装线程模拟
        new Thread(new Runnable() {
            @Override
            public void run() {
                for(int i=0;i<100;i++){
                    builder.setProgress(100,i,false);
                    manager.notify(1,builder.build());
                    //下载进度提示
                    builder.setContentText("下载"+i+"%");
                    broadcastIntent.putExtra(ApiConstant.DOWNLOAD_STATE,ApiConstant.DOWNLOAD_STATE_ING);
                    broadcastIntent.putExtra(ApiConstant.Document_ID,documentID);
                    sendBroadcast(broadcastIntent);
                    try {
                        Thread.sleep(50);//演示休眠50毫秒
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                //下载完成后更改标题以及提示信息
                //builder.setContentTitle("开始安装");
                builder.setContentText("安装中...");
                //设置进度为不确定，用于模拟安装
                builder.setProgress(0,0,true);
                manager.notify(1,builder.build());
//                manager.cancel(NO_3);//设置关闭通知栏
                manager.cancel(1);
                broadcastIntent.putExtra(ApiConstant.DOWNLOAD_STATE,ApiConstant.DOWNLOAD_STATE_FINISH);
                broadcastIntent.putExtra(ApiConstant.Document_ID,documentID);
                isUse =false;
                sendBroadcast(broadcastIntent);
            }
        }).start();

    }

    private void startDownload(){

    }

    private void stopDownload(){

    }

    private void finishDownload(){

    }

    private void continueDownload(){

    }

}
