package com.example.wanhao.aclassapp.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.example.wanhao.aclassapp.R;
import com.example.wanhao.aclassapp.adapter.AssignmentAdapter;
import com.example.wanhao.aclassapp.adapter.CourseAdapter;
import com.example.wanhao.aclassapp.base.TopBarBaseActivity;
import com.example.wanhao.aclassapp.bean.Assignment;
import com.example.wanhao.aclassapp.presenter.AssignmentPresenter;
import com.example.wanhao.aclassapp.view.AssignmentView;
import com.yalantis.phoenix.PullToRefreshView;

import java.util.List;

import butterknife.BindView;

public class AssignmentActivity extends TopBarBaseActivity implements AssignmentView{


    @BindView(R.id.ac_assignment_recycler)
    RecyclerView recyclerView;
    @BindView(R.id.ac_assignment_refresh)
    PullToRefreshView refreshView;

    AssignmentPresenter presenter = new AssignmentPresenter(this,this);
    AssignmentAdapter adapter = new AssignmentAdapter(null);

    @Override
    protected int getContentView() {
        return R.layout.activity_assignment;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        presenter.getData();
        initView();
        initEvent();
    }

    private void initEvent() {
        setTopLeftButton(this::finish);

        refreshView.setOnRefreshListener(() -> refreshView.postDelayed(new Runnable() {
            @Override
            public void run() {
                presenter.getData();
            }
        }, 500));

    }

    private void initView() {
        setTitle("课后作业");

        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL));
        //refreshView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(adapter);

    }

    @Override
    public void showProgress() {
        refreshView.setRefreshing(true);
    }

    @Override
    public void dismissProgress() {
        refreshView.setRefreshing(false);
    }

    @Override
    public void loadDataSuccess(List<Assignment> tData) {
        adapter.setNewData(tData);
    }

    @Override
    public void loadDataError(String throwable) {

    }
}
