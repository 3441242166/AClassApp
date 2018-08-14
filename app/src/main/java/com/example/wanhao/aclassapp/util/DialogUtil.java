package com.example.wanhao.aclassapp.util;

import android.content.Context;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.wanhao.aclassapp.base.BaseApplication;

public class DialogUtil {

    public static MaterialDialog waitDialog(Context context){
        MaterialDialog.Builder builder = new MaterialDialog.Builder(context)
                .title("Zzz...")
                .content("加载中...")
                .cancelable(false)
                .progress(true,100,false);

        return builder.build();
    }

}
