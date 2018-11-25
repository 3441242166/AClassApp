package com.example.wanhao.aclassapp.activity;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
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
import com.example.wanhao.aclassapp.base.BaseMvpActivity;
import com.example.wanhao.aclassapp.config.ApiConstant;
import com.example.wanhao.aclassapp.db.CourseDB;
import com.example.wanhao.aclassapp.presenter.LodingPresenter;
import com.example.wanhao.aclassapp.util.DialogUtil;
import com.example.wanhao.aclassapp.view.ILodingView;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.wanhao.aclassapp.config.Constant.REGISTER_CODE;
import static com.example.wanhao.aclassapp.config.Constant.RESULT_SUCESS;

public class LodingActivity extends BaseMvpActivity<LodingPresenter> implements View.OnClickListener,ILodingView {
    private static final String TAG = "LodingActivity";

    @BindView(R.id.ac_loding_forget) TextView forget;
    @BindView(R.id.ac_loding_loding) Button btGo;
    @BindView(R.id.ac_loding_fab) FloatingActionButton fab;
    @BindView(R.id.ac_loding_count) EditText etUsername;
    @BindView(R.id.ac_loding_password) EditText etPassword;
    @BindView(R.id.ac_loding_cardview) CardView cardView;

    private MaterialDialog dialog;

    @Override
    protected LodingPresenter setPresenter() {
        return new LodingPresenter(this,this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loding);
        ButterKnife.bind(this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        initView();
        presenter.init();
    }

    private void initView() {

        dialog = DialogUtil.waitDialog(this);

        fab.setOnClickListener(this);
        btGo.setOnClickListener(this);
        forget.setOnClickListener(this);

    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ac_loding_fab:
                Intent intent = new Intent(this, RegisterActivity.class);
                startActivityForResult(intent,REGISTER_CODE, ActivityOptions.makeSceneTransitionAnimation(this,
                        fab, fab.getTransitionName()).toBundle());
                break;
            case R.id.ac_loding_loding:
                hideKeyboard();
                presenter.login(etUsername.getText().toString(),etPassword.getText().toString());
                //startActivity(new Intent(this, MainActivity.class));
                break;
            case R.id.ac_loding_forget:

                break;
        }
    }

    @Override
    public void showProgress() {
        dialog.show();
    }

    @Override
    public void dismissProgress() {
        dialog.dismiss();
    }

    @Override
    public void loadDataSuccess(String tData) {
        startActivity(new Intent(this, MainActivity.class));
        Toast.makeText(this,tData, Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void errorMessage(String throwable) {
        Toast.makeText(this,throwable, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void initData(String count, String password) {
        etPassword.setText(password);
        etUsername.setText(count);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REGISTER_CODE){

            if(resultCode == RESULT_SUCESS){
                String count = data.getStringExtra(ApiConstant.USER_COUNT);
                String password = data.getStringExtra(ApiConstant.USER_PASSWORD);
                presenter.login(count,password);
            }

        }

    }

    private void hideKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) Objects.requireNonNull(getSystemService(Context.INPUT_METHOD_SERVICE))).
                    hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

}

