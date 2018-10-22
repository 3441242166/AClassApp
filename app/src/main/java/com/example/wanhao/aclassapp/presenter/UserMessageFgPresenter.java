package com.example.wanhao.aclassapp.presenter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.example.wanhao.aclassapp.bean.HttpResult;
import com.example.wanhao.aclassapp.bean.User;
import com.example.wanhao.aclassapp.config.ApiConstant;
import com.example.wanhao.aclassapp.service.UserMessageService;
import com.example.wanhao.aclassapp.util.RetrofitHelper;
import com.example.wanhao.aclassapp.util.SaveDataUtil;
import com.example.wanhao.aclassapp.view.IUserMessageFgView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.example.wanhao.aclassapp.config.ApiConstant.BASE_URL;
import static com.example.wanhao.aclassapp.config.ApiConstant.HEAD_URL;
import static com.example.wanhao.aclassapp.config.ApiConstant.RETURN_SUCCESS;

public class UserMessageFgPresenter {
    private static final String TAG = "UserMessageFgPresenter";

    Context context;
    IUserMessageFgView view;

    public UserMessageFgPresenter(Context context, IUserMessageFgView view){
        this.context = context;
        this.view = view;
        init();
    }

    @SuppressLint("CheckResult")
    public void init(){
        User user = new User();
        String name = SaveDataUtil.getValueFromSharedPreferences(context,ApiConstant.USER_NAME);
        String signature = SaveDataUtil.getValueFromSharedPreferences(context,ApiConstant.USER_SIGNATURE);

        if(TextUtils.isEmpty(name)){
            user.setNickName("无名氏");
        }else {
            user.setNickName(SaveDataUtil.getValueFromSharedPreferences(context,ApiConstant.USER_NAME));
        }
        if(TextUtils.isEmpty(signature)){
            user.setSignature("你若安好 便是晴天");
        }else {
            user.setSignature(SaveDataUtil.getValueFromSharedPreferences(context,ApiConstant.USER_SIGNATURE));
        }

        view.setUserMessage(user);

        UserMessageService service  = RetrofitHelper.get(UserMessageService.class);
        service.getProfile(SaveDataUtil.getValueFromSharedPreferences(context, ApiConstant.USER_TOKEN))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(responseBodyResponse -> {
                    String body = responseBodyResponse.body().string();
                    Log.i(TAG, "accept: "+body);

                    HttpResult<User> result = new Gson().fromJson(body,new TypeToken<HttpResult<User>>(){}.getType());
                    if(result.getCode().equals(RETURN_SUCCESS)){
                        view.setUserMessage(result.getData());
                        SaveDataUtil.saveToSharedPreferences(context,ApiConstant.USER_NAME,result.getData().getNickName());
                        SaveDataUtil.saveToSharedPreferences(context,ApiConstant.USER_SIGNATURE,result.getData().getSignature());
                    }else{

                    }
                }, throwable -> {
                    Log.i(TAG, "accept: "+throwable);
                });

        getHeadImage();
    }

    @SuppressLint("CheckResult")
    public void getHeadImage(){
        GlideUrl cookie = new GlideUrl(HEAD_URL
                , new LazyHeaders.Builder().addHeader("Authorization", SaveDataUtil.getValueFromSharedPreferences(context,ApiConstant.USER_TOKEN)).build());
        view.setUserHead(cookie);

    }


}
