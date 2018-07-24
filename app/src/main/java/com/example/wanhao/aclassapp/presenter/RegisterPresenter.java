package com.example.wanhao.aclassapp.presenter;

import android.content.Context;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

import com.example.wanhao.aclassapp.bean.NoDataResponse;
import com.example.wanhao.aclassapp.config.ApiConstant;
import com.example.wanhao.aclassapp.service.RegisterService;
import com.example.wanhao.aclassapp.util.RetrofitHelper;
import com.example.wanhao.aclassapp.view.IRegisterView;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Response;


/**
 * Created by wanhao on 2017/11/22.
 */

public class RegisterPresenter {
    private static final String TAG = "RegisterPresenter";

    private IRegisterView iRegisterView;
    private Context mContext;

    public RegisterPresenter(IRegisterView loginView, Context context) {
        this.iRegisterView = loginView;
        this.mContext = context;
    }
    //获取手机验证码
    public void getVerificationCode(final String phoneNum, final TextView getCodeTv, final CountDownTimer timer) {
        if (TextUtils.isEmpty(phoneNum)) {
            iRegisterView.loadDataError("手机号不能为空");
            return;
        }
        getCodeTv.setClickable(false);
        timer.start();
    }

    //注册
    public void register(String phoneNum, String password, String code){
        if (TextUtils.isEmpty(phoneNum)){
            iRegisterView.loadDataError("手机号不能为空");
            return;
        }
        if (TextUtils.isEmpty(code)){
            iRegisterView.loadDataError("验证码不能为空");
            return;
        }
        if (TextUtils.isEmpty(password)){
            iRegisterView.loadDataError("密码不能为空");
            return;
        }
        //发送请求进行注册
        //首先检查验证码是否正确

        iRegisterView.showProgress();

        RegisterService service = RetrofitHelper.get(RegisterService.class);

        JSONObject jsonObject = new JSONObject();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        try {
            jsonObject.put(ApiConstant.USER_NAME, phoneNum);
            jsonObject.put(ApiConstant.PASSWORD, password);
            jsonObject.put(ApiConstant.USER_ROLE, "student");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(JSON, jsonObject.toString());

        service.register(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new io.reactivex.functions.Consumer<Response<ResponseBody>>() {
                    @Override
                    public void accept(Response<ResponseBody> responseBodyResponse) throws Exception {
                        String body = responseBodyResponse.body().string();
                        Log.i(TAG, "accept: "+body);
                        NoDataResponse result = new Gson().fromJson(body,NoDataResponse.class);
                        if(result.getStatus().equals(ApiConstant.RETURN_SUCCESS)){
                            iRegisterView.loadDataSuccess("success");
                        }else{
                            iRegisterView.loadDataError("其他错误");
                        }
                        iRegisterView.disimissProgress();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        iRegisterView.loadDataError("网络错误");
                        iRegisterView.disimissProgress();
                    }
                });

    }
}
