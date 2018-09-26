package com.example.wanhao.aclassapp.presenter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import com.example.wanhao.aclassapp.SQLite.DocumentDao;
import com.example.wanhao.aclassapp.bean.Course;
import com.example.wanhao.aclassapp.bean.CourseListData;
import com.example.wanhao.aclassapp.bean.Document;
import com.example.wanhao.aclassapp.bean.HttpResult;
import com.example.wanhao.aclassapp.config.ApiConstant;
import com.example.wanhao.aclassapp.service.DocumentService;
import com.example.wanhao.aclassapp.util.RetrofitHelper;
import com.example.wanhao.aclassapp.util.SaveDataUtil;
import com.example.wanhao.aclassapp.view.IDocumentView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by wanhao on 2018/3/22.
 */

public class DocumentPresenter {
    private static final String TAG = "DocumentPresenter";

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

    @SuppressLint("CheckResult")
    public void getListByInternet(String courseID){
        List<Document> list =  dao.alterAllDocument(SaveDataUtil.getValueFromSharedPreferences(context, ApiConstant.COUNT));


        DocumentService service = RetrofitHelper.get(DocumentService.class);

        service.getDocumentList(SaveDataUtil.getValueFromSharedPreferences(context,ApiConstant.USER_TOKEN),courseID)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(responseBodyResponse -> {
                    String body = responseBodyResponse.body().string();
                    Log.i(TAG, "getDocumentList body : "+body);

                    HttpResult<List<Document>> result = new Gson().fromJson(body,new TypeToken<HttpResult<List<Document>>>(){}.getType());


                    if(result.getCode().equals(ApiConstant.RETURN_SUCCESS)){
                        List<Document> temp = result.getData();
                        for(Document document:temp){
                            document.setCourseID(courseID);
                        }
                        view.loadDataSuccess(temp);
                    }else{
                        view.loadDataError("error");
                    }
                }, throwable -> {

                    Log.i(TAG, "accept: "+throwable);
                });

        view.loadDataSuccess(list);
    }

    class DocumentList{
        public List<Document> list;
    }
}
