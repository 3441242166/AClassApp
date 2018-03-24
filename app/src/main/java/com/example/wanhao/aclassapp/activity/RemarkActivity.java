package com.example.wanhao.aclassapp.activity;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.example.wanhao.aclassapp.R;
import com.example.wanhao.aclassapp.base.TopBarBaseActivity;

import butterknife.BindView;

public class RemarkActivity extends TopBarBaseActivity {

    @BindView(R.id.ac_remark_recycler)
    RecyclerView recyclerView;
    @BindView(R.id.ac_remark_write)
    TextView write;

    @Override
    protected int getContentView() {
        return R.layout.activity_remark;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        initView();
        initEvent();
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
    }


}
