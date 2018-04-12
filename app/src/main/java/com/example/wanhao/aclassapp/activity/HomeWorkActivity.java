package com.example.wanhao.aclassapp.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.example.wanhao.aclassapp.R;
import com.example.wanhao.aclassapp.base.TopBarBaseActivity;
import com.example.wanhao.aclassapp.config.ApiConstant;

import butterknife.BindView;

public class HomeWorkActivity extends TopBarBaseActivity {
    private static final String TAG = "HomeWorkActivity";

    @BindView(R.id.ac_homework_tab)
    TabLayout tableLayout;
    @BindView(R.id.ac_homework_pager)
    ViewPager viewPager;

    private String courseID;

    @Override
    protected int getContentView() {
        return R.layout.activity_home_work;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        courseID = getIntent().getStringExtra(ApiConstant.COURSE_ID);

        initData();
        initView();
        initEvent();

    }

    private void initData() {

    }

    private void initView() {

    }

    private void initEvent() {
        setTopLeftButton(new OnClickListener() {
            @Override
            public void onClick() {
                finish();
            }
        });

    }

}
