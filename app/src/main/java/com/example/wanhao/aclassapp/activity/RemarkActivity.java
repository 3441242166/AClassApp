package com.example.wanhao.aclassapp.activity;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wanhao.aclassapp.R;
import com.example.wanhao.aclassapp.adapter.RemarkAdapter;
import com.example.wanhao.aclassapp.base.TopBarBaseActivity;
import com.example.wanhao.aclassapp.bean.Remark;
import com.example.wanhao.aclassapp.config.ApiConstant;
import com.example.wanhao.aclassapp.presenter.RemarkPresenter;
import com.example.wanhao.aclassapp.util.MyItemDecoration;
import com.example.wanhao.aclassapp.view.IRemarkView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class RemarkActivity extends TopBarBaseActivity implements IRemarkView {
    private static final String TAG = "RemarkActivity";

    @BindView(R.id.ac_remark_recycler)
    RecyclerView recyclerView;
    @BindView(R.id.ac_remark_write)
    TextView write;

    private RemarkPresenter presenter;
    private String courseID;

    private List<Remark> list;
    private RemarkAdapter adapter;

    @Override
    protected int getContentView() {
        return R.layout.activity_remark;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        courseID = getIntent().getStringExtra(ApiConstant.COURSE_ID);
        Log.i(TAG, "init: courseID "+courseID);
        presenter = new RemarkPresenter(this,this);
        initData();
        initView();
        initEvent();
        presenter.getRemark(Integer.valueOf(courseID));
    }

    private void initData() {
        list = new ArrayList<>();

    }

    private void initEvent() {
        setTopLeftButton(new OnClickListener() {
            @Override
            public void onClick() {
                finish();
            }
        });

    }

    private void initView() {
        setTitle("留言板");

        adapter = new RemarkAdapter(list,this);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL));
        recyclerView.addItemDecoration(new MyItemDecoration());
        recyclerView.setAdapter(adapter);

    }


    @Override
    public void tokenError(String msg) {

    }

    @Override
    public void showProgress() {

    }

    @Override
    public void disimissProgress() {

    }

    @Override
    public void loadDataSuccess(List<Remark> tData) {
        list = tData;
        adapter.setNewData(list);
    }

    @Override
    public void loadDataError(String throwable) {
        Toast.makeText(this,throwable,Toast.LENGTH_SHORT).show();
    }
}
