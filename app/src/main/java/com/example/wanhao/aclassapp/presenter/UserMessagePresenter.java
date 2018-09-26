package com.example.wanhao.aclassapp.presenter;

import android.annotation.SuppressLint;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.example.wanhao.aclassapp.Model.CourseListModel;
import com.example.wanhao.aclassapp.R;
import com.example.wanhao.aclassapp.activity.UserMessageActivity;
import com.example.wanhao.aclassapp.bean.HttpResult;
import com.example.wanhao.aclassapp.bean.Role;
import com.example.wanhao.aclassapp.bean.User;
import com.example.wanhao.aclassapp.config.ApiConstant;
import com.example.wanhao.aclassapp.service.UserMessageService;
import com.example.wanhao.aclassapp.util.RetrofitHelper;
import com.example.wanhao.aclassapp.util.SaveDataUtil;
import com.example.wanhao.aclassapp.view.IUserMessageView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static com.example.wanhao.aclassapp.config.ApiConstant.BASE_URL;
import static com.example.wanhao.aclassapp.config.ApiConstant.RETURN_SUCCESS;

/**
 * Created by wanhao on 2018/2/25.
 */

public class UserMessagePresenter {
    private static final String TAG = "UserMessagePresenter";

    private Context context;
    private IUserMessageView view;
    private CourseListModel model;
    private UserMessageService service;


    public UserMessagePresenter(Context context, IUserMessageView view){
        this.context = context;
        this.view = view;
        model = new CourseListModel(context);
        service = RetrofitHelper.get(UserMessageService.class);
    }

    public void init(){
        view.showProgress();
        getUserMessage();
        getHeadImage();
    }

    @SuppressLint("CheckResult")
    public void getUserMessage(){

        service.getProfile(SaveDataUtil.getValueFromSharedPreferences(context, ApiConstant.USER_TOKEN))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(responseBodyResponse -> {
                    String body = responseBodyResponse.body().string();
                    Log.i(TAG, "accept: "+body);

                    HttpResult<User> result = new Gson().fromJson(body,new TypeToken<HttpResult<User>>(){}.getType());

                    if(result.getCode().equals(RETURN_SUCCESS)){
                        view.loadDataSuccess(result.getData());
                        SaveDataUtil.saveToSharedPreferences(context,ApiConstant.USER_NAME,result.getData().getNickName());
                    }else{
                        view.loadDataError(result.getMessage());
                        view.tokenError("token失效，请重新登陆");
                    }
                    view.dismissProgress();
                }, throwable -> {
                    view.loadDataError("获取个人信息失败");
                    view.dismissProgress();
                    Log.i(TAG, "accept: "+throwable);
                });

    }

    @SuppressLint("CheckResult")
    public void sentUserMessage(User user){
        view.showProgress();

        String json = new Gson().toJson(user);
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody requestBody = RequestBody.create(JSON, json);

        service.postProfile(SaveDataUtil.getValueFromSharedPreferences(context, ApiConstant.USER_TOKEN),requestBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(responseBodyResponse -> {
                    String body = responseBodyResponse.body().string();
                    Log.i(TAG, "accept: "+body);

                    HttpResult<Role> result = new Gson().fromJson(body,new TypeToken<HttpResult<Role>>(){}.getType());
                    if(result.getCode().equals(ApiConstant.RETURN_SUCCESS)){
                        view.changeUserSuccess();
                    }else{
                        view.loadDataError(result.getMessage());
                    }
                }, throwable -> {
                    Log.i(TAG, "sentUserMessage: "+throwable);
                    view.loadDataError(context.getResources().getString(R.string.error_internet));
                });
        view.dismissProgress();
    }

    @SuppressLint("CheckResult")
    private void postHeadImage(String path){
        view.showProgress();
        RequestBody body = RequestBody.create(MediaType.parse("multipart/form-data"),new File(path));
        MultipartBody.Part part = MultipartBody.Part.createFormData("file", "avatar.png", body);
        service.uploadAvatar(SaveDataUtil.getValueFromSharedPreferences(context,ApiConstant.USER_TOKEN),part)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(responseBodyResponse -> {
                    view.changeUserSuccess();
                    view.dismissProgress();
                }, throwable -> {
                    view.loadDataError(context.getResources().getString(R.string.error_internet));
                    view.dismissProgress();
                });
    }

    @SuppressLint("CheckResult")
    public void getHeadImage(){

        GlideUrl cookie = new GlideUrl(BASE_URL+"avatar"
                , new LazyHeaders.Builder().addHeader("Authorization", SaveDataUtil.getValueFromSharedPreferences(context,ApiConstant.USER_TOKEN)).build());
        Glide.with(context)
                .load(cookie)
                .asBitmap()
                .into(target);
    }
    private SimpleTarget<Bitmap> target = new SimpleTarget<Bitmap>(50,50) {
        @Override
        public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
            view.showImage(resource);
        }
    };

    public void openSelectAvatarDialog(){
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_select_avatar, null);
        final PopupWindow popupWindow = getPopupWindow(view);
        //设置点击事件
        TextView cameraTextView = view.findViewById(R.id.dialog_cmera);
        TextView selectAvatar = view.findViewById(R.id.dialog_photo);
        cameraTextView.setOnClickListener(v -> {
            takeCamera();
            popupWindow.dismiss();});

        selectAvatar.setOnClickListener(v -> {
                openGallery();
                popupWindow.dismiss(); });

        View parent = LayoutInflater.from(context).inflate(R.layout.fragment_my, null);
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
        String filePath;
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
