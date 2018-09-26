package com.example.wanhao.aclassapp.view;

import android.graphics.Bitmap;

import com.bumptech.glide.load.model.GlideUrl;
import com.example.wanhao.aclassapp.bean.User;

import java.util.BitSet;

public interface IUserMessageFgView {

    void setUserMessage(User userMessage);

    void setUserHead(GlideUrl bitmap);
}
