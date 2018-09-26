package com.example.wanhao.aclassapp.presenter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import com.example.wanhao.aclassapp.R;
import com.example.wanhao.aclassapp.bean.Homework;
import com.example.wanhao.aclassapp.bean.HttpResult;
import com.example.wanhao.aclassapp.config.ApiConstant;
import com.example.wanhao.aclassapp.service.HomeworkService;
import com.example.wanhao.aclassapp.util.DateUtil;
import com.example.wanhao.aclassapp.util.ResourcesUtil;
import com.example.wanhao.aclassapp.util.RetrofitHelper;
import com.example.wanhao.aclassapp.util.SaveDataUtil;
import com.example.wanhao.aclassapp.view.IHomeworkView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.example.wanhao.aclassapp.config.ApiConstant.RETURN_SUCCESS;

public class HomeworkPresenter {
    private static final String TAG = "HomeworkPresenter";

    Context context;
    IHomeworkView view;

    public HomeworkPresenter(Context context,IHomeworkView view){
        this.context = context;
        this.view = view;
    }

    @SuppressLint("CheckResult")
    public void getHomeworkList(String courseID){
//        List<Homework> tData = new ArrayList<>();
//        for(int x=0;x<10;x++){
//            Homework homework = new Homework();
//            homework.setTitle("x = "+x);
//            homework.setDate(DateUtil.getNowDateString());
//            tData.add(homework);
//        }
//        view.loadDataSuccess(tData);

        HomeworkService service = RetrofitHelper.get(HomeworkService.class);

        service.getHomeworkList(SaveDataUtil.getValueFromSharedPreferences(context, ApiConstant.USER_TOKEN),courseID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(responseBodyResponse -> {
                    String body = responseBodyResponse.body().string();
                    Log.i(TAG, "accept: "+body);

                    HttpResult<String> result = new Gson().fromJson(body,new TypeToken<HttpResult<String>>(){}.getType());

                    if(result.getCode().equals(RETURN_SUCCESS)){
                        //view.loadDataSuccess(result.getMessage());
                    }else{
                        view.loadDataError(result.getMessage());
                    }
                }, throwable -> {
                    view.loadDataError(ResourcesUtil.getString(R.string.error_internet));
                    Log.i(TAG, "accept: "+throwable);
                });

    }



}