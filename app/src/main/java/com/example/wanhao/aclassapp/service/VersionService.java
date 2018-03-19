package com.example.wanhao.aclassapp.service;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.GET;

/**
 * Created by wanhao on 2018/3/18.
 */

public interface VersionService {

    @GET("version")
    Observable<Response<ResponseBody>> checkVersion();

}
