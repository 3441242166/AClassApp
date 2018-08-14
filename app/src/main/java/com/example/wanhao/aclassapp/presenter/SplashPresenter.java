package com.example.wanhao.aclassapp.presenter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.example.wanhao.aclassapp.bean.HttpResult;
import com.example.wanhao.aclassapp.config.ApiConstant;
import com.example.wanhao.aclassapp.service.TokenService;
import com.example.wanhao.aclassapp.util.DateUtil;
import com.example.wanhao.aclassapp.util.RetrofitHelper;
import com.example.wanhao.aclassapp.util.SaveDataUtil;
import com.example.wanhao.aclassapp.view.ISplashView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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


    public void loding(){

        String token = SaveDataUtil.getValueFromSharedPreferences(context, ApiConstant.USER_TOKEN);
        String time = SaveDataUtil.getValueFromSharedPreferences(context,ApiConstant.TOKEN_TIME);
        //  token 为空
        if(TextUtils.isEmpty(token)||TextUtils.isEmpty(time)){
            view.goLoding();
            return;
        }
        //   token离线时间5天以上 从新验证
        if(DateUtil.differentDay(DateUtil.getNowDateString(),time) > 4){
            TokenService service = RetrofitHelper.get(TokenService.class);
            service.check(token).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    String body = null;
                    try {
                        body = response.body().string();
                    } catch (IOException e) {
                        e.printStackTrace();
                        view.goLoding();
                    }
                    Log.i(TAG, "accept: "+body);

                    HttpResult<String> result = new Gson().fromJson(body,new TypeToken<HttpResult<String>>(){}.getType());

                    if(result.getCode().equals(ApiConstant.RETURN_SUCCESS)){
                        SaveDataUtil.saveToSharedPreferences(context, ApiConstant.TOKEN_TIME, DateUtil.getNowDateString());
                        view.goCourse();
                    }else{
                        view.goLoding();
                    }
                }
                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    view.goLoding();
                }
            });
        }

        view.goCourse();
    }

}
