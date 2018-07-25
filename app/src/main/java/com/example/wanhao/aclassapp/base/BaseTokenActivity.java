package com.example.wanhao.aclassapp.base;

import android.content.Intent;
import android.support.annotation.NonNull;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.wanhao.aclassapp.activity.LodingActivity;
import com.example.wanhao.aclassapp.util.ActivityCollector;

public class BaseTokenActivity extends BaseActivity{

    protected void tokenError(){
        MaterialDialog.Builder builder = new MaterialDialog.Builder(BaseApplication.getContext())
                .title("错误")
                .content("登陆已失效,请重新登陆")
                .positiveText("重新登陆")
                .cancelable(false)
                .onAny(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        switch (which){
                            case POSITIVE:
                                ActivityCollector.finishAll();
                                startActivity(new Intent(BaseApplication.getContext(), LodingActivity.class));
                                break;
                        }
                    }
                });
    }

}
