package com.example.wanhao.aclassapp.presenter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

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

import static com.example.wanhao.aclassapp.config.ApiConstant.RETURN_SUCCESS;

public class UserMessageFgPresenter {
    private static final String TAG = "UserMessageFgPresenter";

    Context context;
    IUserMessageFgView view;

    public UserMessageFgPresenter(Context context, IUserMessageFgView view){
        this.context = context;
        this.view = view;
    }

    @SuppressLint("CheckResult")
    public void init(){
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
                    }else{
//                        view.loadDataError(result.getMessage());
//                        view.tokenError("token失效，请重新登陆");
                    }
                    //view.dismissProgress();
                }, throwable -> {
//                    view.loadDataError("获取个人信息失败");
//                    view.dismissProgress();
                    Log.i(TAG, "accept: "+throwable);
                });

    }

}
