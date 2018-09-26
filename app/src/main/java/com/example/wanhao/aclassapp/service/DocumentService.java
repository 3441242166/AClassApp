package com.example.wanhao.aclassapp.service;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Streaming;

/**
 * Created by wanhao on 2018/3/18.
 */

public interface DocumentService {

    @GET("course/{courseId}/all")
    Observable<Response<ResponseBody>> getDocumentList(@Header("Authorization") String token, @Path("courseId") String courseId);

    @GET("course/{courseId}/data/preview")
    Observable<Response<ResponseBody>> getPreviewList(@Header("Authorization") String token, @Path("courseId") String courseId);

    @Streaming
    @GET("course/{courseId}/data/edata/{eDataID}")
    Observable<Response<ResponseBody>> downloadDocument(@Header("Authorization") String token, @Path("courseId") String courseId, @Path("eDataID") int eDataID);

    @Streaming
    @GET("course/{courseId}/data/preview/{previewID}")
    Observable<Response<ResponseBody>> downloadPreview(@Header("Authorization") String token, @Path("courseId") String courseId, @Path("previewID") int previewID);

}
