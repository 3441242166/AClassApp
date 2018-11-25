package com.example.wanhao.aclassapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.acker.simplezxing.activity.CaptureActivity;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.wanhao.aclassapp.R;
import com.example.wanhao.aclassapp.base.TopBarBaseActivity;
import com.example.wanhao.aclassapp.config.ApiConstant;
import com.example.wanhao.aclassapp.presenter.AddCoursePresenter;
import com.example.wanhao.aclassapp.util.DialogUtil;
import com.example.wanhao.aclassapp.view.IAddCourseView;

import butterknife.BindView;

import static com.example.wanhao.aclassapp.config.ApiConstant.RESULT_ADD;

public class AddCourseActivity extends TopBarBaseActivity<AddCoursePresenter> implements View.OnClickListener, IAddCourseView {

    @BindView(R.id.ac_choose_fab)
    FloatingActionButton floatingActionButton;
    @BindView(R.id.ac_choose_add)
    Button abtAdd;
    @BindView(R.id.ac_choose_ed)
    EditText editText;

    MaterialDialog dialog;

    @Override
    protected int getContentView() {
        return R.layout.activity_add_course;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        setTopLeftButton(this::finish);
        setTitle("添加课程");
        dialog = DialogUtil.waitDialog(this);


        abtAdd.setOnClickListener(this);
        floatingActionButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.ac_choose_add:
                presenter.add(editText.getText().toString());
                break;
            case R.id.ac_choose_fab:
                //startActivityForResult(new Intent(this, CaptureActivity.class), CaptureActivity.REQ_CODE);
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
                        presenter.add(data.getStringExtra(CaptureActivity.EXTRA_SCAN_RESULT));
                        break;
                    case RESULT_CANCELED:
                        if (data != null) {
                            editText.setText(data.getStringExtra(CaptureActivity.EXTRA_SCAN_RESULT));
                            presenter.add(data.getStringExtra(CaptureActivity.EXTRA_SCAN_RESULT));
                        }
                        break;
                }
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
    public void loadDataSuccess(String data) {
        Toast.makeText(this,data, Toast.LENGTH_SHORT).show();

        Intent intent = new Intent();
        intent.putExtra(RESULT_ADD, ApiConstant.ADD_SUCCESS);
        setResult(ApiConstant.ADD_SUCCESS, intent);
        finish();
    }

    @Override
    public void errorMessage(String throwable) {
        Toast.makeText(this,throwable, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void tokenError(String msg) {
        showTokenErrorDialog(msg);
    }

    @Override
    protected AddCoursePresenter setPresenter() {
        return new AddCoursePresenter(this,this);
    }
}
