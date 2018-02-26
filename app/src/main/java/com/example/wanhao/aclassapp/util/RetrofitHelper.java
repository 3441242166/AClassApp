package com.example.wanhao.aclassapp.util;


import com.example.wanhao.aclassapp.config.ApiConstant;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by wanhao on 2017/11/7.
 */

public class RetrofitHelper {
    private static final String TAG = "RetrofitHelper";
    public static Retrofit retrofit = null;

    /**
     * 得到service实例
     *
     * @param baseUrl
     * @param tClass
     * @param <T>
     * @return
     */
    public static <T> T getHttpService(String baseUrl, Class<T> tClass) {
        //公共请求头
        Interceptor headerInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request originalRequest = chain.request();
                Request.Builder requestBuilder = originalRequest.newBuilder();
                Request request = requestBuilder
                        .build();
                return chain.proceed(request);
            }
        };
        //日志拦截器
//        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
//        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        //设置头
        builder.retryOnConnectionFailure(true) //自动重连
                .connectTimeout(10, TimeUnit.SECONDS)
                //.addNetworkInterceptor(logging)
                .addInterceptor(headerInterceptor);
        OkHttpClient okHttpClient = builder.build();

        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(okHttpClient)
                //.addConverterFactory(CustomGsonConverterFactory.create())
                .build();
        return retrofit.create(tClass);
    }

    public static <T> T get(Class<T> tClass) {
        OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
        builder.retryOnConnectionFailure(true)
                .retryOnConnectionFailure(true)
                .connectTimeout(5, TimeUnit.SECONDS);

        return new Retrofit.Builder().baseUrl(ApiConstant.BASE_URL)
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(tClass);
    }
}
