package com.example.wanhao.aclassapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.wanhao.aclassapp.R;
import com.example.wanhao.aclassapp.adapter.HomeworkAdapter;
import com.example.wanhao.aclassapp.base.TopBarBaseActivity;
import com.example.wanhao.aclassapp.bean.Homework;
import com.example.wanhao.aclassapp.config.ApiConstant;
import com.example.wanhao.aclassapp.util.DateUtil;
import com.example.wanhao.aclassapp.util.MyItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class HomeWorkActivity extends TopBarBaseActivity {
    private static final String TAG = "HomeWorkActivity";

    @BindView(R.id.ac_homeword_recycler)
    RecyclerView recyclerView;

    private String courseID;

    private List<Homework> list;
    private HomeworkAdapter adapter;

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
        list = new ArrayList<>();
        for(int x=0;x<10;x++){
            Homework homework = new Homework();
            homework.setDate(DateUtil.getNowDateString());
            homework.setTitle(x+1+"");
            list.add(homework);
        }
    }

    private void initView() {
        adapter = new HomeworkAdapter(list);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL));
        recyclerView.addItemDecoration(new MyItemDecoration());
        recyclerView.setAdapter(adapter);

    }

    private void initEvent() {
        setTopLeftButton(new OnClickListener() {
            @Override
            public void onClick() {
                finish();
            }
        });

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                startActivity(new Intent(HomeWorkActivity.this,DoHomeworkActivity.class));
            }
        });
    }

}
