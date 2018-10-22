package com.example.wanhao.aclassapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.wanhao.aclassapp.presenter.SplashPresenter;
import com.example.wanhao.aclassapp.view.ISplashView;

public class SplashActivity extends AppCompatActivity implements ISplashView {

    private SplashPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new SplashPresenter(this,this);
        presenter.loding();
    }

    @Override
    public void goLoding() {
        startActivity(new Intent(this,LodingActivity.class));
        finish();
    }

    @Override
    public void goCourse() {
        startActivity(new Intent(this,MainActivity.class));
        finish();
    }
}
