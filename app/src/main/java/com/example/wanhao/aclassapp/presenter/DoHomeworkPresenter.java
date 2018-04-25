package com.example.wanhao.aclassapp.presenter;

import android.content.Context;
import android.util.Log;

import com.example.wanhao.aclassapp.bean.HomeworkResponse;
import com.example.wanhao.aclassapp.bean.User;
import com.example.wanhao.aclassapp.config.ApiConstant;
import com.example.wanhao.aclassapp.service.HomeworkService;
import com.example.wanhao.aclassapp.util.RetrofitHelper;
import com.example.wanhao.aclassapp.view.DoHomeworkView;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;

public class DoHomeworkPresenter {
    private static final String TAG = "DoHomeworkPresenter";

    Context context;
    DoHomeworkView view;

    public DoHomeworkPresenter(Context context,DoHomeworkView view){
        this.context = context;
        this.view = view;
    }

    public void getHomeworkList(String courseID){

        HomeworkService service = RetrofitHelper.get(HomeworkService.class);

        service.getHomeworkList(ApiConstant.USER_TOKEN,courseID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new io.reactivex.functions.Consumer<Response<ResponseBody>>() {
                    @Override
                    public void accept(Response<ResponseBody> responseBodyResponse) throws Exception {
                        String body = responseBodyResponse.body().string();
                        Log.i(TAG, "accept: "+body);
                        JsonObject obj = new JsonParser().parse(body).getAsJsonObject();

                        if(obj.get("status").getAsString().equals(ApiConstant.RETURN_ERROR)){
                            view.loadDataError(obj.get("message").getAsString());
                            return;
                        }else {
                            HomeworkResponse result = new Gson().fromJson(body,HomeworkResponse.class);
                            view.loadDataSuccess(result.getQuizList());
                        }

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        view.loadDataError("获取个人信息失败");
                        view.disimissProgress();
                    }
                });
    }

    public void postAnwser(){

    }

}
