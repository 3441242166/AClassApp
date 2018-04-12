package com.example.wanhao.aclassapp.presenter;

import android.content.Context;
import android.util.Log;

import com.example.wanhao.aclassapp.bean.NoDataResponse;
import com.example.wanhao.aclassapp.bean.RemarkRequset;
import com.example.wanhao.aclassapp.bean.RemarkResult;
import com.example.wanhao.aclassapp.config.ApiConstant;
import com.example.wanhao.aclassapp.service.RemarkService;
import com.example.wanhao.aclassapp.util.RetrofitHelper;
import com.example.wanhao.aclassapp.util.SaveDataUtil;
import com.example.wanhao.aclassapp.view.IRemarkView;
import com.google.gson.Gson;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * Created by wanhao on 2018/3/28.
 */

public class RemarkPresenter {
    private static final String TAG = "RemarkPresenter";

    private IRemarkView iRemarkView;
    private Context context;

    public RemarkPresenter(IRemarkView view,Context context){
        this.iRemarkView = view;
        this.context = context;
    }

    public void getRemark(int courseID){
        Log.i(TAG, "getRemark: courseID "+courseID);
        RemarkService service = RetrofitHelper.get(RemarkService.class);

        service.getRemark(SaveDataUtil.getValueFromSharedPreferences(context,ApiConstant.USER_TOKEN),courseID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new io.reactivex.functions.Consumer<Response<ResponseBody>>() {
                    @Override
                    public void accept(Response<ResponseBody> responseBodyResponse) throws Exception {
                        String body = responseBodyResponse.body().string();
                        Log.i(TAG, "accept: "+body);
                        RemarkResult result = new Gson().fromJson(body,RemarkResult.class);

                        if(result.getStatus().equals(ApiConstant.RETURN_SUCCESS)){
                            iRemarkView.loadDataSuccess(result.getList());
                        }else{
                            iRemarkView.tokenError("error");
                        }

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                });

    }

    public void sendRemark(String remark,int courseID,int replyID){
        Log.i(TAG, "sendRemark: ");
        RemarkRequset remarkRequset = new RemarkRequset(replyID,remark);

        String json = new Gson().toJson(remarkRequset);
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, json);
        Log.i(TAG, "sendRemark: json  "+json);
        RemarkService service = RetrofitHelper.get(RemarkService.class);

        service.sendRemark(SaveDataUtil.getValueFromSharedPreferences(context,ApiConstant.USER_TOKEN),courseID,body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new io.reactivex.functions.Consumer<Response<ResponseBody>>() {
                    @Override
                    public void accept(Response<ResponseBody> responseBodyResponse) throws Exception {
                        NoDataResponse result = new Gson().fromJson(responseBodyResponse.body().string(),NoDataResponse.class);
                        if(result.getStatus().equals(ApiConstant.RETURN_SUCCESS)){

                            iRemarkView.sendRemarkSucess();
                        }else{
                            iRemarkView.tokenError("token error");
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        iRemarkView.tokenError(throwable.toString());
                    }
                });

    }

}
