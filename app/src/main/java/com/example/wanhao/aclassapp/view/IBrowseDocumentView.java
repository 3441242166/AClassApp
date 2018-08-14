package com.example.wanhao.aclassapp.view;

import com.example.wanhao.aclassapp.activity.BrowseDocumentActivity;
import com.example.wanhao.aclassapp.bean.Document;

/**
 * Created by wanhao on 2018/3/26.
 */

public interface IBrowseDocumentView {

    void documentState(BrowseDocumentActivity.STATE state);

    void setDocument(Document document);

    void tokenError(String msg);

}
