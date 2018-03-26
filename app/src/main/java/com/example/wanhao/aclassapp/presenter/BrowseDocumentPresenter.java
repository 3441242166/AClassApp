package com.example.wanhao.aclassapp.presenter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.wanhao.aclassapp.SQLite.DocumentDao;
import com.example.wanhao.aclassapp.activity.BrowseDocumentActivity;
import com.example.wanhao.aclassapp.bean.Document;
import com.example.wanhao.aclassapp.config.ApiConstant;
import com.example.wanhao.aclassapp.service.DownService;
import com.example.wanhao.aclassapp.util.FileConvertUtil;
import com.example.wanhao.aclassapp.util.FileSizeUtil;
import com.example.wanhao.aclassapp.util.SaveDataUtil;
import com.example.wanhao.aclassapp.view.BrowseDocumentView;

import java.io.File;

/**
 * Created by wanhao on 2018/3/26.
 */

public class BrowseDocumentPresenter {
    private static final String TAG = "BrowseDocumentPresenter";

    private Context context;
    private BrowseDocumentView view;

    private DocumentDao dao;

    private int courseID;
    private int documentID;

    public BrowseDocumentPresenter(Context context,BrowseDocumentView browseDocumentView){
        this.context = context;
        this.view = browseDocumentView;
        dao = new DocumentDao(context);
    }

    public void checkDocument(String documentID){
        Document document = dao.alterDocument(documentID, SaveDataUtil.getValueFromSharedPreferences(context, ApiConstant.COUNT));
        view.setDocument(document);
        courseID = Integer.valueOf(document.getCourseID());
        this.documentID = document.getId();
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
                view.documentState(BrowseDocumentActivity.STATE.FINISH);
            }else{
                Log.i(TAG, "checkDocument: none");
                view.documentState(BrowseDocumentActivity.STATE.NONE);
            }
        }else{
            Log.i(TAG, "checkDocument: file not exists");
            view.documentState(BrowseDocumentActivity.STATE.NONE);
        }

    }

    public void startDownload(){
        Log.i(TAG, "startDownload: courseID  "+courseID+"  documentID  "+documentID);
        Intent intent = new Intent(context, DownService.class);
        intent.putExtra(ApiConstant.Document_ID,documentID);
        intent.putExtra(ApiConstant.COURSE_ID,courseID);
        context.startService(intent);
    }

    public void cancalDownload(){
        Log.i(TAG, "cancalDownload: ");
        Intent intent = new Intent(context, DownService.class);
        intent.putExtra(ApiConstant.Document_ID,documentID);
        intent.putExtra(ApiConstant.COURSE_ID,courseID);
        context.stopService(intent);
    }

    public void openDocument(){

    }

}
