package com.example.wanhao.aclassapp.service;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

/**
 * Created by wanhao on 2018/3/19.
 */

public interface RemarkService {

    @GET("course/{courseId}/comment")
    Observable<Response<ResponseBody>> deleteCourse(@Header("Authorization") String token, @Path("courseId") int courseId);
}
