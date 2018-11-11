package com.example.wanhao.aclassapp.presenter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.wanhao.aclassapp.backService.CourseService;
import com.example.wanhao.aclassapp.bean.Course;
import com.example.wanhao.aclassapp.bean.HttpResult;
import com.example.wanhao.aclassapp.config.ApiConstant;
import com.example.wanhao.aclassapp.config.Constant;
import com.example.wanhao.aclassapp.db.ChatDB;
import com.example.wanhao.aclassapp.db.CourseDB;
import com.example.wanhao.aclassapp.util.RetrofitHelper;
import com.example.wanhao.aclassapp.util.SaveDataUtil;
import com.example.wanhao.aclassapp.view.ICourseFgView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.LinkedList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;

/**
 * Created by wanhao on 2018/2/23.
 */

public class CourseListPresenter {
    private static final String TAG = "CourseListPresenter";

    private Context context;
    private ICourseFgView view;
    private Realm realm;

    public CourseListPresenter(Context context, ICourseFgView view){
        this.context = context;
        this.view = view;
        realm = Realm.getDefaultInstance();
        EventBus.getDefault().register(this);
    }

    public void getListDataByDB(){
        realm.beginTransaction();

        String count = SaveDataUtil.getValueFromSharedPreferences(context,ApiConstant.USER_COUNT);
        List<CourseDB> list = realm.where(CourseDB.class)
                .equalTo("userCount",count)
                .findAll();

        realm.commitTransaction();

        // 如果本地数据库为空 则从网络获取数据
        if(list == null || list.size() == 0){
            getListDataByInternet();
            return;
        }

        startService(list);
        view.loadDataSuccess(list);
    }

    @SuppressLint("CheckResult")
    public void getListDataByInternet() {

        String token = SaveDataUtil.getValueFromSharedPreferences(context,ApiConstant.USER_TOKEN);
        com.example.wanhao.aclassapp.service.CourseService service = RetrofitHelper.get(com.example.wanhao.aclassapp.service.CourseService.class);
        service.getCourseList(token)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(responseBodyResponse -> {
                    String body = responseBodyResponse.body().string();
                    Log.i(TAG, "getListDataByInternet body : "+body);

                    HttpResult<CourseListData> result = new Gson().fromJson(body,new TypeToken<HttpResult<CourseListData>>(){}.getType());

                    if(result.getCode().equals(ApiConstant.RETURN_SUCCESS)){
                        List<Course> list = result.getData().courses;
                        List<CourseDB> data = saveData(list);
                        startService(data);
                        view.loadDataSuccess(data);
                    }else{
                        view.loadDataError(result.getMessage());
                    }
                }, throwable -> {
                    view.loadDataError("网络异常");
                    Log.i(TAG, "accept: "+throwable);
                });
    }

    private void startService(List<CourseDB> list){
        for(CourseDB course :list){
            Intent intent = new Intent(context, CourseService.class);
            //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(ApiConstant.IM_ACTION ,ApiConstant.MESSAGE_CONNECT);
            intent.putExtra(ApiConstant.COURSE_ID,course.getCourseID());
            context.startService(intent);
        }
    }

    private List<CourseDB> saveData(List<Course> list){
        List<CourseDB> data = new LinkedList<>();
        for(Course course:list){
            data.add(new CourseDB(course));
        }

        realm.beginTransaction();
        realm.copyToRealmOrUpdate(data);
        realm.commitTransaction();

        return data;
    }

    @Subscribe(priority = 888)
    public void handleIMMessage(ChatDB bean) {
        Log.i(Constant.TAG_EVENTBUS, "ListPresenter: content = "+bean.getContent());
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();

        CourseDB course = realm.where(CourseDB.class)
                .equalTo("courseID",bean.getCourseID())
                .findFirst();
        course.setUnRead(course.getUnRead()+1);

        realm.commitTransaction();
    }

    static class CourseListData {
        List<Course> courses;
    }
}
