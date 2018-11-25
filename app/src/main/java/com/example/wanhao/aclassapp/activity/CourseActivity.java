package com.example.wanhao.aclassapp.activity;

import android.graphics.Rect;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.wanhao.aclassapp.R;
import com.example.wanhao.aclassapp.adapter.ChatAdapter;
import com.example.wanhao.aclassapp.base.TopBarBaseActivity;
import com.example.wanhao.aclassapp.config.ApiConstant;
import com.example.wanhao.aclassapp.db.ChatDB;
import com.example.wanhao.aclassapp.db.CourseDB;
import com.example.wanhao.aclassapp.presenter.CoursePresenter;
import com.example.wanhao.aclassapp.view.CourseView;

import java.util.List;

import butterknife.BindView;

import static com.example.wanhao.aclassapp.config.Constant.SCREEN_HEIGHT;
import static com.example.wanhao.aclassapp.util.SaveDataUtil.getValueFromSharedPreferences;

public class CourseActivity extends TopBarBaseActivity<CoursePresenter> implements CourseView {
    private static final String TAG = "CourseActivity";

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

    ChatAdapter adapter;

    boolean mIsSoftKeyboardShowing = false;

    @Override
    protected int getContentView() {
        return R.layout.activity_course;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        initView();
        initEvent();

        String courseID = getIntent().getStringExtra(ApiConstant.COURSE_ID);
        presenter.init(courseID);

        new Handler().postDelayed(() -> recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (!recyclerView.canScrollVertically(-1)) {        //canScrollVertically
                    //onScrolledToTop();
                    Log.i(TAG, "UpFetch: tttttttttttttttttttttop");
                    presenter.loadMore();
                } else if (!recyclerView.canScrollVertically(1)) {
                    //onScrolledToBottom();
                    Log.i(TAG, "UpFetch: bbbbbbbbbbbbbbbbbbbottm");
                }
            }
        }),1000);
    }

    private void initView() {
        adapter = new ChatAdapter(null,this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void initEvent() {
        setTopLeftButton(this::finish);

        btMore.setOnClickListener(v -> presenter.openSelectAvatarDialog());

        btSend.setOnClickListener(v -> {
            presenter.sendMessage(input.getText().toString());
            input.setText("");
        });

        int screenHeight = Integer.valueOf(getValueFromSharedPreferences(this,SCREEN_HEIGHT));
        mIsSoftKeyboardShowing = false;
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
    public void showProgress() {

    }

    @Override
    public void dismissProgress() {

    }

    @Override
    public void loadDataSuccess(List<ChatDB> data) {
        adapter.setNewData(data);
        if(data.size()>0)
            recyclerView.smoothScrollToPosition(adapter.getData().size()-1);
    }

    @Override
    public void errorMessage(String error) {
        adapter.setUpFetching(false);
    }

    @Override
    public void notifyDataChange(boolean isMove) {
        adapter.notifyDataSetChanged();
        if(isMove){
            recyclerView.smoothScrollToPosition(adapter.getData().size()-1);
        }
    }

    @Override
    public void tokenError() {
        showTokenErrorDialog("异地登陆");
    }

    @Override
    public void initView(CourseDB course) {
        setTitle(course.getName());
    }

    @Override
    protected CoursePresenter setPresenter() {
        return new CoursePresenter(this,this);
    }


}
