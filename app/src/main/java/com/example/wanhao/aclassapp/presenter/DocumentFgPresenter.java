package com.example.wanhao.aclassapp.presenter;

import android.content.Context;
import android.util.Log;

import com.example.wanhao.aclassapp.Model.DocumentModel;
import com.example.wanhao.aclassapp.base.IBaseRequestCallBack;
import com.example.wanhao.aclassapp.bean.sqlbean.Document;
import com.example.wanhao.aclassapp.config.ApiConstant;
import com.example.wanhao.aclassapp.view.IDocumentFgView;

import java.util.List;

/**
 * Created by wanhao on 2018/3/19.
 */

public class DocumentFgPresenter {
    private static final String TAG = "DocumentFgPresenter";

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
        if(type.equals(ApiConstant.DOCUMENT_EDATA)){
            Log.i(TAG, "getDocumentList: getEdata");
            model.getDocumentList(courseID,callBack);
        }
        if(type.equals(ApiConstant.DOCUMENT_PREVIEW)){
            Log.i(TAG, "getDocumentList: getPreview");
            model.getPreviewList(courseID,callBack);
        }
    }


}
