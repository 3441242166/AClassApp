package com.example.wanhao.aclassapp.presenter;

import android.content.Context;

import com.example.wanhao.aclassapp.Model.DocumentModel;
import com.example.wanhao.aclassapp.base.IBaseRequestCallBack;
import com.example.wanhao.aclassapp.bean.Document;
import com.example.wanhao.aclassapp.view.IDocumentFgView;

import java.util.List;

/**
 * Created by wanhao on 2018/3/19.
 */

public class DocumentFgPresenter {

    private IDocumentFgView view;
    private Context mContext;
    private DocumentModel model;

    public DocumentFgPresenter(IDocumentFgView loginView, Context context) {
        this.view = loginView;
        this.mContext = context;
        model = new DocumentModel(context);
    }

    public void getDocumentList(String courseID, final String type){
        IBaseRequestCallBack<List<Document>> callBack = new IBaseRequestCallBack<List<Document>>() {
            @Override
            public void beforeRequest() {
                view.showProgress();
            }

            @Override
            public void requestError(Throwable throwable) {
                view.disimissProgress();
                view.loadDataError(throwable.toString());
            }

            @Override
            public void requestComplete() {
                view.disimissProgress();
            }

            @Override
            public void requestSuccess(List<Document> callBack) {
                view.disimissProgress();
                view.loadDataSuccess(callBack,type);
            }
        };
        if(type.equals("edata")){
            model.getDocumentList(courseID,callBack);
        }
        if(type.equals("preview")){
            model.getPreviewList(courseID,callBack);
        }
    }

    public void downloadDocument(String courseID,String DocumentID){

    }

    public void deleteDocument(String courseID,String DocumentID){

    }



}
