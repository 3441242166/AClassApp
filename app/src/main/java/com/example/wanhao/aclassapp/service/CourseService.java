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

public interface CourseService {
    @GET("course")
    Observable<Response<ResponseBody>> getCourseList(@Header("Authorization") String token);

    @GET("course/{courseId}/delete")
    Observable<Response<ResponseBody>> deleteCourse(@Header("Authorization") String token, @Path("courseId") int courseId);

}
