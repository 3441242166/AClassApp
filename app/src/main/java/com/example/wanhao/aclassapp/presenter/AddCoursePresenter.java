package com.example.wanhao.aclassapp.presenter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.example.wanhao.aclassapp.R;
import com.example.wanhao.aclassapp.bean.HttpResult;
import com.example.wanhao.aclassapp.config.ApiConstant;
import com.example.wanhao.aclassapp.service.AddCourseService;
import com.example.wanhao.aclassapp.util.ResourcesUtil;
import com.example.wanhao.aclassapp.util.RetrofitHelper;
import com.example.wanhao.aclassapp.util.SaveDataUtil;
import com.example.wanhao.aclassapp.view.IAddCourseView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.example.wanhao.aclassapp.config.ApiConstant.RETURN_SUCCESS;

/**
 * Created by wanhao on 2018/2/24.
 */

public class AddCoursePresenter{
    private static final String TAG = "AddCoursePresenter";

    private Context context;
    private IAddCourseView view;

    public AddCoursePresenter(Context context, IAddCourseView view){
        this.context = context;
        this.view = view;
    }

    @SuppressLint("CheckResult")
    public void add(String code) {

        if (TextUtils.isEmpty(code)) {
            view.loadDataError(ResourcesUtil.getString(R.string.error_courseid_empty));
            return;
        }
        view.showProgress();

        AddCourseService service = RetrofitHelper.get(AddCourseService.class);

        service.addCourse(SaveDataUtil.getValueFromSharedPreferences(context,ApiConstant.USER_TOKEN),Integer.parseInt(code))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(responseBodyResponse -> {
                    String body = responseBodyResponse.body().string();
                    Log.i(TAG, "accept: "+body);

                    HttpResult<String> result = new Gson().fromJson(body,new TypeToken<HttpResult<String>>(){}.getType());

                    if(result.getCode().equals(RETURN_SUCCESS)){
                        view.loadDataSuccess(result.getMessage());
                    }else{
                        view.loadDataError(result.getMessage());
                    }
                    view.dismissProgress();
                }, throwable -> {
                    view.loadDataError(ResourcesUtil.getString(R.string.error_internet));
                    Log.i(TAG, "accept: "+throwable);
                    view.dismissProgress();

                });
    }
}

