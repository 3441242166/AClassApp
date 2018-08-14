package com.example.wanhao.aclassapp.presenter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import com.example.wanhao.aclassapp.R;
import com.example.wanhao.aclassapp.bean.Homework;
import com.example.wanhao.aclassapp.bean.HttpResult;
import com.example.wanhao.aclassapp.bean.Question;
import com.example.wanhao.aclassapp.config.ApiConstant;
import com.example.wanhao.aclassapp.service.HomeworkService;
import com.example.wanhao.aclassapp.util.ResourcesUtil;
import com.example.wanhao.aclassapp.util.RetrofitHelper;
import com.example.wanhao.aclassapp.util.SaveDataUtil;
import com.example.wanhao.aclassapp.view.DoHomeworkView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;

import static com.example.wanhao.aclassapp.config.ApiConstant.RETURN_SUCCESS;

public class DoHomeworkPresenter {
    private static final String TAG = "DoHomeworkPresenter";

    Context context;
    DoHomeworkView view;

    public DoHomeworkPresenter(Context context,DoHomeworkView view){
        this.context = context;
        this.view = view;
    }

    @SuppressLint("CheckResult")
    public void getHomeworkList(String courseID){
        List<Question> data = getData();
        Log.i(TAG, "getHomeworkList: size = "+ data.size());
        view.loadDataSuccess(data);


        HomeworkService service = RetrofitHelper.get(HomeworkService.class);

        service.getHomeworkList(SaveDataUtil.getValueFromSharedPreferences(context, ApiConstant.USER_TOKEN),courseID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(responseBodyResponse -> {
                    String body = responseBodyResponse.body().string();
                    Log.i(TAG, "accept: "+body);

                    HttpResult<String> result = new Gson().fromJson(body,new TypeToken<HttpResult<String>>(){}.getType());

                    if(result.getCode().equals(RETURN_SUCCESS)){
                        //view.loadDataSuccess(result.getMessage());
                    }else{
                        view.loadDataError(result.getMessage());
                    }
                }, throwable -> {
                    view.loadDataError(ResourcesUtil.getString(R.string.error_internet));
                    Log.i(TAG, "accept: "+throwable);
                });
    }

    public void postAnwser(List<Homework> list,String courseID){

        HomeworkService service = RetrofitHelper.get(HomeworkService.class);

        for(int x=0;x<list.size();x++){
            HashMap<String,String> map = new HashMap<>();
            //map.put("answer",list.get(x).getUserChoose());
            String json = new Gson().toJson(map);
            Log.i(TAG, "postAnwser: json = "+json);
            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            RequestBody body = RequestBody.create(JSON, json);


//            service.postAnswer(SaveDataUtil.getValueFromSharedPreferences(context,ApiConstant.USER_TOKEN),courseID,String.valueOf(list.get(x).getId()),body)
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(new io.reactivex.functions.Consumer<Response<ResponseBody>>() {
//                        @Override
//                        public void accept(Response<ResponseBody> responseBodyResponse) throws Exception {
//                            view.loadDataError("提交成功");
//                        }
//                    }, new Consumer<Throwable>() {
//                        @Override
//                        public void accept(Throwable throwable) throws Exception {
//                            Log.i(TAG, "accept: "+throwable);
//                            view.loadDataError("提交成功");
//                        }
//                    });
        }

    }

    List<Question> getData(){

        List<Question> data = new ArrayList<>();

        String[] ar = new String[]{"a:b:c:d","1:2:3:4","q:w:e:r"};
        for(int x=0;x<20;x++){
            Question temp = new Question();
            temp.setQuestion(String.valueOf(x));
            temp.setAnswer(ar[x%3]);
            temp.transfrom();
            data.add(temp);
        }

        return data;
    }

}
