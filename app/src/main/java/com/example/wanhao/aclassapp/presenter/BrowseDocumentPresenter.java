package com.example.wanhao.aclassapp.presenter;

import android.content.Context;
import android.content.Intent;


import com.example.wanhao.aclassapp.bean.Document;
import com.example.wanhao.aclassapp.config.ApiConstant;
import com.example.wanhao.aclassapp.backService.DownDocumentService;
import com.example.wanhao.aclassapp.util.FileConvertUtil;
import com.example.wanhao.aclassapp.view.IBrowseDocumentView;

import java.io.File;

import static com.example.wanhao.aclassapp.util.FileConvertUtil.getUriForFile;

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

    public void startDownload(Document document){
        Intent intent = new Intent(context,DownDocumentService.class);
        intent.putExtra(ApiConstant.DOCUMENT,document);
        context.startService(intent);
    }

    public void openDocument(Document document){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        File file = new File(FileConvertUtil.getFileDocumentPath()+document.getTitle());
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setDataAndType(getUriForFile(context,file), FileConvertUtil.getMIMEType(file));//设置类型
        context.startActivity(intent);

    }

    public void continueDownload(int id){
        Intent intent = new Intent(context, DownDocumentService.class);
        intent.putExtra(ApiConstant.DOWNLOAD_ID,id);
        context.startService(intent);
    }

    public void stopDownload(int id) {
        Intent intent = new Intent(context, DownDocumentService.class);
        intent.putExtra(ApiConstant.DOWNLOAD_ID,id);
        context.startService(intent);
    }
}
