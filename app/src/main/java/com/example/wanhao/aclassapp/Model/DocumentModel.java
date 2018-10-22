package com.example.wanhao.aclassapp.Model;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import com.example.wanhao.aclassapp.R;
import com.example.wanhao.aclassapp.base.IBaseRequestCallBack;
import com.example.wanhao.aclassapp.bean.HttpResult;
import com.example.wanhao.aclassapp.bean.Document;
import com.example.wanhao.aclassapp.config.ApiConstant;
import com.example.wanhao.aclassapp.service.DocumentService;
import com.example.wanhao.aclassapp.util.ResourcesUtil;
import com.example.wanhao.aclassapp.util.RetrofitHelper;
import com.example.wanhao.aclassapp.util.SaveDataUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.example.wanhao.aclassapp.config.ApiConstant.RETURN_SUCCESS;

/**
 * Created by wanhao on 2018/3/19.
 */

public class DocumentModel {
    private static final String TAG = "DocumentModel";

    private Context context;


    public DocumentModel(Context context){
        this.context = context;

    }

    @SuppressLint("CheckResult")
    public void getDocumentList(final String courseID, final IBaseRequestCallBack callBack){

        //----------从服务器取数据--------------------
        DocumentService service = RetrofitHelper.get(DocumentService.class);

        service.getDocumentList(SaveDataUtil.getValueFromSharedPreferences(context, ApiConstant.USER_TOKEN),courseID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(responseBodyResponse -> {
                    String body = responseBodyResponse.body().string();
                    Log.i(TAG, "accept: "+body);

                    HttpResult< List<Document>> result = new Gson().fromJson(body,new TypeToken<HttpResult< List<Document>>>(){}.getType());

                    if(result.getCode().equals(RETURN_SUCCESS)){
                        callBack.requestSuccess(result.getData());
                        addToDB(result.getData(),courseID,ApiConstant.DOCUMENT_EDATA);

                    }else{
                        callBack.requestError(new Throwable(result.getMessage()));
                    }
                }, throwable -> {
                    callBack.requestError(new Throwable(ResourcesUtil.getString(R.string.error_internet)));
                    Log.i(TAG, "accept: "+throwable);
                });

    }

    @SuppressLint("CheckResult")
    public void getPreviewList(final String courseID, final IBaseRequestCallBack callBack){
        //----------从服务器取数据--------------------
        DocumentService service = RetrofitHelper.get(DocumentService.class);

        service.getPreviewList(SaveDataUtil.getValueFromSharedPreferences(context, ApiConstant.USER_TOKEN),courseID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(responseBodyResponse -> {
                    String body = responseBodyResponse.body().string();
                    Log.i(TAG, "accept: "+body);

                    HttpResult< List<Document>> result = new Gson().fromJson(body,new TypeToken<HttpResult< List<Document>>>(){}.getType());

                    if(result.getCode().equals(RETURN_SUCCESS)){
                        callBack.requestSuccess(result.getData());
                        addToDB(result.getData(),courseID,ApiConstant.DOCUMENT_PREVIEW);
                    }else{
                        callBack.requestError(new Throwable(result.getMessage()));
                    }
                }, throwable -> {
                    callBack.requestError(new Throwable(ResourcesUtil.getString(R.string.error_internet)));
                    Log.i(TAG, "accept: "+throwable);
                });

    }

    private void addToDB(List<Document> list,String courseId,String type){

    }

}

