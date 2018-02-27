package com.example.wanhao.aclassapp.service;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

/**
 * Created by wanhao on 2018/2/27.
 */

public interface TokenService {
    @GET("check")
    Call<ResponseBody> check(@Header("Authorization") String token);
}
