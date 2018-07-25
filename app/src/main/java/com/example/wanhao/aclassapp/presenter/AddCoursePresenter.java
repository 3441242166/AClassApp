package com.example.wanhao.aclassapp.presenter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.example.wanhao.aclassapp.R;
import com.example.wanhao.aclassapp.config.ApiConstant;
import com.example.wanhao.aclassapp.service.AddCourseService;
import com.example.wanhao.aclassapp.util.ResourcesUtil;
import com.example.wanhao.aclassapp.util.RetrofitHelper;
import com.example.wanhao.aclassapp.util.SaveDataUtil;
import com.example.wanhao.aclassapp.view.IAddCourseView;

import org.json.JSONObject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;

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
                .subscribe(new io.reactivex.functions.Consumer<Response<ResponseBody>>() {
                    @Override
                    public void accept(Response<ResponseBody> responseBodyResponse) throws Exception {
                        if(responseBodyResponse.isSuccessful()){
                            JSONObject jsonObject = new JSONObject(responseBodyResponse.body().string());
                            Log.i(TAG, "accept: "+responseBodyResponse.body().string());
                            String status = jsonObject.optString("status");
                            if (status.equals("SUCCESS")) {
                                view.loadDataSuccess("添加成功");
                            } else {
                                view.loadDataError(ResourcesUtil.getString(R.string.error_courseid));
                            }
                        }else{
                            view.loadDataError(ResourcesUtil.getString(R.string.error_internet));
                        }
                        view.disimissProgress();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        view.disimissProgress();
                        view.loadDataError(ResourcesUtil.getString(R.string.error_internet));
                    }
                });

    }
}

