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

public interface HomeworkService {
    @GET("homeworks/{courseId}")
    Observable<Response<ResponseBody>> getHomeworkList(@Header("Authorization") String token, @Path("courseId") String courseId);

    @GET("homework/{courseId}/{quizId}")
    Observable<Response<ResponseBody>> getQuestionList(@Header("Authorization") String token, @Path("courseId") String courseId);

//    @POST("course/{courseId}/{quizId}")
//    Observable<Response<ResponseBody>> postAnswer(@Header("Authorization") String token, @Path("courseId") String courseId,@Path("quizId") String quizId, @Body RequestBody body);
}
