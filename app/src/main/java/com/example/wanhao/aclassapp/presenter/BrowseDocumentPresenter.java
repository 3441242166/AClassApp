package com.example.wanhao.aclassapp.presenter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.wanhao.aclassapp.SQLite.DocumentDao;
import com.example.wanhao.aclassapp.activity.BrowseDocumentActivity;
import com.example.wanhao.aclassapp.bean.Document;
import com.example.wanhao.aclassapp.config.ApiConstant;
import com.example.wanhao.aclassapp.service.DownDocumentService;
import com.example.wanhao.aclassapp.util.FileConvertUtil;
import com.example.wanhao.aclassapp.util.FileSizeUtil;
import com.example.wanhao.aclassapp.util.SaveDataUtil;
import com.example.wanhao.aclassapp.view.IBrowseDocumentView;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;

import java.io.File;

import javax.security.auth.login.LoginException;

import static com.example.wanhao.aclassapp.config.ApiConstant.BASE_URL;
import static com.example.wanhao.aclassapp.util.FileConvertUtil.isFolderExists;

/**
 * Created by wanhao on 2018/3/26.
 */

public class BrowseDocumentPresenter {
    private static final String TAG = "BrowseDocumentPresenter";

    private Context context;
    private IBrowseDocumentView view;

    public BrowseDocumentPresenter(Context context,IBrowseDocumentView IBrowseDocumentView){
        this.context = context;
        this.view = IBrowseDocumentView;
    }

    public void checkDocument(String documentID){
        Document document = new Document();
        view.setDocument(document);
        String courseID = document.getCourseID();
        String documentId = String.valueOf(document.getId());
        /*
         *  判断是否本地已下载
         */

        File file = new File(FileConvertUtil.getDocumentFilePath()+"/"+document.getTitle());
        if (file.exists()) {
            Log.i(TAG, "checkDocument: file exists" + FileSizeUtil.FormetFileSize(Integer.valueOf(document.getSize())));
            Log.i(TAG, "checkDocument: "+FileSizeUtil.getAutoFileOrFilesSize(file.getPath()));

            String documentSize =FileSizeUtil.FormetFileSize(Integer.valueOf(document.getSize()));
            if(documentSize.equals(FileSizeUtil.getAutoFileOrFilesSize(file.getPath()))){
                Log.i(TAG, "checkDocument: finish");
                //view.documentState(BrowseDocumentActivity.STATE.FINISH);
            }else{
                Log.i(TAG, "checkDocument: none");
                //view.documentState(BrowseDocumentActivity.STATE.NONE);
            }
        }else{
            Log.i(TAG, "checkDocument: file not exists");
            //view.documentState(BrowseDocumentActivity.STATE.NONE);
        }

    }

    public void startDownload(Document document){
//        Log.i(TAG, "startDownload: courseID  "+document.getCourseID()+"  documentID  "+document.getId());
//        Intent intent = new Intent(context, DownDocumentService.class);
//        intent.putExtra(ApiConstant.DOCUMENT,document);
//        context.startService(intent);
        String path = FileConvertUtil.getDocumentFilePath()+String.valueOf(document.getCourseID())+"/"+document.getCourseID()+document.getTitle();
        Log.i(TAG, "startDownload: path = "+path);
        if(isFolderExists(path)){

        }

        FileDownloader.getImpl().create(BASE_URL+"file/"+document.getId())
                .setPath(path)
                .setListener(new FileDownloadListener() {
                    @Override
                    protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        Log.i(TAG, "pending: ");
                        //等待，已经进入下载队列
                        view.setDocumentState(BrowseDocumentActivity.STATE.ING);
                    }

                    @Override
                    protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        //下载进度回调
                        Log.i(TAG, "progress: soFarByt = "+soFarBytes + " totalBytes = "+totalBytes);
                        view.setProgress(task,soFarBytes,totalBytes);
                    }

                    @Override
                    protected void completed(BaseDownloadTask task) {
                        Log.i(TAG, "completed: ");
                        //完成整个下载过程
                        view.setDocumentState(BrowseDocumentActivity.STATE.FINISH);
                    }

                    @Override
                    protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        Log.i(TAG, "paused: ");
                        //暂停下载
                    }

                    @Override
                    protected void error(BaseDownloadTask task, Throwable e) {
                        Log.i(TAG, "error: " + e);
                        //下载出现错误
                        view.downError(e);
                        view.setDocumentState(BrowseDocumentActivity.STATE.FINISH);
                    }

                    @Override
                    protected void warn(BaseDownloadTask task) {
                        //在下载队列中(正在等待/正在下载)已经存在相同下载连接与相同存储路径的任务
                    }
                }).start();
    }

    public void cancalDownload(Document document){
        Log.i(TAG, "cancalDownload: ");
        Intent intent = new Intent(context, DownDocumentService.class);
        intent.putExtra(ApiConstant.DOCUMENT,document);
        context.stopService(intent);
    }

    public void openDocument(){

    }

}
