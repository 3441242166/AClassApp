package com.example.wanhao.aclassapp.presenter;

import android.content.Context;
import android.content.Intent;

import com.example.wanhao.aclassapp.Model.CourseListModel;
import com.example.wanhao.aclassapp.backService.CourseService;
import com.example.wanhao.aclassapp.backService.DownDocumentService;
import com.example.wanhao.aclassapp.base.IBaseRequestCallBack;
import com.example.wanhao.aclassapp.bean.ChatBean;
import com.example.wanhao.aclassapp.bean.Course;
import com.example.wanhao.aclassapp.config.ApiConstant;
import com.example.wanhao.aclassapp.view.ICourseFgView;

import java.util.List;

/**
 * Created by wanhao on 2018/2/23.
 */

public class CourseListPresenter {
    private static final String TAG = "CourseListPresenter";

    private Context context;
    private ICourseFgView view;
    private CourseListModel model;


    public CourseListPresenter(Context context, ICourseFgView view){
        this.context = context;
        this.view = view;
        model = new CourseListModel(context);
    }

    public void upDataList(boolean isDB){
        IBaseRequestCallBack<List<Course>> callBack = new IBaseRequestCallBack<List<Course>>() {
            @Override
            public void beforeRequest() {
                view.showProgress();
            }

            @Override
            public void requestError(Throwable throwable) {
                view.dismissProgress();
                view.loadDataError(throwable.toString());
            }

            @Override
            public void requestComplete() {
                view.dismissProgress();
            }

            @Override
            public void requestSuccess(List<Course> callBack) {
                for(Course course :callBack){
                    Intent intent = new Intent(context,CourseService.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra(ApiConstant.COURSE_ID,course.getId());
                    context.startService(intent);
                }

                view.dismissProgress();
                view.loadDataSuccess(callBack);
            }
        };

        if(isDB)
            model.getListDataByDB(callBack);
        else
            model.getListDataByInternet(callBack);
    }

}
