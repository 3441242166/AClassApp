package com.example.wanhao.aclassapp.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.example.wanhao.aclassapp.R;
import com.example.wanhao.aclassapp.base.TopBarBaseActivity;

import butterknife.BindView;

public class DocumentActivity extends TopBarBaseActivity {


    @BindView(R.id.ac_document_pager)
    ViewPager viewPager;
    @BindView(R.id.ac_document_tab)
    TabLayout tabLayout;

    @Override
    protected int getContentView() {
        return R.layout.activity_document;
    }

    @Override
    protected void init(Bundle savedInstanceState) {

        initView();
        initEvent();

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

        setTopRightButton("筛选", new OnClickListener() {
            @Override
            public void onClick() {

            }
        });
    }


}
