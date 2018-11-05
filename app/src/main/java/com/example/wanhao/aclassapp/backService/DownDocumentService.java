package com.example.wanhao.aclassapp.backService;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.util.SparseArray;

import com.example.wanhao.aclassapp.R;
import com.example.wanhao.aclassapp.activity.BrowseDocumentActivity;
import com.example.wanhao.aclassapp.bean.Document;
import com.example.wanhao.aclassapp.config.ApiConstant;
import com.example.wanhao.aclassapp.util.FileConvertUtil;
import com.example.wanhao.aclassapp.util.NotificationUtils;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import static com.example.wanhao.aclassapp.config.ApiConstant.BASE_URL;
import static com.example.wanhao.aclassapp.config.ApiConstant.CHANNE_DOWNED_ID;
import static com.example.wanhao.aclassapp.config.ApiConstant.CHANNE_DOWNED_NAME;
import static com.example.wanhao.aclassapp.config.ApiConstant.DOCUMENT;
import static com.example.wanhao.aclassapp.config.ApiConstant.DOCUMENT_ID;
import static com.example.wanhao.aclassapp.config.ApiConstant.DOCUMENT_URL;
import static com.example.wanhao.aclassapp.config.ApiConstant.DOWNED_BEAN;
import static com.example.wanhao.aclassapp.config.ApiConstant.DOWNED_BEGIN;
import static com.example.wanhao.aclassapp.config.ApiConstant.DOWNED_FINISH;
import static com.example.wanhao.aclassapp.config.ApiConstant.DOWNED_ID;
import static com.example.wanhao.aclassapp.config.ApiConstant.DOWNED_ING;
import static com.example.wanhao.aclassapp.config.ApiConstant.DOWNED_PROCESS;
import static com.example.wanhao.aclassapp.config.ApiConstant.DOWNED_STATE;
import static com.example.wanhao.aclassapp.util.FileConvertUtil.isFolderExists;
import static com.example.wanhao.aclassapp.util.NotificationUtils.createNotificationChanne;

/**
 * Created by wanhao on 2018/3/24.
 */

public class DownDocumentService extends Service {
    private static final String TAG = "DownDocumentService";

    private Intent broadcastIntent;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        broadcastIntent = new Intent(ApiConstant.DOWNLOAD_ACTION);
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand: ");

        if(intent == null){
            return super.onStartCommand(intent, flags, startId);
        }

        Document document = (Document) intent.getSerializableExtra(DOCUMENT);
        int taskID = intent.getIntExtra(ApiConstant.DOWNLOAD_ID,-1);

        if(taskID != -1){
            Log.i(TAG, "onStartCommand: StopTask");
            FileDownloader.getImpl().pause(taskID);
            return super.onStartCommand(intent, flags, startId);
        }

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            startDownload(document);
        else
            startDownloadBelow(document);

        return super.onStartCommand(intent, flags, startId);
    }

    public void startDownloadBelow(Document document){

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void startDownload(Document document){

        String path = FileConvertUtil.getFileDocumentPath();
        Log.i(TAG, "startDownload: path = "+path);

        if(!isFolderExists(path)){
            Log.i(TAG, "startDownload: 创建目录失败");
            return;
        }

        NotificationManager notificationManager= (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        createNotificationChanne(CHANNE_DOWNED_ID, CHANNE_DOWNED_NAME, NotificationManager.IMPORTANCE_HIGH,notificationManager);

        Notification.Builder builder = new Notification.Builder(this,CHANNE_DOWNED_ID);
        Notification.Builder finishBuilder = new Notification.Builder(this,CHANNE_DOWNED_ID);

        Intent documentIntent = new Intent(this, BrowseDocumentActivity.class);
        documentIntent.putExtra(ApiConstant.DOCUMENT,document);
        PendingIntent mainPendingIntent = PendingIntent.getActivity(this, 0, documentIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        builder.setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(document.getTitle())
                .setAutoCancel(true)
                .setContentIntent(mainPendingIntent);

        finishBuilder.setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(document.getTitle())
                .setAutoCancel(true)
                .setContentIntent(mainPendingIntent);


        FileDownloader.getImpl().create(DOCUMENT_URL+"?path="+document.getPath())
                .setPath(path+document.getTitle())
                .setListener(new FileDownloadListener() {
                    @Override
                    protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        Log.i(TAG, "pending: ");
                        //等待，已经进入下载队列
                        int present = soFarBytes*100/Integer.valueOf(document.getSize());
                        broadcastIntent.putExtra(DOWNED_BEAN,new DownLoadBean(task.getId(),document.getId(), ApiConstant.DOWNLOAD_STATE.ING,present+"%"));
                        sendBroadcast(broadcastIntent);
                    }

                    @Override
                    protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        //下载进度回调
                        Log.i(TAG, "progress: soFarByt = "+soFarBytes + " totalBytes = "+totalBytes + " taskID = "+task.getId());
                        int present = soFarBytes*100/Integer.valueOf(document.getSize());
                        builder.setContentText("已下载 "+present+"%")
                            .setProgress(100, present, false);
                        notificationManager.notify(task.getId(),builder.build());

                        broadcastIntent.putExtra(DOWNED_BEAN,new DownLoadBean(task.getId(),document.getId(), ApiConstant.DOWNLOAD_STATE.ING,present+"%"));
                        sendBroadcast(broadcastIntent);
                    }

                    @Override
                    protected void completed(BaseDownloadTask task) {
                        Log.i(TAG, "completed: ");
                        //完成整个下载过程
                        finishBuilder.setContentText("下载完成");

                        notificationManager.notify(task.getId(),finishBuilder.build());

                        broadcastIntent.putExtra(DOWNED_BEAN,new DownLoadBean(task.getId(),document.getId(), ApiConstant.DOWNLOAD_STATE.FINISH,"完成"));
                        sendBroadcast(broadcastIntent);

                    }

                    @Override
                    protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        Log.i(TAG, "paused: ");
                        //暂停下载
                        finishBuilder.setContentText("下载暂停 重新下载");

                        notificationManager.notify(task.getId(),finishBuilder.build());

                        broadcastIntent.putExtra(DOWNED_BEAN,new DownLoadBean(task.getId(),document.getId(), ApiConstant.DOWNLOAD_STATE.STOP,"重新下载"));
                        sendBroadcast(broadcastIntent);
                    }

                    @Override
                    protected void error(BaseDownloadTask task, Throwable e) {
                        Log.i(TAG, "error: " + e);

                        finishBuilder.setContentText("下载出错 重新下载");
                        notificationManager.notify(task.getId(),finishBuilder.build());

                        broadcastIntent.putExtra(DOWNED_BEAN,new DownLoadBean(task.getId(),document.getId(), ApiConstant.DOWNLOAD_STATE.NONE,"出错"));
                        sendBroadcast(broadcastIntent);
                    }

                    @Override
                    protected void warn(BaseDownloadTask task) {
                        //在下载队列中(正在等待/正在下载)已经存在相同下载连接与相同存储路径的任务
                        Log.i(TAG, "warn: ");
                        broadcastIntent.putExtra(DOWNED_BEAN,new DownLoadBean(task.getId(),document.getId(), ApiConstant.DOWNLOAD_STATE.NONE,"警告"));
                        sendBroadcast(broadcastIntent);
                    }
                }).start();
    }

    public static class DownLoadBean implements Serializable{
        public int taskID;
        public int documentID;
        public ApiConstant.DOWNLOAD_STATE state;
        public String message;

        DownLoadBean(int taskID, int documentID, ApiConstant.DOWNLOAD_STATE state, String message){
            this.taskID = taskID;
            this.documentID = documentID;
            this.message = message;
            this.state = state;
        }
    }
}
