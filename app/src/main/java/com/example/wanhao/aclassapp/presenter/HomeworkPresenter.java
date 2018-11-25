package com.example.wanhao.aclassapp.presenter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import com.example.wanhao.aclassapp.R;
import com.example.wanhao.aclassapp.base.IBasePresenter;
import com.example.wanhao.aclassapp.bean.Homework;
import com.example.wanhao.aclassapp.bean.HttpResult;
import com.example.wanhao.aclassapp.config.ApiConstant;
import com.example.wanhao.aclassapp.service.HomeworkService;
import com.example.wanhao.aclassapp.util.ResourcesUtil;
import com.example.wanhao.aclassapp.util.RetrofitHelper;
import com.example.wanhao.aclassapp.util.SaveDataUtil;
import com.example.wanhao.aclassapp.view.IHomeworkView;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.example.wanhao.aclassapp.config.ApiConstant.RETURN_SUCCESS;

public class HomeworkPresenter implements IBasePresenter {
    private static final String TAG = "HomeworkPresenter";

    Context context;
    IHomeworkView view;

    public HomeworkPresenter(Context context,IHomeworkView view){
        this.context = context;
        this.view = view;
    }

    @SuppressLint("CheckResult")
    public void getHomeworkList(String courseID){

        HomeworkService service = RetrofitHelper.get(HomeworkService.class);

        service.getHomeworkList(SaveDataUtil.getValueFromSharedPreferences(context, ApiConstant.USER_TOKEN),courseID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(responseBodyResponse -> {
                    String body = responseBodyResponse.body().string();
                    Log.i(TAG, "accept: "+body);

                    HttpResult<HomeworkData> result = new Gson().fromJson(body,new TypeToken<HttpResult<HomeworkData>>(){}.getType());

                    if(result.getCode().equals(RETURN_SUCCESS)){
                        view.loadDataSuccess(result.getData().list);
                    }else{
                        view.errorMessage(result.getMessage());
                    }
                }, throwable -> {
                    view.errorMessage(ResourcesUtil.getString(R.string.error_internet));
                    Log.i(TAG, "accept: "+throwable);
                });

    }

    @Override
    public void destroy() {

    }

    static class HomeworkData{
        @SerializedName("titles")
        public List<Homework> list;
    }

}
