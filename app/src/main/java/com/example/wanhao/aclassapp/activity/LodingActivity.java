package com.example.wanhao.aclassapp.activity;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.wanhao.aclassapp.R;
import com.example.wanhao.aclassapp.presenter.LodingPresenter;
import com.example.wanhao.aclassapp.view.ILodingView;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.ProgressHelper;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class LodingActivity extends AppCompatActivity implements View.OnClickListener,ILodingView {
    private static final String TAG = "LodingActivity";

    @BindView(R.id.ac_loding_forget) TextView forget;
    @BindView(R.id.ac_loding_loding) Button btGo;
    @BindView(R.id.ac_loding_fab) FloatingActionButton fab;
    @BindView(R.id.ac_loding_count) EditText etUsername;
    @BindView(R.id.ac_loding_password) EditText etPassword;
    @BindView(R.id.ac_loding_cardview) CardView cardView;

    MaterialDialog dialog;

    LodingPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loding);
        ButterKnife.bind(this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        initView();
        presenter.init();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ac_loding_fab:
                startActivity(new Intent(this, RegisterActivity.class), ActivityOptions.makeSceneTransitionAnimation(this,
                        fab, fab.getTransitionName()).toBundle());
                break;
            case R.id.ac_loding_loding:
                hideKeyboard();
                presenter.login(etUsername.getText().toString(),etPassword.getText().toString());
                break;
            case R.id.ac_loding_forget:

                break;
        }
    }

    private void initView() {

        MaterialDialog.Builder builder = new MaterialDialog.Builder(this)
                .title("Zzz...")
                .content("加载中...")
                .progress(true,100,false);

        dialog = builder.build();

        fab.setOnClickListener(this);
        btGo.setOnClickListener(this);
        forget.setOnClickListener(this);

        presenter = new LodingPresenter(this,this);
    }

    @Override
    public void showProgress() {
        dialog.show();
    }

    @Override
    public void disimissProgress() {
        dialog.dismiss();
    }

    @Override
    public void loadDataSuccess(String tData) {
        startActivity(new Intent(this, CourseActivity.class));
        Toast.makeText(this,"success", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void loadDataError(String throwable) {
        Toast.makeText(this,throwable, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void initData(String count, String password) {
        etPassword.setText(password);
        etUsername.setText(count);
    }

    private void hideKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).
                    hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

}

