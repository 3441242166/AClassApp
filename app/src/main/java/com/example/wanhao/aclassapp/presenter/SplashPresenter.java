package com.example.wanhao.aclassapp.presenter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.example.wanhao.aclassapp.R;
import com.example.wanhao.aclassapp.activity.LodingActivity;
import com.example.wanhao.aclassapp.activity.MainActivity;
import com.example.wanhao.aclassapp.bean.HttpResult;
import com.example.wanhao.aclassapp.config.ApiConstant;
import com.example.wanhao.aclassapp.service.TokenService;
import com.example.wanhao.aclassapp.util.DateUtil;
import com.example.wanhao.aclassapp.util.ResourcesUtil;
import com.example.wanhao.aclassapp.util.RetrofitHelper;
import com.example.wanhao.aclassapp.util.SaveDataUtil;
import com.example.wanhao.aclassapp.view.ISplashView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.wanhao.aclassapp.config.ApiConstant.RETURN_SUCCESS;

/**
 * Created by wanhao on 2018/2/24.
 */

public class SplashPresenter {
    private static final String TAG = "SplashPresenter";

    private Context context;
    private ISplashView view;

    public SplashPresenter(Context context,ISplashView view){
        this.context = context;
        this.view = view;
    }


    @SuppressLint("CheckResult")
    public void loding(){

        String token = SaveDataUtil.getValueFromSharedPreferences(context, ApiConstant.USER_TOKEN);
        String time = SaveDataUtil.getValueFromSharedPreferences(context,ApiConstant.TOKEN_TIME);
        //  token 为空
        if(TextUtils.isEmpty(token)||TextUtils.isEmpty(time)){
            context.startActivity(new Intent(context,LodingActivity.class));
            view.finishActivity();
            return;
        }
        context.startActivity(new Intent(context,MainActivity.class));

        view.finishActivity();
    }

}
