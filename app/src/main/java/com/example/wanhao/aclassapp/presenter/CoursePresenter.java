package com.example.wanhao.aclassapp.presenter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.example.wanhao.aclassapp.bean.requestbean.User;
import com.example.wanhao.aclassapp.config.ApiConstant;
import com.example.wanhao.aclassapp.service.UserMessageService;
import com.example.wanhao.aclassapp.util.FileConvertUtil;
import com.example.wanhao.aclassapp.util.RetrofitHelper;
import com.example.wanhao.aclassapp.util.SaveDataUtil;
import com.example.wanhao.aclassapp.view.ICourseView;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;

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

    public void getHeadImage(){
        UserMessageService service = RetrofitHelper.get(UserMessageService.class);

        service.getAvatar(SaveDataUtil.getValueFromSharedPreferences(mContext,ApiConstant.USER_TOKEN))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new io.reactivex.functions.Consumer<Response<ResponseBody>>() {
                    @Override
                    public void accept(Response<ResponseBody> responseBodyResponse) throws Exception {
                        Bitmap bitmap = BitmapFactory.decodeStream(responseBodyResponse.body().byteStream());
                        FileConvertUtil.saveBitmapToLocal(ApiConstant.USER_AVATAR_NAME,bitmap);
                        iCourseView.setHead(bitmap);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        iCourseView.setHead(FileConvertUtil.getBitmapFromLocal(ApiConstant.USER_AVATAR_NAME));
                        Log.i(TAG, "accept: "+throwable);
                    }
                });
    }

    public void getUserMessage(){
        UserMessageService service = RetrofitHelper.get(UserMessageService.class);

        service.getProfile(SaveDataUtil.getValueFromSharedPreferences(mContext, ApiConstant.USER_TOKEN))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new io.reactivex.functions.Consumer<Response<ResponseBody>>() {
                    @Override
                    public void accept(Response<ResponseBody> responseBodyResponse) throws Exception {
                        String body = responseBodyResponse.body().string();
                        Log.i(TAG, "accept: "+body);
                        JsonObject obj = new JsonParser().parse(body).getAsJsonObject();
                        if(obj.get("status")==null){
                            User result = new Gson().fromJson(body,User.class);
                            iCourseView.setName(result.getNickName());
                        }else if(obj.get("status").getAsString().equals(ApiConstant.RETURN_ERROR)){
                            Log.i(TAG, "accept: token error");
                            iCourseView.tokenError();
                            return;
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        iCourseView.setName(SaveDataUtil.getValueFromSharedPreferences(mContext, ApiConstant.USER_NAME));
                    }
                });

    }
}
