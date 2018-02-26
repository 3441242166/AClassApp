package com.example.wanhao.aclassapp.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.acker.simplezxing.activity.CaptureActivity;
import com.example.wanhao.aclassapp.R;
import com.example.wanhao.aclassapp.base.TopBarBaseActivity;
import com.example.wanhao.aclassapp.config.ApiConstant;
import com.example.wanhao.aclassapp.presenter.AddCoursePresenter;
import com.example.wanhao.aclassapp.view.IAddCourseView;

import butterknife.BindView;
import cn.pedant.SweetAlert.SweetAlertDialog;


public class AddCourseActivity extends TopBarBaseActivity implements View.OnClickListener, IAddCourseView {

    AddCoursePresenter mPresenter;

    @BindView(R.id.ac_choose_fab)
    FloatingActionButton floatingActionButton;
    @BindView(R.id.ac_choose_add)
    Button abtAdd;
    @BindView(R.id.ac_choose_ed)
    EditText editText;

    private SweetAlertDialog pDialog;
    private boolean isSucess = false;

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.ac_choose_add:
                mPresenter.add(editText.getText().toString());
                break;
            case R.id.ac_choose_fab:
                startActivityForResult(new Intent(this, CaptureActivity.class), CaptureActivity.REQ_CODE);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CaptureActivity.REQ_CODE:
                switch (resultCode) {
                    case RESULT_OK:
                        editText.setText(data.getStringExtra(CaptureActivity.EXTRA_SCAN_RESULT));
                        mPresenter.add(data.getStringExtra(CaptureActivity.EXTRA_SCAN_RESULT));
                        break;
                    case RESULT_CANCELED:
                        if (data != null) {
                            editText.setText(data.getStringExtra(CaptureActivity.EXTRA_SCAN_RESULT));
                            mPresenter.add(data.getStringExtra(CaptureActivity.EXTRA_SCAN_RESULT));
                        }
                        break;
                }
                break;
        }
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_add_course;
    }

    @Override
    protected void init(Bundle savedInstanceState) {

        pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("添加中...");
        pDialog.setCancelable(false);

        mPresenter = new AddCoursePresenter(this,this);

        abtAdd.setOnClickListener(this);
        floatingActionButton.setOnClickListener(this);

        setTopLeftButton(new OnClickListener() {
            @Override
            public void onClick() {
                AddCourseActivity.this.finish();
            }
        });

        setTitle("添加课程");
    }

    @Override
    public void showProgress() {
        pDialog.show();
    }

    @Override
    public void disimissProgress() {
        pDialog.hide();
    }

    @Override
    public void loadDataSuccess(Object tData) {
        Toast.makeText(this,"success", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent();
        intent.putExtra("result", ApiConstant.ADD_SUCCESS);
        setResult(ApiConstant.ADD_SUCCESS, intent);
        finish();
    }

    @Override
    public void loadDataError(String throwable) {
        Toast.makeText(this,throwable, Toast.LENGTH_SHORT).show();
    }
}
