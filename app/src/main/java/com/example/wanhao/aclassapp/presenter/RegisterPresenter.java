package com.example.wanhao.aclassapp.presenter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

import com.example.wanhao.aclassapp.R;
import com.example.wanhao.aclassapp.bean.HttpResult;
import com.example.wanhao.aclassapp.bean.Role;
import com.example.wanhao.aclassapp.config.ApiConstant;
import com.example.wanhao.aclassapp.service.RegisterService;
import com.example.wanhao.aclassapp.util.GsonUtils;
import com.example.wanhao.aclassapp.util.ResourcesUtil;
import com.example.wanhao.aclassapp.util.RetrofitHelper;
import com.example.wanhao.aclassapp.view.IRegisterView;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by wanhao on 2017/11/22.
 */

public class RegisterPresenter {
    private static final String TAG = "RegisterPresenter";

    private IRegisterView iRegisterView;
    private Context mContext;

    public RegisterPresenter(IRegisterView loginView, Context context) {
        this.iRegisterView = loginView;
        this.mContext = context;
    }
    //获取手机验证码
    public void getVerificationCode(final String phoneNum, final TextView getCodeTv, final CountDownTimer timer) {
        if (TextUtils.isEmpty(phoneNum)) {
            iRegisterView.loadDataError("手机号不能为空");
            return;
        }
        getCodeTv.setClickable(false);
        timer.start();
    }

    //注册
    @SuppressLint("CheckResult")
    public void register(String phoneNum, String password, String code){
        if (TextUtils.isEmpty(phoneNum)){
            iRegisterView.loadDataError("手机号不能为空");
            return;
        }
        if (TextUtils.isEmpty(code)){
            iRegisterView.loadDataError("验证码不能为空");
            return;
        }
        if (TextUtils.isEmpty(password)){
            iRegisterView.loadDataError("密码不能为空");
            return;
        }
        //发送请求进行注册
        //首先检查验证码是否正确

        iRegisterView.showProgress();

        Map<String,String> map = new HashMap<>();
        map.put(ApiConstant.USER_NAME, phoneNum);
        map.put(ApiConstant.PASSWORD, password);
        map.put(ApiConstant.USER_ROLE, "student");

        RetrofitHelper.get(RegisterService.class).register(GsonUtils.toBody(map))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(responseBodyResponse -> {
                    String body = responseBodyResponse.body().string();
                    Log.i(TAG, "accept: "+body);

                    HttpResult<Role> result = new Gson().fromJson(body,HttpResult.class);

                    if(result.getCode().equals(ApiConstant.RETURN_SUCCESS)){
                        iRegisterView.loadDataSuccess("成功注册！");
                    }else{
                        iRegisterView.loadDataError(result.getMessage());
                    }
                    iRegisterView.dismissProgress();
                }, throwable -> {
                    Log.i(TAG, "accept: "+throwable);
                    iRegisterView.loadDataError(ResourcesUtil.getString(R.string.error_internet));
                    iRegisterView.dismissProgress();
                });

    }
}
