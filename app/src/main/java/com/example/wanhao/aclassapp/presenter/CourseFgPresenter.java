package com.example.wanhao.aclassapp.presenter;

import android.content.Context;

import com.example.wanhao.aclassapp.Model.CourseModel;
import com.example.wanhao.aclassapp.base.IBaseRequestCallBack;
import com.example.wanhao.aclassapp.bean.sqlbean.Course;
import com.example.wanhao.aclassapp.view.ICourseFgView;

import java.util.List;

/**
 * Created by wanhao on 2018/2/23.
 */

public class CourseFgPresenter {
    private static final String TAG = "CourseFgPresenter";

    private Context context;
    private ICourseFgView view;
    private CourseModel model;


    public CourseFgPresenter(Context context, ICourseFgView view){
        this.context = context;
        this.view = view;
        model = new CourseModel(context);
    }

    public void upDateList(){
        IBaseRequestCallBack<List<Course>> callBack = new IBaseRequestCallBack<List<Course>>() {
            @Override
            public void beforeRequest() {
                view.showProgress();
            }

            @Override
            public void requestError(Throwable throwable) {
                view.disimissProgress();
                view.loadDataError(throwable.toString());
            }

            @Override
            public void requestComplete() {
                view.disimissProgress();
            }

            @Override
            public void requestSuccess(List<Course> callBack) {
                view.disimissProgress();
                view.loadDataSuccess(callBack);
            }
        };

        model.getListDataByInternet(callBack);
    }

    public void getList(){
        IBaseRequestCallBack<List<Course>> callBack = new IBaseRequestCallBack<List<Course>>() {
            @Override
            public void beforeRequest() {
                view.showProgress();
            }

            @Override
            public void requestError(Throwable throwable) {
                view.disimissProgress();
                view.loadDataError(throwable.toString());
            }

            @Override
            public void requestComplete() {
                view.disimissProgress();
            }

            @Override
            public void requestSuccess(List<Course> callBack) {
                view.disimissProgress();
                view.loadDataSuccess(callBack);
            }
        };

        model.getListDataByDB(callBack);
    }

    public void delete(String coursID){
        IBaseRequestCallBack callBack = new IBaseRequestCallBack() {
            @Override
            public void beforeRequest() {

            }

            @Override
            public void requestError(Throwable throwable) {
                view.loadDataError(throwable.toString());
            }

            @Override
            public void requestComplete() {

            }

            @Override
            public void requestSuccess(Object callBack) {
                view.deleteSucess();
            }
        };
        model.deleteCourse(coursID,callBack);
    }

}
