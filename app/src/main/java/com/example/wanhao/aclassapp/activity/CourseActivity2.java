package com.example.wanhao.aclassapp.activity;

import android.hardware.display.DisplayManager;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.widget.LinearLayout;

import com.example.wanhao.aclassapp.R;
import com.example.wanhao.aclassapp.util.ActivityCollector;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CourseActivity2 extends AppCompatActivity {
    private static final String TAG = "CourseActivity2";

    @BindView(R.id.ac_course_toolbar)
    Toolbar toolbar;
    @BindView(R.id.ac_course_layout)
    ConstraintLayout layout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course2);
        ActivityCollector.addActivity(this);
        ButterKnife.bind(this);

        init();
    }

    private void init() {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        layout.setMinimumHeight(metrics.heightPixels);
    }
}
