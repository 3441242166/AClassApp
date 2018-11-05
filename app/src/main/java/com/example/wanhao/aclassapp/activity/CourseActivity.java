package com.example.wanhao.aclassapp.activity;

import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Rect;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.wanhao.aclassapp.R;
import com.example.wanhao.aclassapp.adapter.ChatAdapter;
import com.example.wanhao.aclassapp.base.TopBarBaseActivity;
import com.example.wanhao.aclassapp.bean.ChatBean;
import com.example.wanhao.aclassapp.broadcast.CourseReceiver;
import com.example.wanhao.aclassapp.config.ApiConstant;
import com.example.wanhao.aclassapp.presenter.CoursePresenter;
import com.example.wanhao.aclassapp.view.CourseView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.example.wanhao.aclassapp.config.ApiConstant.COURSE_ID;
import static com.example.wanhao.aclassapp.config.Constant.SCREEN_HEIGHT;
import static com.example.wanhao.aclassapp.util.SaveDataUtil.getValueFromSharedPreferences;

public class CourseActivity extends TopBarBaseActivity implements CourseView {
    private static final String TAG = "CourseActivity";

    CoursePresenter presenter;

    @BindView(R.id.ac_course_list)
    RecyclerView recyclerView;
    @BindView(R.id.ac_course_fab)
    FloatingActionButton btMore;
    @BindView(R.id.ac_course_send)
    Button btSend;
    @BindView(R.id.ac_course_input)
    EditText input;
    @BindView(R.id.ac_course_layout)
    ConstraintLayout layout;

    List<ChatBean> chatList;

    ChatAdapter adapter;
    LinearLayoutManager manager ;
    private CourseReceiver receiver;
    String courseID ;

    boolean mIsSoftKeyboardShowing = false;

    @Override
    protected int getContentView() {
        return R.layout.activity_course;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        courseID = getIntent().getStringExtra(ApiConstant.COURSE_ID);
        presenter = new CoursePresenter(this,this,courseID);

        receiver = new CourseReceiver();
        IntentFilter filter = new IntentFilter(ApiConstant.COURSE_ACTION);
        filter.setPriority(999);
        registerReceiver(receiver,filter);

        initView();
        initEvent();
        presenter.init();
    }

    private void initEvent() {
        btMore.setOnClickListener(v ->
                presenter.openSelectAvatarDialog());

        btSend.setOnClickListener(v -> {
            presenter.sendMessage(input.getText().toString());
            input.setText("");
        });

        setTopLeftButton(this::finish);

        int screenHeight = Integer.valueOf(getValueFromSharedPreferences(this,SCREEN_HEIGHT));
        mIsSoftKeyboardShowing = false;
        //注册布局变化监听
        getWindow().getDecorView().getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            //判断窗口可见区域大小
            Rect r = new Rect();
            getWindow().getDecorView().getWindowVisibleDisplayFrame(r);
            //如果屏幕高度和Window可见区域高度差值大于整个屏幕高度的1/3，则表示软键盘显示中，否则软键盘为隐藏状态。
            int heightDifference = screenHeight - r.bottom;
            boolean isKeyboardShowing = heightDifference > screenHeight/4;

            if(isKeyboardShowing && !mIsSoftKeyboardShowing){
                mIsSoftKeyboardShowing = true;
                showKeyboard(heightDifference);
            }else if(!isKeyboardShowing && mIsSoftKeyboardShowing){
                mIsSoftKeyboardShowing = false;
                hideKeyboard();
            }
        });

        receiver.setOnNewMessageListener(data -> {
            Log.i(TAG, "onNewMessage: getMessage " + data.getContent());
            presenter.handleMessage(data);
        });
    }

    private void initView() {
        String courseName = getIntent().getStringExtra(ApiConstant.COURSE_NAME);
        setTitle(courseName);
        manager = new LinearLayoutManager(this);
        adapter = new ChatAdapter(null,this);
        recyclerView.setLayoutManager(manager);
        chatList = new ArrayList<>();
        adapter.setNewData(chatList);
        recyclerView.setAdapter(adapter);
    }

    int keyboardHeight = 0;
    private void showKeyboard(int keyboardHeight){
        Log.i(TAG, "showKeyboard: keyboardHeight height = "+keyboardHeight);
        this.keyboardHeight = keyboardHeight;
        ViewGroup.LayoutParams params= layout.getLayoutParams();
        params.height = layout.getHeight()-keyboardHeight;
        layout.setLayoutParams(params);
    }

    private void hideKeyboard(){
        ViewGroup.LayoutParams params= layout.getLayoutParams();
        params.height = layout.getHeight() + keyboardHeight;
        layout.setLayoutParams(params);
    }

    @Override
    public void startActivity(Class activity,String data) {
        Intent intent = new Intent(this,activity);
        intent.putExtra(COURSE_ID,courseID);
        startActivity(intent);
    }

    @Override
    public void getMessage(ChatBean message) {
        chatList.add(message);
        adapter.notifyDataSetChanged();
        recyclerView.smoothScrollToPosition(chatList.size()-1);
    }

    @Override
    public void getHistoryMessage(List<ChatBean> list) {
        chatList.addAll(list);
        adapter.notifyDataSetChanged();
        Log.i(TAG, "getHistoryMessage: list size  = "+list.size());
        if(chatList.size()>0)
            recyclerView.smoothScrollToPosition(chatList.size()-1);
    }

    @Override
    public void errorMessage(String error) {

    }

    @Override
    public void tokenError() {
        showTokenErrorDialog("异地登陆");
    }

    /*----------------------------------------------------------------------------------------------*/

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }
}
