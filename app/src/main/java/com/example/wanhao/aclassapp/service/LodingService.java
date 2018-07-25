package com.example.wanhao.aclassapp.service;


import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by wanhao on 2017/12/6.
 */

public interface LodingService {

    @POST("login")
    Observable<Response<ResponseBody>> login(@Body RequestBody body);

}
