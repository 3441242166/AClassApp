package com.example.wanhao.aclassapp.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.LinearSnapHelper;

import com.example.wanhao.aclassapp.R;
import com.example.wanhao.aclassapp.adapter.QuestionAdapter;
import com.example.wanhao.aclassapp.base.TopBarBaseActivity;
import com.example.wanhao.aclassapp.bean.Question;
import com.example.wanhao.aclassapp.presenter.DoHomeworkPresenter;
import com.example.wanhao.aclassapp.util.PagingScrollHelper;
import com.example.wanhao.aclassapp.view.DoHomeworkView;

import java.util.List;

import butterknife.BindView;

public class DoHomeworkActivity extends TopBarBaseActivity implements DoHomeworkView {

    @BindView(R.id.ac_dohomework_recycler)
    RecyclerView recyclerView;

    QuestionAdapter adapter = new QuestionAdapter(null,this);
    private PagingScrollHelper scrollHelper = new PagingScrollHelper();

    DoHomeworkPresenter presenter = new DoHomeworkPresenter(this,this);

    @Override
    protected int getContentView() {
        return R.layout.activity_do_homework;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        setTitle("数据库第一章课后习题");
        presenter.getHomeworkList("");
        initView();
        initEvent();
    }

    private void initEvent() {
        setTopLeftButton(this::finish);
        scrollHelper.setOnPageChangeListener(index -> {

        });
    }

    private void initView() {
        setTitle("数据库第一章课后习题");
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        new LinearSnapHelper().attachToRecyclerView(recyclerView);
        recyclerView.setAdapter(adapter);
        scrollHelper.setUpRecycleView(recyclerView);
    }

    @Override
    public void tokenError(String msg) {

    }

    @Override
    public void showProgress() {

    }

    @Override
    public void dismissProgress() {

    }

    @Override
    public void loadDataSuccess(List<Question> tData) {
        adapter.setNewData(tData);
    }

    @Override
    public void loadDataError(String throwable) {

    }
}
