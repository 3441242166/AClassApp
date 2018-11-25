package com.example.wanhao.aclassapp.activity;

import android.os.Bundle;

import com.example.wanhao.aclassapp.R;
import com.example.wanhao.aclassapp.base.IBasePresenter;
import com.example.wanhao.aclassapp.base.TopBarBaseActivity;

public class ChatHistoryActivity extends TopBarBaseActivity {
    private static final String TAG = "ChatHistoryActivity";


    @Override
    protected int getContentView() {
        return R.layout.activity_chat_history;
    }

    @Override
    protected void init(Bundle savedInstanceState) {

    }

    @Override
    protected IBasePresenter setPresenter() {
        return null;
    }
}
