package com.example.wanhao.aclassapp.presenter;

import android.content.Context;

import com.example.wanhao.aclassapp.view.IDocumentView;

/**
 * Created by wanhao on 2018/3/19.
 */

public class DocumentPresenter {

    private IDocumentView iDocumentView;
    private Context mContext;

    public DocumentPresenter(IDocumentView loginView, Context context) {
        this.iDocumentView = loginView;
        this.mContext = context;
    }

    public void getDocumentList(String courseID){

    }

    public void downloadDocument(String courseID,String DocumentID){

    }

    public void deleteDocument(String courseID,String DocumentID){

    }



}
