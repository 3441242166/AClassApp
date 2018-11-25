package com.example.wanhao.aclassapp.presenter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import com.example.wanhao.aclassapp.R;
import com.example.wanhao.aclassapp.base.IBasePresenter;
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

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;

import static com.example.wanhao.aclassapp.config.ApiConstant.RETURN_SUCCESS;

public class DoHomeworkPresenter implements IBasePresenter {
    private static final String TAG = "DoHomeworkPresenter";

    Context context;
    DoHomeworkView view;

    public DoHomeworkPresenter(Context context,DoHomeworkView view){
        this.context = context;
        this.view = view;
    }

    @SuppressLint("CheckResult")
    public void getHomeworkList(String courseID,String homeworkID){

        HomeworkService service = RetrofitHelper.get(HomeworkService.class);

        service.getQuestionList(SaveDataUtil.getValueFromSharedPreferences(context, ApiConstant.USER_TOKEN),courseID,homeworkID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(responseBodyResponse -> {
                    String body = responseBodyResponse.body().string();
                    Log.i(TAG, "accept: "+body);

                    HttpResult<QusetionData> result = new Gson().fromJson(body,new TypeToken<HttpResult<QusetionData>>(){}.getType());

                    if(result.getCode().equals(RETURN_SUCCESS)){
                        List<Question> homeworks = result.getData().homeworks;
                        for(Question question:homeworks){
                            question.init();
                        }
                        view.loadDataSuccess(homeworks);
                    }else{
                        view.errorMessage(result.getMessage());
                    }
                }, throwable -> {
                    view.errorMessage(ResourcesUtil.getString(R.string.error_internet));
                    Log.i(TAG, "accept: "+throwable);
                });
    }

    @SuppressLint("CheckResult")
    public void postAnswer(List<Question> list){
        String data = getErrorString(list);
        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"),data);

        HomeworkService service = RetrofitHelper.get(HomeworkService.class);
        String token = SaveDataUtil.getValueFromSharedPreferences(context,ApiConstant.USER_TOKEN);
        Log.i(TAG, "postAnswer: token = "+token);

        service.postAnswer(token,requestBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(responseBodyResponse -> {
                    Log.i(TAG, "postAnswer: "+responseBodyResponse.errorBody().string());
                    Log.i(TAG, "postAnswer: "+responseBodyResponse.message());
                    String response = responseBodyResponse.body().string();
                    Log.i(TAG, "postAnswer: "+response);

                    view.errorMessage("提交成功");
                }, throwable -> {
                    Log.i(TAG, "postAnswer: "+throwable.toString());
                });

    }

    private String getErrorString(List<Question> list){
        StringBuilder builder = new StringBuilder();
        for(Question question :list){
            String answer = question.getAnswer();
            boolean[] choose = question.getChooses();
            StringBuilder chooseStr = new StringBuilder();
            for(int x=0;x<choose.length;x++){
                if(choose[x]){
                    chooseStr.append((char)('A'+x));
                }
            }
            Log.i(TAG, "getErrorString: chooseStr = "+chooseStr+" answer = "+answer);
            if(!chooseStr.toString().equals(answer)){
                builder.append(question.getId()).append(",");
            }
        }

        if(builder.length()>0) {
            builder.deleteCharAt(builder.length() - 1);
        }
        Log.i(TAG, "getErrorString: final = "+builder.toString());
        return builder.toString();
    }

    @Override
    public void destroy() {

    }

    static class QusetionData{
        List<Question> homeworks;
    }

}
