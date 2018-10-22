package com.example.wanhao.aclassapp.service;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

/**
 * Created by wanhao on 2018/2/24.
 */

public interface AddCourseService {
    @GET("auth/course/{courseId}/join")
    Observable<Response<ResponseBody>> addCourse(@Header("Authorization") String token, @Path("courseId") int courseId);
}
