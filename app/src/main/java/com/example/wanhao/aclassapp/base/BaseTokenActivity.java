package com.example.wanhao.aclassapp.base;

import android.content.Intent;
import android.support.annotation.NonNull;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.wanhao.aclassapp.activity.LodingActivity;
import com.example.wanhao.aclassapp.config.ApiConstant;
import com.example.wanhao.aclassapp.util.ActivityCollector;
import com.example.wanhao.aclassapp.util.SaveDataUtil;

abstract public class BaseTokenActivity extends BaseActivity{

    protected void showTokenErrorDialog(String msg){
        MaterialDialog.Builder builder = new MaterialDialog.Builder(this)
                .title("错误")
                .content(msg)
                .positiveText("重新登陆")
                .cancelable(false)
                .onAny((dialog, which) -> {
                    switch (which){
                        case POSITIVE:
                            SaveDataUtil.saveToSharedPreferences(this, ApiConstant.USER_TOKEN, "");
                            SaveDataUtil.saveToSharedPreferences(this, ApiConstant.USER_ROLE, "");
                            SaveDataUtil.saveToSharedPreferences(this, ApiConstant.TOKEN_TIME,"");
                            ActivityCollector.finishAll();
                            startActivity(new Intent(BaseApplication.getContext(), LodingActivity.class));
                            break;
                    }
                });
        builder.build().show();
    }

}
