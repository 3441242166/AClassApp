package com.example.wanhao.aclassapp.presenter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.example.wanhao.aclassapp.R;
import com.example.wanhao.aclassapp.bean.HttpResult;
import com.example.wanhao.aclassapp.bean.Role;
import com.example.wanhao.aclassapp.config.ApiConstant;
import com.example.wanhao.aclassapp.service.LodingService;
import com.example.wanhao.aclassapp.util.DateUtil;
import com.example.wanhao.aclassapp.util.GsonUtils;
import com.example.wanhao.aclassapp.util.ResourcesUtil;
import com.example.wanhao.aclassapp.util.RetrofitHelper;
import com.example.wanhao.aclassapp.util.SaveDataUtil;
import com.example.wanhao.aclassapp.view.ILodingView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.example.wanhao.aclassapp.config.ApiConstant.RETURN_SUCCESS;


/**
 * Created by wanhao on 2017/11/22.
 */

public class LodingPresenter{
    private static final String TAG = "LodingPresenter";

    private ILodingView iLoginView;
    private Context mContext;

    public LodingPresenter(ILodingView loginView, Context context) {
        this.iLoginView = loginView;
        this.mContext = context;
    }

    @SuppressLint("CheckResult")
    public void login(final String phoneNum, final String password) {
        if (TextUtils.isEmpty(phoneNum)){
            iLoginView.loadDataError("账号不能为空");
            return;
        }
        if (TextUtils.isEmpty(password)){
            iLoginView.loadDataError("密码不能为空");
            return;
        }
        //开始向服务器请求
        iLoginView.showProgress();

        Map<String,String> map = new HashMap<>();
        map.put("username", phoneNum);
        map.put("password", password);

        RetrofitHelper.get(LodingService.class).login(GsonUtils.toBody(map))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(responseBodyResponse -> {
                    String body = responseBodyResponse.body().string();
                    Log.i(TAG, "accept: "+body);

                    HttpResult<Role> result = new Gson().fromJson(body,new TypeToken<HttpResult<Role>>(){}.getType());

                    if(result.getCode().equals(RETURN_SUCCESS)){
                        Role role = result.getData();
                        SaveDataUtil.saveToSharedPreferences(mContext, ApiConstant.USER_TOKEN, role.getToken());
                        SaveDataUtil.saveToSharedPreferences(mContext, ApiConstant.USER_ROLE, role.getRole());
                        SaveDataUtil.saveToSharedPreferences(mContext, ApiConstant.COUNT, phoneNum);
                        SaveDataUtil.saveToSharedPreferences(mContext, ApiConstant.PASSWORD, password);
                        SaveDataUtil.saveToSharedPreferences(mContext, ApiConstant.TOKEN_TIME, DateUtil.getNowDateString());
                        iLoginView.loadDataSuccess("登陆成功");
                    }else{
                        iLoginView.loadDataError(result.getMessage());
                    }
                    iLoginView.disimissProgress();
                }, throwable -> {
                    Log.i(TAG, "accept: "+throwable.toString());
                    iLoginView.disimissProgress();
                    iLoginView.loadDataError(ResourcesUtil.getString(R.string.error_internet));
                });
    }

    public void init() {
        String count = SaveDataUtil.getValueFromSharedPreferences(mContext,ApiConstant.COUNT);
        String password = SaveDataUtil.getValueFromSharedPreferences(mContext,ApiConstant.PASSWORD);
        iLoginView.initData(count,password);
    }

}
