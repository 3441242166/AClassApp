package com.example.wanhao.aclassapp.service;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by wanhao on 2018/2/24.
 */

public interface CourseService {
    @POST("auth/course")
    Observable<Response<ResponseBody>> addCourse(@Header("Authorization") String token, @Body RequestBody body);
    @GET("auth/courses")
    Observable<Response<ResponseBody>> getCourseList(@Header("Authorization") String token);
    @GET("course/{courseId}/delete")
    Observable<Response<ResponseBody>> deleteCourse(@Header("Authorization") String token, @Path("courseId") int courseId);

}
