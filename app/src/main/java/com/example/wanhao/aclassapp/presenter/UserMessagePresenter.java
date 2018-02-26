package com.example.wanhao.aclassapp.presenter;

import android.content.Context;

import com.example.wanhao.aclassapp.Model.CourseModel;
import com.example.wanhao.aclassapp.R;
import com.example.wanhao.aclassapp.bean.NoDataResponse;
import com.example.wanhao.aclassapp.bean.User;
import com.example.wanhao.aclassapp.config.ApiConstant;
import com.example.wanhao.aclassapp.service.UserMessageService;
import com.example.wanhao.aclassapp.util.RetrofitHelper;
import com.example.wanhao.aclassapp.util.SaveDataUtil;
import com.example.wanhao.aclassapp.view.IUserMessageView;
import com.google.gson.Gson;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * Created by wanhao on 2018/2/25.
 */

public class UserMessagePresenter {
    private static final String TAG = "UserMessagePresenter";

    private Context context;
    private IUserMessageView view;
    private CourseModel model;


    public UserMessagePresenter(Context context, IUserMessageView view){
        this.context = context;
        this.view = view;
        model = new CourseModel(context);
    }

    public void getUserMessage(){
        view.showProgress();
        UserMessageService service = RetrofitHelper.get(UserMessageService.class);
        service.getProfile(SaveDataUtil.getValueFromSharedPreferences(context, ApiConstant.USER_TOKEN))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new io.reactivex.functions.Consumer<Response<ResponseBody>>() {
                    @Override
                    public void accept(Response<ResponseBody> responseBodyResponse) throws Exception {
                        User result = new Gson().fromJson(responseBodyResponse.body().string(),User.class);
                        view.loadDataSuccess(result);
                        view.disimissProgress();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        view.loadDataError(throwable.toString());
                        view.disimissProgress();
                    }
                });

    }

    public void sentUserMessage(User user){
        view.showProgress();
        UserMessageService service = RetrofitHelper.get(UserMessageService.class);

        String json = new Gson().toJson(user);
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, json);

        service.postProfile(SaveDataUtil.getValueFromSharedPreferences(context, ApiConstant.USER_TOKEN),body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new io.reactivex.functions.Consumer<Response<ResponseBody>>() {
                    @Override
                    public void accept(Response<ResponseBody> responseBodyResponse) throws Exception {
                        NoDataResponse result = new Gson().fromJson(responseBodyResponse.body().string(),NoDataResponse.class);
                        if(result.getStatus().equals(ApiConstant.RETURN_SUCCESS)){
                            view.changeUserSucess();
                            view.disimissProgress();
                        }else{
                            view.loadDataError(context.getResources().getString(R.string.internet_error));
                            view.disimissProgress();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        view.loadDataError(context.getResources().getString(R.string.internet_error));
                        view.disimissProgress();
                    }
                });
    }

}
