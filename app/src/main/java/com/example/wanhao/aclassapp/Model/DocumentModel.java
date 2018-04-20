package com.example.wanhao.aclassapp.Model;

import android.content.Context;
import android.util.Log;

import com.example.wanhao.aclassapp.SQLite.DocumentDao;
import com.example.wanhao.aclassapp.base.IBaseRequestCallBack;
import com.example.wanhao.aclassapp.bean.sqlbean.Document;
import com.example.wanhao.aclassapp.bean.sqlbean.DocumentResult;
import com.example.wanhao.aclassapp.config.ApiConstant;
import com.example.wanhao.aclassapp.service.DocumentService;
import com.example.wanhao.aclassapp.util.RetrofitHelper;
import com.example.wanhao.aclassapp.util.SaveDataUtil;
import com.google.gson.Gson;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * Created by wanhao on 2018/3/19.
 */

public class DocumentModel {
    private static final String TAG = "DocumentModel";

    private Context context;
    private DocumentDao dao;

    public DocumentModel(Context context){
        this.context = context;
        dao = new DocumentDao(context);
    }

    public void getDocumentList(final String courseID, final IBaseRequestCallBack callBack){
        //----------从数据库取数据--------------------

        //----------从服务器取数据--------------------
        DocumentService service = RetrofitHelper.get(DocumentService.class);

        service.getDocumentList(SaveDataUtil.getValueFromSharedPreferences(context, ApiConstant.USER_TOKEN),Integer.valueOf(courseID))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Response<ResponseBody>>() {
                    @Override
                    public void accept(Response<ResponseBody> responseBodyResponse) throws Exception {
                        String accept = responseBodyResponse.body().string();
                        Log.i(TAG, "accept: "+accept);
                        DocumentResult result = new Gson().fromJson(accept,DocumentResult.class);
                        if(result.getStatus().equals(ApiConstant.RETURN_SUCCESS)){
                            List<Document> list = result.getCourses();
                            dao.addDocumentList(list,SaveDataUtil.getValueFromSharedPreferences(context,ApiConstant.COUNT),courseID);
                            callBack.requestSuccess(list);
                        }else{
                            callBack.requestError(new Throwable("error"));
                        }
                    }
                },new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.i(TAG, "accept: error "+throwable);
                        callBack.requestError(throwable);
                    }
                });


    }

    public void getPreviewList(final String courseID, final IBaseRequestCallBack callBack){
        //----------从数据库取数据--------------------

        //----------从服务器取数据--------------------
        DocumentService service = RetrofitHelper.get(DocumentService.class);


        service.getPreviewList(SaveDataUtil.getValueFromSharedPreferences(context, ApiConstant.USER_TOKEN),Integer.valueOf(courseID))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Response<ResponseBody>>() {
                    @Override
                    public void accept(Response<ResponseBody> responseBodyResponse) throws Exception {
                        String accept = responseBodyResponse.body().string();
                        Log.i(TAG, "accept: "+accept);
                        DocumentResult result = new Gson().fromJson(accept,DocumentResult.class);

                        if(result.getStatus().equals(ApiConstant.RETURN_SUCCESS)){

                            List<Document> list = result.getCourses();
                            dao.addDocumentList(list,SaveDataUtil.getValueFromSharedPreferences(context,ApiConstant.COUNT),courseID);
                            //dao.addCourseList(SaveDataUtil.getValueFromSharedPreferences(context,ApiConstant.USER_NAME),list);
                            callBack.requestSuccess(list);
                        }else{
                            Log.i(TAG, " getPreviewList error");
                            callBack.requestError(new Throwable("error"));
                        }
                    }
                },new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.i(TAG, throwable.toString());
                        callBack.requestError(throwable);
                    }
                });

    }

}

