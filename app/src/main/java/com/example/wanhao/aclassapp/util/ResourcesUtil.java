package com.example.wanhao.aclassapp.util;

import android.content.Context;

import com.example.wanhao.aclassapp.base.BaseApplication;

public class ResourcesUtil {

    public static String getString(int id){
        return BaseApplication.getContext().getResources().getString(id);
    }

}
