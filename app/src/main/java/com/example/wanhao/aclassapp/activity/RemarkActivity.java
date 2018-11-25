package com.example.wanhao.aclassapp.activity;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wanhao.aclassapp.R;
import com.example.wanhao.aclassapp.adapter.RemarkAdapter;
import com.example.wanhao.aclassapp.base.TopBarBaseActivity;
import com.example.wanhao.aclassapp.bean.Remark;
import com.example.wanhao.aclassapp.config.ApiConstant;
import com.example.wanhao.aclassapp.presenter.RemarkPresenter;
import com.example.wanhao.aclassapp.util.ColorDividerItemDecoration;
import com.example.wanhao.aclassapp.view.IRemarkView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class RemarkActivity extends TopBarBaseActivity<RemarkPresenter> implements IRemarkView {
    private static final String TAG = "RemarkActivity";

    @BindView(R.id.ac_remark_recycler)
    RecyclerView recyclerView;
    @BindView(R.id.ac_remark_write)
    TextView write;

    private String courseID;

    private List<Remark> list = new ArrayList<>();
    private RemarkAdapter adapter;

    @Override
    protected int getContentView() {
        return R.layout.activity_remark;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        courseID = getIntent().getStringExtra(ApiConstant.COURSE_ID);
        Log.i(TAG, "init: courseID "+courseID);
        initView();
        initEvent();
        presenter.getRemark(courseID);
    }

    private void initEvent() {
        setTopLeftButton(this::finish);

        write.setOnClickListener(view -> {
            final EditText editText = new EditText(RemarkActivity.this);
            AlertDialog.Builder builder = new AlertDialog.Builder(RemarkActivity.this);
            builder.setTitle("输入你的回复")
                    .setNegativeButton("取消", null)
                    .setPositiveButton("确定", (dialogInterface, i) -> presenter.sendRemark(editText.getText().toString(),courseID,-1));
            builder.setView(editText);
            builder.show();
        });

        adapter.setOnItemClickListener((adapter, view, position) -> {
            final EditText editText = new EditText(RemarkActivity.this);
            AlertDialog.Builder builder = new AlertDialog.Builder(RemarkActivity.this);
            builder.setTitle("输入你的回复")
                    .setNegativeButton("取消", null)
                    .setPositiveButton("确定", (dialogInterface, i) -> presenter.sendRemark(editText.getText().toString(),courseID,list.get(position).getId()));
            builder.setView(editText);
            builder.show();
        });
    }

    private void initView() {
        setTitle("留言板");

        adapter = new RemarkAdapter(list,this);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL));
        recyclerView.addItemDecoration(new ColorDividerItemDecoration());
        recyclerView.setAdapter(adapter);

    }


    @Override
    public void tokenError(String msg) {
        showTokenErrorDialog(msg);
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void dismissProgress() {

    }

    @Override
    public void loadDataSuccess(List<Remark> tData) {
        list = tData;
        adapter.setNewData(list);
    }

    @Override
    public void errorMessage(String throwable) {
        Toast.makeText(this,throwable,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void sendRemarkSucess() {
        presenter.getRemark(courseID);
    }

    @Override
    public void sendRemarkError(String msg) {
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }

    @Override
    protected RemarkPresenter setPresenter() {
        return new RemarkPresenter(this,this);
    }
}
