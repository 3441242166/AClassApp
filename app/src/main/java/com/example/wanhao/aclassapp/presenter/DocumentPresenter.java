package com.example.wanhao.aclassapp.presenter;

import android.content.Context;

import com.example.wanhao.aclassapp.SQLite.DocumentDao;
import com.example.wanhao.aclassapp.bean.sqlbean.Document;
import com.example.wanhao.aclassapp.config.ApiConstant;
import com.example.wanhao.aclassapp.util.SaveDataUtil;
import com.example.wanhao.aclassapp.view.IDocumentView;

import java.util.List;

/**
 * Created by wanhao on 2018/3/22.
 */

public class DocumentPresenter {

    private IDocumentView view;
    private Context context;
    private DocumentDao dao;

    public DocumentPresenter(IDocumentView view, Context context) {
        this.view = view;
        this.context = context;
        dao = new DocumentDao(context);
    }

    public void getListByCourse(){
        List<Document> list =  dao.alterAllDocument(SaveDataUtil.getValueFromSharedPreferences(context, ApiConstant.COUNT));
        view.loadDataSuccess(list);
    }

    public void sortAndGroup(int x,int y){

    }

}
