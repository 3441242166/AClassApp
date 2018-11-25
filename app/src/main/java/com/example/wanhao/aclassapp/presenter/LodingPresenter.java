package com.example.wanhao.aclassapp.presenter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.example.wanhao.aclassapp.R;
import com.example.wanhao.aclassapp.base.IBasePresenter;
import com.example.wanhao.aclassapp.bean.HttpResult;
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

public class LodingPresenter implements IBasePresenter {
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
            iLoginView.errorMessage("账号不能为空");
            return;
        }
        if (TextUtils.isEmpty(password)){
            iLoginView.errorMessage("密码不能为空");
            return;
        }
        //开始向服务器请求
        iLoginView.showProgress();

        Map<String,String> map = new HashMap<>();
        map.put("user_name", phoneNum);
        map.put("password", password);

        RetrofitHelper.get(LodingService.class).login(GsonUtils.toBody(map))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(responseBodyResponse -> {
                    String body = responseBodyResponse.body().string();
                    Log.i(TAG, "accept: "+body);

                    HttpResult<lodingResult> result = new Gson().fromJson(body,new TypeToken<HttpResult<lodingResult>>(){}.getType());

                    if(result.getCode().equals(RETURN_SUCCESS)){
                        lodingResult role = result.getData();
                        SaveDataUtil.saveToSharedPreferences(mContext, ApiConstant.USER_TOKEN, role.token);
                        SaveDataUtil.saveToSharedPreferences(mContext, ApiConstant.USER_ROLE, role.user);
                        SaveDataUtil.saveToSharedPreferences(mContext, ApiConstant.USER_COUNT, phoneNum);
                        SaveDataUtil.saveToSharedPreferences(mContext, ApiConstant.USER_PASSWORD, password);
                        SaveDataUtil.saveToSharedPreferences(mContext, ApiConstant.TOKEN_TIME, DateUtil.getNowDateString());
                        iLoginView.loadDataSuccess("登陆成功");
                    }else{
                        iLoginView.errorMessage(result.getMessage());
                    }
                    iLoginView.dismissProgress();
                }, throwable -> {
                    Log.i(TAG, "accept: "+throwable.toString());
                    iLoginView.dismissProgress();
                    iLoginView.errorMessage(ResourcesUtil.getString(R.string.error_internet));
                });
    }

    public void init() {
        String count = SaveDataUtil.getValueFromSharedPreferences(mContext,ApiConstant.USER_COUNT);
        String password = SaveDataUtil.getValueFromSharedPreferences(mContext,ApiConstant.USER_PASSWORD);
        iLoginView.initData(count,password);
    }

    @Override
    public void destroy() {

    }

    class lodingResult{
        public String user;
        public String token;
    }
}
