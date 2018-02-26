package com.example.wanhao.aclassapp.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import com.example.wanhao.aclassapp.R;

/**
 * Created by wanhao on 2018/2/25.
 */

public class CourseDialog extends Dialog {

    public CourseDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_course);

    }

}
