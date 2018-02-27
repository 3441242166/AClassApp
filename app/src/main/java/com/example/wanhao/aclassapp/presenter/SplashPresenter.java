package com.example.wanhao.aclassapp.presenter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.example.wanhao.aclassapp.bean.NoDataResponse;
import com.example.wanhao.aclassapp.config.ApiConstant;
import com.example.wanhao.aclassapp.service.TokenService;
import com.example.wanhao.aclassapp.util.MyDate;
import com.example.wanhao.aclassapp.util.RetrofitHelper;
import com.example.wanhao.aclassapp.util.SaveDataUtil;
import com.example.wanhao.aclassapp.view.ISplashView;
import com.google.gson.Gson;

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
        if(MyDate.differentDay(MyDate.getNowDateString(),time) > 4){
            TokenService service = RetrofitHelper.get(TokenService.class);
            service.check(token).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    Log.i(TAG, "onResponse: ");
                    NoDataResponse result = null;
                    try {
                        result = new Gson().fromJson(response.body().string(),NoDataResponse.class);
                    } catch (IOException e) {
                        view.goLoding();
                    }
                    if(result.getStatus().equals(ApiConstant.RETURN_SUCCESS)){
                        SaveDataUtil.saveToSharedPreferences(context, ApiConstant.TOKEN_TIME, MyDate.getNowDateString());
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
