package com.example.wanhao.aclassapp.presenter;

import android.content.Context;
import android.util.Log;

import com.example.wanhao.aclassapp.bean.Homework;
import com.example.wanhao.aclassapp.bean.HomeworkResponse;
import com.example.wanhao.aclassapp.bean.User;
import com.example.wanhao.aclassapp.config.ApiConstant;
import com.example.wanhao.aclassapp.service.HomeworkService;
import com.example.wanhao.aclassapp.util.RetrofitHelper;
import com.example.wanhao.aclassapp.util.SaveDataUtil;
import com.example.wanhao.aclassapp.view.DoHomeworkView;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.HashMap;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;
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
        Log.i(TAG, "getHomeworkList: courseID = "+courseID);
        HomeworkService service = RetrofitHelper.get(HomeworkService.class);

        service.getHomeworkList(SaveDataUtil.getValueFromSharedPreferences(context,ApiConstant.USER_TOKEN),courseID)
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
                        Log.i(TAG, "accept: "+throwable);
                        view.loadDataError("失败");
                        view.disimissProgress();
                    }
                });
    }

    public void postAnwser(List<Homework> list,String courseID){

        HomeworkService service = RetrofitHelper.get(HomeworkService.class);

        for(int x=0;x<list.size();x++){
            HashMap<String,String> map = new HashMap<>();
            map.put("answer",list.get(x).getUserChoose());
            String json = new Gson().toJson(map);
            Log.i(TAG, "postAnwser: json = "+json);
            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            RequestBody body = RequestBody.create(JSON, json);


            service.postAnswer(SaveDataUtil.getValueFromSharedPreferences(context,ApiConstant.USER_TOKEN),courseID,String.valueOf(list.get(x).getId()),body)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new io.reactivex.functions.Consumer<Response<ResponseBody>>() {
                        @Override
                        public void accept(Response<ResponseBody> responseBodyResponse) throws Exception {
                            view.loadDataError("提交成功");
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            Log.i(TAG, "accept: "+throwable);
                            view.loadDataError("提交成功");
                        }
                    });
        }

    }

}
