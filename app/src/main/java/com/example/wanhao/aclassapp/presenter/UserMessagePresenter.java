package com.example.wanhao.aclassapp.presenter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.wanhao.aclassapp.Model.CourseModel;
import com.example.wanhao.aclassapp.R;
import com.example.wanhao.aclassapp.activity.UserMessageActivity;
import com.example.wanhao.aclassapp.bean.NoDataResponse;
import com.example.wanhao.aclassapp.bean.User;
import com.example.wanhao.aclassapp.config.ApiConstant;
import com.example.wanhao.aclassapp.service.UserMessageService;
import com.example.wanhao.aclassapp.util.RetrofitHelper;
import com.example.wanhao.aclassapp.util.SaveDataUtil;
import com.example.wanhao.aclassapp.view.IUserMessageView;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
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
    UserMessageService service;


    public UserMessagePresenter(Context context, IUserMessageView view){
        this.context = context;
        this.view = view;
        model = new CourseModel(context);
        service = RetrofitHelper.get(UserMessageService.class);
    }

    public void getUserMessage(){
        view.showProgress();

        service.getProfile(SaveDataUtil.getValueFromSharedPreferences(context, ApiConstant.USER_TOKEN))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new io.reactivex.functions.Consumer<Response<ResponseBody>>() {
                    @Override
                    public void accept(Response<ResponseBody> responseBodyResponse) throws Exception {
                        String body = responseBodyResponse.body().string();
                        Log.i(TAG, "accept: "+body);
                        User result = new Gson().fromJson(body,User.class);

                        view.loadDataSuccess(result);
                        view.disimissProgress();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        view.loadDataError("获取个人信息失败");
                        view.disimissProgress();
                    }
                });

    }

    public void sentUserMessage(User user){
        view.showProgress();

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

    public void postHeadImage(String path){
        view.showProgress();
        RequestBody body = RequestBody.create(MediaType.parse("multipart/form-data"),new File(path));
        MultipartBody.Part part = MultipartBody.Part.createFormData("file", "avatar.png", body);
        service.uploadAvatar(SaveDataUtil.getValueFromSharedPreferences(context,ApiConstant.USER_TOKEN),part)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new io.reactivex.functions.Consumer<Response<ResponseBody>>() {
                    @Override
                    public void accept(Response<ResponseBody> responseBodyResponse) throws Exception {
                        view.changeUserSucess();
                        view.disimissProgress();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        view.loadDataError(context.getResources().getString(R.string.internet_error));
                        view.disimissProgress();
                    }
                });
    }

    public void getHeadImage(){
        service.getAvatar(SaveDataUtil.getValueFromSharedPreferences(context,ApiConstant.USER_TOKEN))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new io.reactivex.functions.Consumer<Response<ResponseBody>>() {
                    @Override
                    public void accept(Response<ResponseBody> responseBodyResponse) throws Exception {
                        Bitmap bitmap = BitmapFactory.decodeStream(responseBodyResponse.body().byteStream());
                        view.showImage(bitmap);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        view.loadDataError("获取头像失败");
                    }
                });
    }

    public void openSelectAvatarDialog(){
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_select_avatar, null);
        final PopupWindow popupWindow = getPopupWindow(view);
        //设置点击事件
        TextView cameraTextView = (TextView) view.findViewById(R.id.dialog_cmera);
        TextView selectAvatar = (TextView) view.findViewById(R.id.dialog_photo);
        cameraTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takeCamera();
                popupWindow.dismiss();
            }
        });
        selectAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
                popupWindow.dismiss();
            }
        });
        View parent = LayoutInflater.from(context).inflate(R.layout.activity_user_message, null);
        //显示PopupWindow
        popupWindow.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
    }

    private PopupWindow getPopupWindow(View view) {
        PopupWindow popupWindow = new PopupWindow(context);
        popupWindow.setContentView(view);
        popupWindow.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        return popupWindow;
    }

    private void takeCamera() {
        //构建隐式Intent
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //调用系统相机
        UserMessageActivity activity = (UserMessageActivity) context;
        activity.startActivityForResult(intent, ApiConstant.CAMERA_CODE);
    }

    private void openGallery() {
        UserMessageActivity activity = (UserMessageActivity) context;
        Intent albumIntent = new Intent(Intent.ACTION_PICK);
        albumIntent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        activity.startActivityForResult(albumIntent, ApiConstant.GALLERY_CODE);
    }

    public void onSelectImage(Uri uri) {
        //两种情况存在
        String filePath = "";
        long fileSize = 0;
        //URI的scheme直接就是file://.....
        if ("file".equals(uri.getScheme())) {
            //直接调用getPath方法就可以了
            filePath = uri.getPath();
        } else {
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = context.getContentResolver()
                    .query(uri, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int path = cursor.getColumnIndex(filePathColumn[0]);
            filePath = cursor.getString(path);
            cursor.close();
        }

        //判断大小,超过2M的不能选择
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(uri);
            //获取大小
            long size = inputStream.available();

            //提示重新选择
            if (size >= 1024*2048) {
                //ToastUtils.createToast("选择图片大于2M，请重新选择");
            } else {
                Bitmap bitmap = BitmapFactory.decodeFile(filePath);
                Log.i(TAG, "onSelectImage: bitmap"+bitmap);
                //显示新的头像
                view.showImage(bitmap);
                postHeadImage(filePath);
            }

            inputStream.close();
        } catch (
                FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
