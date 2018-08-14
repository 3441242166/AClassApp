package com.example.wanhao.aclassapp.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.example.wanhao.aclassapp.R;
import com.example.wanhao.aclassapp.base.TopBarBaseActivity;
import com.yalantis.phoenix.PullToRefreshView;

import butterknife.BindView;

public class AssignmentActivity extends TopBarBaseActivity {


    @BindView(R.id.ac_assignment_recycler)
    RecyclerView recyclerView;
    @BindView(R.id.ac_assignment_refresh)
    PullToRefreshView refreshView;

    @Override
    protected int getContentView() {
        return R.layout.activity_assignment;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        setTitle("课后作业");
        setTopLeftButton(this::finish);
        initView();
        initEvent();
    }

    private void initEvent() {

    }

    private void initView() {

    }
}
