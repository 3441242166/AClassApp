package com.example.wanhao.aclassapp.view;

import com.example.wanhao.aclassapp.bean.sqlbean.Document;

import java.util.List;

/**
 * Created by wanhao on 2018/3/19.
 */

public interface IDocumentFgView {
    void showProgress();
    void disimissProgress();
    void loadDataError(String throwable);
    void loadDataSuccess(List<Document> tData,String type);
    void tokenError();
}
