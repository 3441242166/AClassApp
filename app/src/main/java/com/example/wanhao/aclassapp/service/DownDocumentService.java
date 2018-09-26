package com.example.wanhao.aclassapp.service;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.example.wanhao.aclassapp.R;
import com.example.wanhao.aclassapp.SQLite.DocumentDao;
import com.example.wanhao.aclassapp.bean.Document;
import com.example.wanhao.aclassapp.config.ApiConstant;
import com.example.wanhao.aclassapp.util.FileConvertUtil;
import com.example.wanhao.aclassapp.util.RetrofitHelper;
import com.example.wanhao.aclassapp.util.SaveDataUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * Created by wanhao on 2018/3/24.
 */

public class DownDocumentService extends Service {
    private static final String TAG = "DownDocumentService";

    private Document document;

    Notification.Builder builder;
    NotificationManager manager;
    private Intent broadcastIntent;
    //  标记是否有任务在进行
    private boolean isUse;

    private boolean isStop;

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
        isStop = false;
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand: ");
        //如果有任务正在进行
        if(isUse) {
            Toast.makeText(this,"已有一个任务在进行,清稍后重试",Toast.LENGTH_SHORT).show();
            return super.onStartCommand(intent, flags, startId);
        }

        //如果没有任务进行 则获取文件id 启动下载
        document = (Document) intent.getSerializableExtra(ApiConstant.DOCUMENT);
        initNotification();

        download();
        //show();
        return super.onStartCommand(intent, flags, startId);
    }

    private void initNotification(){
        builder = new Notification.Builder(this);

        builder.setSmallIcon(R.drawable.icon_pdf);
        builder.setContentTitle(document.getTitle());
        builder.setContentText("正在下载");

        manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(document.getId(), builder.build());
        builder.setProgress(100,0,false);

    }

    @SuppressLint("CheckResult")
    private void download(){
        isUse = true;

        Log.i(TAG, "download: courseID  "+document.getCourseID()+"  documentID  "+document.getId());

        DocumentService service = RetrofitHelper.get(DocumentService.class);

        service.downloadDocument(SaveDataUtil.getValueFromSharedPreferences(this,ApiConstant.USER_TOKEN),document.getCourseID(),document.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    InputStream is = response.body().byteStream();
                    byte[] buf = new byte[2048];
                    int len;
                    FileOutputStream fos = null;
                    // 储存下载文件的目录
                    String savePath = FileConvertUtil.getDocumentFilePath();
                    Log.i(TAG, "download: savePath = "+savePath);
                    try {
                        long total = Integer.valueOf(document.getSize());
                        File files = new File(savePath);
                        Log.i(TAG, "download: exists = "+files.exists());

                        if(!files.exists()){
                            files.mkdirs();
                            Log.i(TAG, "download: !file.exists()");
                            //Log.i(TAG, "download: createNewFile = "+file.createNewFile());
                        }

                        File file = new File(savePath+"/document/"+document.getTitle());
                        Log.i(TAG, "download: createNewFile = "+file.createNewFile());
                        fos = new FileOutputStream(file);
                        long sum = 0;
                        int progress;

                        while ((len = is.read(buf)) != -1) {
                            fos.write(buf, 0, len);
                            sum += len;
                            progress = (int) (sum * 1.0f / total * 100);

                            builder.setProgress(100,progress,false);
                            builder.setContentText("下载"+progress+"%");
                            manager.notify(document.getId(),builder.build());

                            broadcastIntent.putExtra(ApiConstant.DOWNLOAD_STATE,"下载 "+progress+"%");
                            broadcastIntent.putExtra(ApiConstant.DOCUMENT_ID,document.getId());
                            sendBroadcast(broadcastIntent);

                            if(isStop){
                                broadcastIntent.putExtra(ApiConstant.DOWNLOAD_STATE,"重新下载");
                                broadcastIntent.putExtra(ApiConstant.DOCUMENT_ID,document.getId());
                                sendBroadcast(broadcastIntent);
                                Log.i(TAG, "accept: 取消下载");
                                return;
                            }
                        }

                        builder.setContentText(document.getTitle() +" 下载完成");
                        //设置进度为不确定，用于模拟安装
                        manager.notify(document.getId(),builder.build());
                        //manager.cancel(1);
                        broadcastIntent.putExtra(ApiConstant.DOWNLOAD_STATE,"打开");
                        broadcastIntent.putExtra(ApiConstant.DOCUMENT_ID,document.getId());
                        isUse =false;
                        sendBroadcast(broadcastIntent);

                        fos.flush();

                    } catch (Exception e) {
                        Log.i(TAG, "download: "+e.toString());
                    } finally {

                        try {
                            if (is != null)
                                is.close();
                        } catch (IOException e) {
                            Log.i(TAG, "is.close(): "+e.toString());
                        }

                        try {
                            if (fos != null)
                                fos.close();
                        } catch (IOException e) {
                            Log.i(TAG, " fos.close(): "+e.toString());
                        }
                        isUse = false;
                    }
                }, throwable -> {
                    isUse = false;
                    Log.i(TAG, "accept: throwable "+throwable.toString());
                });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isStop = true;
    }

    private void startDownload(){

    }

    private void stopDownload(){

    }

    private void finishDownload(){

    }

    private void continueDownload(){

    }

    public interface DownloadListener{
        void OnDownloadListener(int progress);
    }

}