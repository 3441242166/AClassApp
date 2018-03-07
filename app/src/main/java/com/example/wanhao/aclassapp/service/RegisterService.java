package com.example.wanhao.aclassapp.service;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by szh on 2017/5/13.
 */

public interface RegisterService {
//    @FormUrlEncoded
//    @POST("user/checkphoneisused")
//    Call<HttpResult<String>> checkPhoneIsUsed(@Field(ApiConstant.TEL_NUM) String telNum);
//
//    //TODO 短信验证码
//    @FormUrlEncoded
//    @POST("sendcode.action")
//    Call<SMSVerifyResponse> getVerifyCode(@HeaderMap Map<String, String> headers,
//                                          @Field(ApiConstant.MOBILE) String mobile,
//                                          @Field(ApiConstant.TEMPLATE_ID) String templateId);
//
//    @FormUrlEncoded
//    @POST("verifycode.action")
//    Call<ResponseBody> checkVerifyCode(@HeaderMap Map<String, String> headers,
//                                       @Field(ApiConstant.MOBILE) String mobile,
//                                       @Field(ApiConstant.CODE) String code);
//

    @POST("register")
    Observable<Response<ResponseBody>> register(@Body RequestBody body);
}
