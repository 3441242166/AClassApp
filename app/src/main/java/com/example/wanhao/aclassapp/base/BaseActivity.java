package com.example.wanhao.aclassapp.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.wanhao.aclassapp.util.ActivityCollector;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollector.addActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }

}
