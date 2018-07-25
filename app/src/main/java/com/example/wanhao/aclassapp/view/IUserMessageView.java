package com.example.wanhao.aclassapp.view;

import android.graphics.Bitmap;

import com.example.wanhao.aclassapp.base.IBaseTokenView;
import com.example.wanhao.aclassapp.base.IBaseView;
import com.example.wanhao.aclassapp.bean.requestbean.User;

/**
 * Created by wanhao on 2018/2/25.
 */

public interface IUserMessageView extends IBaseTokenView<User> {
    void changeUserSucess();

    void showImage(Bitmap bitmap);
}
