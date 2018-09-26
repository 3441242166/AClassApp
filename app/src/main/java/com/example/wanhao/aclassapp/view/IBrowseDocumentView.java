package com.example.wanhao.aclassapp.view;

import com.example.wanhao.aclassapp.activity.BrowseDocumentActivity;
import com.example.wanhao.aclassapp.bean.Document;
import com.liulishuo.filedownloader.BaseDownloadTask;

/**
 * Created by wanhao on 2018/3/26.
 */

public interface IBrowseDocumentView {

    void setDocumentState(BrowseDocumentActivity.STATE state);

    void setDocument(Document document);

    void setProgress(BaseDownloadTask task, int soFarBytes, int totalBytes);

    void downError(Throwable e);

    void tokenError(String msg);

}
