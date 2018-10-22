package com.example.wanhao.aclassapp.presenter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

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

    public DocumentPresenter(IDocumentView view, Context context) {
        this.view = view;
        this.context = context;
    }

    public void getListByCourse(){


    }

    @SuppressLint("CheckResult")
    public void getListByInternet(String courseID){

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

    }

    public void checkDocument(Document document){

    }

    class DocumentList{

    }
}
