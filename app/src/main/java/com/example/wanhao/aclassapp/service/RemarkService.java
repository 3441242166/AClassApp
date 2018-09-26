package com.example.wanhao.aclassapp.service;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by wanhao on 2018/3/19.
 */

public interface RemarkService {

    @GET("course/{courseID}/comment")
    Observable<Response<ResponseBody>> getRemark(@Header("Authorization") String token, @Path("courseID") String courseId);

    @POST("auth/course/{courseID}/comment")
    Observable<Response<ResponseBody>> sendRemark(@Header("Authorization") String token, @Path("courseID") String courseId, @Body RequestBody body);
}
