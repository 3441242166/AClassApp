package com.example.wanhao.aclassapp.service;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface HomeworkService {
    @GET("course/{courseId}/quiz")
    Observable<Response<ResponseBody>> getHomeworkList(@Header("Authorization") String token, @Path("courseId") String courseId);

    @GET("course/{courseId}/quiz/{quizId}")
    Observable<Response<ResponseBody>> postAnswer(@Header("Authorization") String token, @Path("courseId") int courseId,@Path("courseId") int quizId, @Body RequestBody body);


}
