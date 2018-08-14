package com.example.wanhao.aclassapp.presenter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import com.example.wanhao.aclassapp.bean.HttpResult;
import com.example.wanhao.aclassapp.bean.Remark;
import com.example.wanhao.aclassapp.bean.RemarkRequset;
import com.example.wanhao.aclassapp.config.ApiConstant;
import com.example.wanhao.aclassapp.service.RemarkService;
import com.example.wanhao.aclassapp.util.RetrofitHelper;
import com.example.wanhao.aclassapp.util.SaveDataUtil;
import com.example.wanhao.aclassapp.view.IRemarkView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by wanhao on 2018/3/28.
 */

public class RemarkPresenter {
    private static final String TAG = "RemarkPresenter";

    private IRemarkView iRemarkView;
    private Context context;

    public RemarkPresenter(IRemarkView view,Context context){
        this.iRemarkView = view;
        this.context = context;
    }

    @SuppressLint("CheckResult")
    public void getRemark(int courseID){
        Log.i(TAG, "getRemark: courseID "+courseID);
        RemarkService service = RetrofitHelper.get(RemarkService.class);

        service.getRemark(SaveDataUtil.getValueFromSharedPreferences(context,ApiConstant.USER_TOKEN),courseID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(responseBodyResponse -> {
                    String body = responseBodyResponse.body().string();
                    Log.i(TAG, "accept: "+body);

                    HttpResult<List<Remark>> result = new Gson().fromJson(body,new TypeToken<HttpResult<List<Remark>>>(){}.getType());

                    if(result.getCode().equals(ApiConstant.RETURN_SUCCESS)){

                        List<Remark> list = result.getData();
                        List<Remark> temp = new ArrayList<>();

                        for(int x=0;x<list.size();x++){
                            temp.add(list.get(list.size()-x-1));
                        }
                        iRemarkView.loadDataSuccess(temp);

                    }else{
                        iRemarkView.loadDataError(result.getMessage());
                    }

                }, throwable -> {
                    iRemarkView.loadDataError("网络错误");
                    Log.i(TAG, "getRemark: "+throwable);
                });

    }

    @SuppressLint("CheckResult")
    public void sendRemark(String remark, int courseID, int replyID){
        Log.i(TAG, "sendRemark: ");
        RemarkRequset remarkRequset = new RemarkRequset(replyID,remark);

        String json = new Gson().toJson(remarkRequset);
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody requestBody = RequestBody.create(JSON, json);
        Log.i(TAG, "sendRemark: json  "+json);
        RemarkService service = RetrofitHelper.get(RemarkService.class);

        service.sendRemark(SaveDataUtil.getValueFromSharedPreferences(context,ApiConstant.USER_TOKEN),courseID,requestBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(responseBodyResponse -> {
                    String body = responseBodyResponse.body().string();
                    Log.i(TAG, "accept: "+body);

                    HttpResult<String> result = new Gson().fromJson(body,new TypeToken<HttpResult<String>>(){}.getType());

                    if(result.getCode().equals(ApiConstant.RETURN_SUCCESS)){
                        iRemarkView.sendRemarkSucess();
                    }else{
                        iRemarkView.sendRemarkError(result.getMessage());
                    }
                }, throwable ->{
                    iRemarkView.sendRemarkError("网络错误");
                    Log.i(TAG, "getRemark: "+throwable);
                });

    }

}
