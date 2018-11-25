package com.example.wanhao.aclassapp.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wanhao.aclassapp.R;
import com.example.wanhao.aclassapp.base.IBasePresenter;
import com.example.wanhao.aclassapp.base.TopBarBaseActivity;

import butterknife.BindView;

public class SignActivity extends TopBarBaseActivity {
    private static final String TAG = "SignActivity";

    @BindView(R.id.ac_sign_map)
    ImageView map;
    @BindView(R.id.ac_sign_sign)
    Button btSign;
    @BindView(R.id.ac_sign_leave)
    Button btLeave;
    @BindView(R.id.ac_sign_title)
    TextView title;
    @BindView(R.id.ac_sign_initiator)
    TextView initiator;
    @BindView(R.id.ac_sign_date)
    TextView date;


    @Override
    protected int getContentView() {
        return R.layout.activity_sign;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        initEvent();
    }

    private void initEvent() {
        setTitle("签到");
        setTopLeftButton(this::finish);

    }

    @Override
    protected IBasePresenter setPresenter() {
        return null;
    }
}
