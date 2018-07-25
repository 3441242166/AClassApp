package com.example.wanhao.aclassapp.Model;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import com.example.wanhao.aclassapp.R;
import com.example.wanhao.aclassapp.SQLite.ChatDao;
import com.example.wanhao.aclassapp.SQLite.CourseDao;
import com.example.wanhao.aclassapp.base.IBaseRequestCallBack;
import com.example.wanhao.aclassapp.bean.sqlbean.Course;
import com.example.wanhao.aclassapp.bean.sqlbean.CourseResult;
import com.example.wanhao.aclassapp.bean.requestbean.NoDataResponse;
import com.example.wanhao.aclassapp.config.ApiConstant;
import com.example.wanhao.aclassapp.service.CourseService;
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
 * Created by wanhao on 2018/2/25.
 */

public class CourseModel implements ICourseModel{
    private static final String TAG = "CourseModel";

    private Context context;
    private CourseDao dao;

    public CourseModel(Context context){
        this.context = context;
        dao = new CourseDao(context);
    }

    @Override
    public void getListDataByDB(final IBaseRequestCallBack<List<Course>> callBack){
        List<Course> list = dao.alterAllCoursse(SaveDataUtil.getValueFromSharedPreferences(context,ApiConstant.COUNT));
        if(list==null || list.size()==0){
            getListDataByInternet(callBack);
            return;
        }
        callBack.requestSuccess(list);
    }

    @Override
    public void getListDataByInternet(final IBaseRequestCallBack<List<Course>> callBack) {
        callBack.beforeRequest();
        CourseService service = RetrofitHelper.get(CourseService.class);
        service.getCourseList(SaveDataUtil.getValueFromSharedPreferences(context,ApiConstant.USER_TOKEN))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Response<ResponseBody>>() {
                    @Override
                    public void accept(Response<ResponseBody> responseBodyResponse) throws Exception {
                        //Log.i(TAG, "accept: "+responseBodyResponse.body().string());
                        CourseResult result = new Gson().fromJson(responseBodyResponse.body().string(),CourseResult.class);
                        if(result.getStatus().equals(ApiConstant.RETURN_SUCCESS)){
                            List<Course> list = result.getCourses();
                            dao.deleteAllCourse(SaveDataUtil.getValueFromSharedPreferences(context,ApiConstant.COUNT));
                            dao.addCourseList(SaveDataUtil.getValueFromSharedPreferences(context,ApiConstant.COUNT),list);
                            callBack.requestSuccess(list);
                        }else{
                            callBack.requestError(new Throwable("error"));
                        }
                    }
                },new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        callBack.requestError(throwable);
                    }
                });
    }

    @SuppressLint("CheckResult")
    @Override
    public void deleteCourse(final String id, final IBaseRequestCallBack callBack){
        callBack.beforeRequest();

        ChatDao chatDao = new ChatDao(context);
        chatDao.deleteAllChat(SaveDataUtil.getValueFromSharedPreferences(context,ApiConstant.COUNT),id);

        CourseService service = RetrofitHelper.get(CourseService.class);
        service.deleteCourse(SaveDataUtil.getValueFromSharedPreferences(context,ApiConstant.USER_TOKEN),Integer.valueOf(id))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Response<ResponseBody>>() {
                    @Override
                    public void accept(Response<ResponseBody> responseBodyResponse) throws Exception {
                        //Log.i(TAG, "deleteCourse: "+responseBodyResponse.body().string());
                        String result = responseBodyResponse.body().string();
                        Log.i(TAG, "accept: result = "+result);
                        NoDataResponse dataResponse = new Gson().fromJson(result,NoDataResponse.class);
                        if(dataResponse.getStatus().equals(ApiConstant.RETURN_SUCCESS)){
                            callBack.requestSuccess("");
                            dao.deleteCourse(SaveDataUtil.getValueFromSharedPreferences(context,ApiConstant.COUNT),id);
                        }else{
                            callBack.requestError(new Throwable(context.getResources().getString(R.string.error_internet)));
                        }
                    }
                },new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.i(TAG, "accept: throwable "+throwable);
                        callBack.requestError(throwable);
                    }
                });
    }
}

interface ICourseModel{
    void getListDataByInternet(IBaseRequestCallBack<List<Course>> callBack);

    void getListDataByDB(IBaseRequestCallBack<List<Course>> callBack);

    public void deleteCourse(String id,IBaseRequestCallBack callBack);
}