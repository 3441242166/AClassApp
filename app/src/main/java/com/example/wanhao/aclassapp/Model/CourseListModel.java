package com.example.wanhao.aclassapp.Model;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import com.example.wanhao.aclassapp.SQLite.ChatDao;
import com.example.wanhao.aclassapp.SQLite.CourseDao;
import com.example.wanhao.aclassapp.base.IBaseRequestCallBack;
import com.example.wanhao.aclassapp.bean.HttpResult;
import com.example.wanhao.aclassapp.bean.Course;
import com.example.wanhao.aclassapp.config.ApiConstant;
import com.example.wanhao.aclassapp.service.CourseService;
import com.example.wanhao.aclassapp.util.RetrofitHelper;
import com.example.wanhao.aclassapp.util.SaveDataUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by wanhao on 2018/2/25.
 */

public class CourseListModel {
    private static final String TAG = "CourseListModel";

    private Context context;
    private CourseDao dao;

    public CourseListModel(Context context){
        this.context = context;
        dao = new CourseDao(context);
    }

    public void getListDataByDB(final IBaseRequestCallBack<List<Course>> callBack){
        List<Course> list = dao.alterAllCoursse(SaveDataUtil.getValueFromSharedPreferences(context,ApiConstant.COUNT));
        if(list==null || list.size()==0){
            getListDataByInternet(callBack);
            return;
        }
        callBack.requestSuccess(list);
    }

    @SuppressLint("CheckResult")
    public void getListDataByInternet(final IBaseRequestCallBack<List<Course>> callBack) {
        callBack.beforeRequest();
        CourseService service = RetrofitHelper.get(CourseService.class);
        service.getCourseList(SaveDataUtil.getValueFromSharedPreferences(context,ApiConstant.USER_TOKEN))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(responseBodyResponse -> {
                    String body = responseBodyResponse.body().string();
                    Log.i(TAG, "getListDataByInternet body : "+body);

                    HttpResult<List<Course>> result = new Gson().fromJson(body,new TypeToken<HttpResult<List<Course>>>(){}.getType());

                    if(result.getCode().equals(ApiConstant.RETURN_SUCCESS)){
                        List<Course> list = result.getData();
                        dao.deleteAllCourse(SaveDataUtil.getValueFromSharedPreferences(context,ApiConstant.COUNT));
                        dao.addCourseList(SaveDataUtil.getValueFromSharedPreferences(context,ApiConstant.COUNT),list);
                        callBack.requestSuccess(list);
                    }else{
                        callBack.requestError(new Throwable(result.getMessage()));
                    }
                }, throwable -> {
                    callBack.requestError(new Throwable("网络异常"));
                    Log.i(TAG, "accept: "+throwable);
                });
    }

}
