package com.example.wanhao.aclassapp.service;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * Created by wanhao on 2018/2/25.
 */

public interface UserMessageService {

    @GET("profile")
    Observable<Response<ResponseBody>> getProfile(@Header("Authorization") String token);

    @POST("profile")
    Observable<Response<ResponseBody>> postProfile(@Header("Authorization") String token, @Body RequestBody body);

}
