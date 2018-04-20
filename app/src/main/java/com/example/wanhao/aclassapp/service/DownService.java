package com.example.wanhao.aclassapp.service;

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
import com.example.wanhao.aclassapp.bean.sqlbean.Document;
import com.example.wanhao.aclassapp.config.ApiConstant;
import com.example.wanhao.aclassapp.util.FileConvertUtil;
import com.example.wanhao.aclassapp.util.RetrofitHelper;
import com.example.wanhao.aclassapp.util.SaveDataUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * Created by wanhao on 2018/3/24.
 */

public class DownService extends Service {
    private static final String TAG = "DownService";

    private Document document;
    private int documentID;

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
        documentID = intent.getIntExtra(ApiConstant.DOCUMENT_ID, -1);
        document = new DocumentDao(this).alterDocument(String.valueOf(documentID), SaveDataUtil.getValueFromSharedPreferences(this, ApiConstant.COUNT));
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
        manager.notify(documentID, builder.build());
        builder.setProgress(100,0,false);
    }

    private void download(){
        isUse = true;

        Log.i(TAG, "download: courseID  "+document.getCourseID()+"  documentID  "+documentID);

        DocumentService service = RetrofitHelper.get(DocumentService.class);

        service.downloadDocument(SaveDataUtil.getValueFromSharedPreferences(this,ApiConstant.USER_TOKEN),Integer.valueOf(document.getCourseID()),documentID)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new io.reactivex.functions.Consumer<Response<ResponseBody>>() {
                    @Override
                    public void accept(Response<ResponseBody> response) throws Exception {
                        InputStream is = response.body().byteStream();
                        byte[] buf = new byte[2048];
                        int len = 0;
                        FileOutputStream fos = null;
                        // 储存下载文件的目录
                        String savePath = FileConvertUtil.getDocumentFilePath();
                        try {
                            long total = Integer.valueOf(document.getSize());
                            File file = new File(savePath+"/"+document.getTitle());

                            file.createNewFile();
                            fos = new FileOutputStream(file);
                            long sum = 0;
                            int progress;
                            while ((len = is.read(buf)) != -1) {
                                fos.write(buf, 0, len);
                                sum += len;
                                progress = (int) (sum * 1.0f / total * 100);

                                builder.setProgress(100,progress,false);
                                builder.setContentText("下载"+progress+"%");
                                manager.notify(documentID,builder.build());

                                broadcastIntent.putExtra(ApiConstant.DOWNLOAD_STATE,"下载 "+progress+"%");
                                broadcastIntent.putExtra(ApiConstant.DOCUMENT_ID,documentID);
                                sendBroadcast(broadcastIntent);

                                if(isStop){
                                    broadcastIntent.putExtra(ApiConstant.DOWNLOAD_STATE,"重新下载");
                                    broadcastIntent.putExtra(ApiConstant.DOCUMENT_ID,documentID);
                                    sendBroadcast(broadcastIntent);
                                    Log.i(TAG, "accept: 取消下载");
                                    return;
                                }
                            }

                            builder.setContentText(document.getTitle() +" 下载完成");
                            //设置进度为不确定，用于模拟安装
                            manager.notify(documentID,builder.build());
                            //manager.cancel(1);
                            broadcastIntent.putExtra(ApiConstant.DOWNLOAD_STATE,"打开");
                            broadcastIntent.putExtra(ApiConstant.DOCUMENT_ID,documentID);
                            isUse =false;
                            sendBroadcast(broadcastIntent);

                            fos.flush();

                        } catch (Exception e) {
                            Log.i(TAG, "saveDocument: "+e.toString());
                        } finally {

                            try {
                                if (is != null)
                                    is.close();
                            } catch (IOException e) {
                                Log.i(TAG, "saveDocument: "+e.toString());
                            }
                            try {
                                if (fos != null)
                                    fos.close();
                            } catch (IOException e) {
                                Log.i(TAG, "saveDocument: "+e.toString());
                            }
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.i(TAG, "accept: throwable "+throwable.toString());
                    }
                });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isStop = true;
    }

    //    public  void show(){
//
//
//        //下载以及安装线程模拟
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                for(int i=0;i<100;i++){
//                    builder.setProgress(100,i,false);
//                    manager.notify(1,builder.build());
//                    //下载进度提示
//                    builder.setContentText("下载"+i+"%");
//                    broadcastIntent.putExtra(ApiConstant.DOWNLOAD_STATE,ApiConstant.DOWNLOAD_STATE_ING);
//                    broadcastIntent.putExtra(ApiConstant.Document_ID,documentID);
//                    sendBroadcast(broadcastIntent);
//                    try {
//                        Thread.sleep(50);//演示休眠50毫秒
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//                //下载完成后更改标题以及提示信息
//                //builder.setContentTitle("开始安装");
//                builder.setContentText("安装中...");
//                //设置进度为不确定，用于模拟安装
//                builder.setProgress(0,0,true);
//                manager.notify(1,builder.build());
////                manager.cancel(NO_3);//设置关闭通知栏
//                manager.cancel(1);
//                broadcastIntent.putExtra(ApiConstant.DOWNLOAD_STATE,ApiConstant.DOWNLOAD_STATE_FINISH);
//                broadcastIntent.putExtra(ApiConstant.Document_ID,documentID);
//                isUse =false;
//                sendBroadcast(broadcastIntent);
//            }
//        }).start();
//
//
//        ProgressListener listener = new ProgressListener() {
//            @Override
//            public void onProgress(ProgressInfo progressInfo) {
//                if(progressInfo.isFinish()){
//                    broadcastIntent.putExtra(ApiConstant.DOWNLOAD_STATE,ApiConstant.DOWNLOAD_STATE_FINISH);
//                    broadcastIntent.putExtra(ApiConstant.Document_ID,documentID);
//                    isUse =false;
//                    sendBroadcast(broadcastIntent);
//                }
//                builder.setContentText("下载"+progressInfo.getPercent()+"%");
//            }
//
//            @Override
//            public void onError(long id, Exception e) {
//
//            }
//        };
//
//        ProgressManager.getInstance().addResponseListener("", listener);
//    }

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
