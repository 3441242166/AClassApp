package com.example.wanhao.aclassapp.presenter;

import android.annotation.SuppressLint;
import android.content.Context;

import com.example.wanhao.aclassapp.bean.Homework;
import com.example.wanhao.aclassapp.util.DateUtil;
import com.example.wanhao.aclassapp.view.IHomeworkView;

import java.util.ArrayList;
import java.util.List;

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
        List<Homework> tData = new ArrayList<>();
        for(int x=0;x<10;x++){
            Homework homework = new Homework();
            homework.setTitle("x = "+x);
            homework.setDate(DateUtil.getNowDateString());
            tData.add(homework);
        }
        view.loadDataSuccess(tData);

//        HomeworkService service = RetrofitHelper.get(HomeworkService.class);
//
//        service.getHomeworkList(SaveDataUtil.getValueFromSharedPreferences(context, ApiConstant.USER_TOKEN),courseID)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(responseBodyResponse -> {
//                    String body = responseBodyResponse.body().string();
//                    Log.i(TAG, "accept: "+body);
//
//                    HttpResult<String> result = new Gson().fromJson(body,new TypeToken<HttpResult<String>>(){}.getType());
//
//                    if(result.getCode().equals(RETURN_SUCCESS)){
//                        //view.loadDataSuccess(result.getMessage());
//                    }else{
//                        view.loadDataError(result.getMessage());
//                    }
//                }, throwable -> {
//                    view.loadDataError(ResourcesUtil.getString(R.string.error_internet));
//                    Log.i(TAG, "accept: "+throwable);
//                });

    }



}
