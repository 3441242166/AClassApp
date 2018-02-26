package com.example.wanhao.aclassapp.presenter;

import android.content.Context;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.widget.TextView;

import com.example.wanhao.aclassapp.view.IRegisterView;


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
            //iRegisterView.loadDataError(mContext.getString(R.string.str_phone_num_cannot_empty));
            return;
        }
        getCodeTv.setClickable(false);
        timer.start();
    }

    //注册
    public void register(String phoneNum, String password, String code){
        if (TextUtils.isEmpty(phoneNum)){
            //iRegisterView.loadDataError(mContext.getString(R.string.str_phone_num_cannot_empty));
            return;
        }
        if (TextUtils.isEmpty(code)){
            iRegisterView.loadDataError("");
            return;
        }
        if (TextUtils.isEmpty(password)){
            //iRegisterView.loadDataError(mContext.getString(R.string.str_password_cannot_empty));
            return;
        }
        //发送请求进行注册
        //首先检查验证码是否正确

    }
}
