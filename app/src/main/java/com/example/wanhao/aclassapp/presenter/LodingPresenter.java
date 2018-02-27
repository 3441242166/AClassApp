package com.example.wanhao.aclassapp.presenter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.example.wanhao.aclassapp.R;
import com.example.wanhao.aclassapp.config.ApiConstant;
import com.example.wanhao.aclassapp.service.LodingService;
import com.example.wanhao.aclassapp.util.MyDate;
import com.example.wanhao.aclassapp.util.RetrofitHelper;
import com.example.wanhao.aclassapp.util.SaveDataUtil;
import com.example.wanhao.aclassapp.view.ILodingView;

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

public class LodingPresenter implements ILoginPresenter{
    private static final String TAG = "LodingPresenter";

    private ILodingView iLoginView;
    private Context mContext;

    public LodingPresenter(ILodingView loginView, Context context) {
        this.iLoginView = loginView;
        this.mContext = context;
    }

    @Override
    public void login(final String phoneNum, final String password) {
        if (TextUtils.isEmpty(phoneNum)){
            iLoginView.loadDataError("账号不能为空");
            return;
        }
        if (TextUtils.isEmpty(password)){
            iLoginView.loadDataError("密码不能为空");
            return;
        }
        //开始向服务器请求
        iLoginView.showProgress();

        JSONObject jsonObject = new JSONObject();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        try {
            jsonObject.put("username", phoneNum);
            jsonObject.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(JSON, jsonObject.toString());

        LodingService service = RetrofitHelper.get(LodingService.class);
        service.login(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new io.reactivex.functions.Consumer<Response<ResponseBody>>() {
                    @Override
                    public void accept(Response<ResponseBody> responseBodyResponse) throws Exception {
                        if(responseBodyResponse.isSuccessful()){

                            JSONObject object = new JSONObject(responseBodyResponse.body().string());
                            if(object.optString("status").equals("SUCCESS")) {
                                String token = object.optString(ApiConstant.USER_TOKEN);
                                String role = object.optString(ApiConstant.USER_ROLE);
                                Log.i(TAG, "accept: " + token + "  " + role);
                                SaveDataUtil.saveToSharedPreferences(mContext, ApiConstant.USER_TOKEN, token);
                                SaveDataUtil.saveToSharedPreferences(mContext, ApiConstant.USER_ROLE, role);
                                SaveDataUtil.saveToSharedPreferences(mContext, ApiConstant.COUNT, phoneNum);
                                SaveDataUtil.saveToSharedPreferences(mContext, ApiConstant.PASSWORD, password);
                                SaveDataUtil.saveToSharedPreferences(mContext, ApiConstant.TOKEN_TIME, MyDate.getNowDateString());
                                iLoginView.disimissProgress();
                                iLoginView.loadDataSuccess(null);
                            }else{
                                iLoginView.disimissProgress();
                                iLoginView.loadDataError("账户或密码错误");
                            }
                        }else{
                            iLoginView.disimissProgress();
                            iLoginView.loadDataError("账户或密码错误");
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        iLoginView.disimissProgress();
                        iLoginView.loadDataError(mContext.getResources().getString(R.string.internet_error));
                    }
                });
    }

    @Override
    public void init() {
        String count = SaveDataUtil.getValueFromSharedPreferences(mContext,ApiConstant.COUNT);
        String password = SaveDataUtil.getValueFromSharedPreferences(mContext,ApiConstant.PASSWORD);
        iLoginView.initData(count,password);
    }

}

interface ILoginPresenter{
    void login(String phoneNum, String password);

    void init();
}