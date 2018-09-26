package com.example.wanhao.aclassapp.view;

import android.graphics.Bitmap;

import com.example.wanhao.aclassapp.base.IBaseTokenView;
import com.example.wanhao.aclassapp.bean.User;

/**
 * Created by wanhao on 2018/2/25.
 */

public interface IUserMessageView extends IBaseTokenView<User> {
    void changeUserSuccess();

    void showImage(Bitmap bitmap);
}
