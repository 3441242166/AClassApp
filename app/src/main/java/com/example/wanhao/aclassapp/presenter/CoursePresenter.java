package com.example.wanhao.aclassapp.presenter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.example.wanhao.aclassapp.bean.HttpResult;
import com.example.wanhao.aclassapp.bean.User;
import com.example.wanhao.aclassapp.config.ApiConstant;
import com.example.wanhao.aclassapp.service.UserMessageService;
import com.example.wanhao.aclassapp.util.RetrofitHelper;
import com.example.wanhao.aclassapp.util.SaveDataUtil;
import com.example.wanhao.aclassapp.view.ICourseView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.example.wanhao.aclassapp.config.ApiConstant.BASE_URL;
import static com.example.wanhao.aclassapp.config.ApiConstant.RETURN_SUCCESS;

/**
 * Created by wanhao on 2018/3/5.
 */

public class CoursePresenter {
    private static final String TAG = "CoursePresenter";

    private ICourseView iCourseView;
    private Context mContext;

    public CoursePresenter(ICourseView iCourseView, Context context) {
        this.iCourseView = iCourseView;
        this.mContext = context;
    }

    public void getData(){
        getHeadImage();
        getUserMessage();
    }

    private void getHeadImage(){

        GlideUrl cookie = new GlideUrl(BASE_URL+"avatar"
                , new LazyHeaders.Builder().addHeader("Authorization", SaveDataUtil.getValueFromSharedPreferences(mContext,ApiConstant.USER_TOKEN)).build());
        Glide.with(mContext)
                .load(cookie)
                .asBitmap()
                .into(target);
    }

    private SimpleTarget<Bitmap> target = new SimpleTarget<Bitmap>(50,50) {
        @Override
        public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
            iCourseView.setHead(resource);
        }
    };

    @SuppressLint("CheckResult")
    private void getUserMessage(){
        UserMessageService service = RetrofitHelper.get(UserMessageService.class);

        service.getProfile(SaveDataUtil.getValueFromSharedPreferences(mContext, ApiConstant.USER_TOKEN))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(responseBodyResponse -> {
                    String body = responseBodyResponse.body().string();
                    Log.i(TAG, "accept: "+body);

                    HttpResult<User> result = new Gson().fromJson(body,new TypeToken<HttpResult<User>>(){}.getType());

                    if(result.getCode().equals(RETURN_SUCCESS)){
                        iCourseView.setName(result.getData().getNickName());
                    }else{
                        iCourseView.tokenError();
                    }

//                    JsonObject obj = new JsonParser().parse(body).getAsJsonObject();
//                    if(obj.get("status")==null){
//                        User result = new Gson().fromJson(body,User.class);
//                        iCourseView.setName(result.getNickName());
//                    }else if(obj.get("status").getAsString().equals(ApiConstant.RETURN_ERROR)){
//                        Log.i(TAG, "accept: token error");
//                        iCourseView.tokenError();
//                        return;
//                    }

                }, throwable -> {
                    iCourseView.setName(SaveDataUtil.getValueFromSharedPreferences(mContext, ApiConstant.USER_NAME));
                    Log.i(TAG, "getUserMessage: "+throwable);
                });

    }
}
