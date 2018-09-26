package com.example.wanhao.aclassapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.example.wanhao.aclassapp.R;
import com.example.wanhao.aclassapp.adapter.HomeworkAdapter;
import com.example.wanhao.aclassapp.base.TopBarBaseActivity;
import com.example.wanhao.aclassapp.bean.Homework;
import com.example.wanhao.aclassapp.config.ApiConstant;
import com.example.wanhao.aclassapp.presenter.HomeworkPresenter;
import com.example.wanhao.aclassapp.view.IHomeworkView;

import java.util.List;

import butterknife.BindView;

public class HomeWorkActivity extends TopBarBaseActivity implements IHomeworkView{

    @BindView(R.id.ac_homeword_recycler)
    RecyclerView recyclerView;

    HomeworkPresenter presenter = new HomeworkPresenter(this,this);

    HomeworkAdapter adapter = new HomeworkAdapter(null);

    private String courseID;

    @Override
    protected int getContentView() {
        return R.layout.activity_homework;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        courseID = getIntent().getStringExtra(ApiConstant.COURSE_ID);
        initView();
        initEvent();
    }

    private void initEvent() {
        setTopLeftButton(this::finish);

        adapter.setOnItemClickListener((adapter, view, position) -> startActivity(new Intent(HomeWorkActivity.this,DoHomeworkActivity.class)));
    }

    private void initView() {
        setTitle("数据库习题库");
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setAdapter(adapter);

        presenter.getHomeworkList(courseID);
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void dismissProgress() {

    }

    @Override
    public void loadDataSuccess(List<Homework> tData) {
        adapter.setNewData(tData);
    }

    @Override
    public void loadDataError(String throwable) {

    }
}
