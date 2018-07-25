package com.example.wanhao.aclassapp.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.wanhao.aclassapp.R;
import com.example.wanhao.aclassapp.adapter.QuestionAdapter;
import com.example.wanhao.aclassapp.base.TopBarBaseActivity;
import com.example.wanhao.aclassapp.bean.requestbean.Homework;
import com.example.wanhao.aclassapp.config.ApiConstant;
import com.example.wanhao.aclassapp.presenter.DoHomeworkPresenter;
import com.example.wanhao.aclassapp.util.PagingScrollHelper;
import com.example.wanhao.aclassapp.view.DoHomeworkView;

import java.util.List;

import butterknife.BindView;

public class DoHomeworkActivity extends TopBarBaseActivity implements DoHomeworkView{
    private static final String TAG = "DoHomeworkActivity";

    @BindView(R.id.ac_dohome_recycler)
    RecyclerView recyclerView;

    List<Homework> list;
    private QuestionAdapter adapter;

    private DoHomeworkPresenter presenter;
    private String courseID;

    @Override
    protected int getContentView() {
        return R.layout.activity_do_homework;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        courseID = getIntent().getStringExtra(ApiConstant.COURSE_ID);
        Log.i(TAG, "init: courseID "+courseID);
        initData();
        initView();
        initEvent();
        presenter.getHomeworkList(courseID);
    }

    private void initData() {
        presenter = new DoHomeworkPresenter(this,this);
    }

    private void initView() {
        setTitle("随堂测试");

        adapter = new QuestionAdapter(list);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setAdapter(adapter);

        PagingScrollHelper scrollHelper = new PagingScrollHelper();
        scrollHelper.setUpRecycleView(recyclerView);
    }

    private void initEvent() {
        setTopLeftButton(new OnClickListener() {
            @Override
            public void onClick() {
                finish();
            }
        });

        setTopRightButton("提交", new OnClickListener() {
            @Override
            public void onClick() {
                presenter.postAnwser(list,courseID);
            }
        });

        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()){
                    case R.id.item_qustion_a:
                        list.get(position).setChoose(0);
                        break;
                    case R.id.item_qustion_b:
                        list.get(position).setChoose(1);
                        break;
                    case R.id.item_qustion_c:
                        list.get(position).setChoose(2);
                        break;
                    case R.id.item_qustion_d:
                        list.get(position).setChoose(3);
                        break;
                    case R.id.item_qustion_next:
                        list.get(position).setChoose(-1);
                        break;
                }
                adapter.notifyDataSetChanged();
            }
        });
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
    public void loadDataSuccess(List<Homework> tData) {
        list = tData;
        adapter.setNewData(list);
    }

    @Override
    public void loadDataError(String throwable) {

    }
}
