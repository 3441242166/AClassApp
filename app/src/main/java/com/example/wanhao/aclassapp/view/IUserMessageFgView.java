package com.example.wanhao.aclassapp.view;

import android.graphics.Bitmap;

import com.bumptech.glide.load.model.GlideUrl;
import com.example.wanhao.aclassapp.base.IBaseTokenView;
import com.example.wanhao.aclassapp.base.IBaseView;
import com.example.wanhao.aclassapp.bean.User;

import java.util.BitSet;

public interface IUserMessageFgView extends IBaseTokenView<User> {
    void setUserHead(GlideUrl bitmap);
}
