package com.example.wanhao.aclassapp.service;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by wanhao on 2018/2/25.
 */

public interface UserMessageService {

    @GET("profile")
    Observable<Response<ResponseBody>> getProfile(@Header("Authorization") String token);

    @POST("profile")
    Observable<Response<ResponseBody>> postProfile(@Header("Authorization") String token, @Body RequestBody body);

    @GET("avatar")
    Observable<Response<ResponseBody>> getAvatar(@Header("Authorization") String token);

    @Multipart
    @POST("avatar")
    Observable<Response<ResponseBody>> uploadAvatar(@Header("Authorization") String token, @Part MultipartBody.Part part);
}
