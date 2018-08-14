package com.example.wanhao.aclassapp.view;

import android.graphics.Bitmap;

/**
 * Created by wanhao on 2018/3/5.
 */

public interface ICourseView {
    void setHead(Bitmap bitmap);
    void setName(String name);
    void tokenError();
}
