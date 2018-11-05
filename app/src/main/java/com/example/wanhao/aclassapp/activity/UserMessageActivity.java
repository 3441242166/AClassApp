package com.example.wanhao.aclassapp.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.wanhao.aclassapp.R;
import com.example.wanhao.aclassapp.adapter.SettingAdapter;
import com.example.wanhao.aclassapp.base.BaseTokenActivity;
import com.example.wanhao.aclassapp.bean.User;
import com.example.wanhao.aclassapp.config.ApiConstant;
import com.example.wanhao.aclassapp.presenter.UserMessagePresenter;
import com.example.wanhao.aclassapp.util.ColorDividerItemDecoration;
import com.example.wanhao.aclassapp.util.DialogUtil;
import com.example.wanhao.aclassapp.util.FileConvertUtil;
import com.example.wanhao.aclassapp.view.IUserMessageView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UserMessageActivity extends BaseTokenActivity implements IUserMessageView{
    @BindView(R.id.ac_usermessage_head)
    ImageView head;
    @BindView(R.id.ac_usermessage_name)
    TextView name;
    @BindView(R.id.ac_usermessage_first)
    RecyclerView mainRecycler;
    @BindView(R.id.ac_usermessage_secand)
    RecyclerView otherRecycler;
    @BindView(R.id.ac_usermessage_toolbar)
    Toolbar toolbar;
    @BindView(R.id.ac_usermessage_scroll)
    NestedScrollView scrollView;

    private static final String[] MAIN_TITLE = {"账号", "个性签名", "昵称", "身份"};
    private static final String[] OTHER_TITLE = {"学号", "学校", "性别", "邮箱",
            "地区", "EmptyView", "DragAndSwipe", "ItemClick",
            "ExpandableItem", "DataBinding", "UpFetchData"};

    private ArrayList<SettingAdapter.SettingBean> mainList;
    private ArrayList<SettingAdapter.SettingBean> otherList;
    private SettingAdapter mainAdapter;
    private SettingAdapter otherAdapter;

    private UserMessagePresenter presenter;

    MaterialDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usermessage);
        ButterKnife.bind(this);
        presenter = new UserMessagePresenter(this, this);

        dialog = DialogUtil.waitDialog(this);

        initData();
        initView();
        initEvent();
        presenter.init();
    }

    private void initData() {
        mainList = new ArrayList<>();
        otherList = new ArrayList<>();

        for (String title : MAIN_TITLE) {
            SettingAdapter.SettingBean bean = new SettingAdapter.SettingBean(title);
            bean.content = title;
            mainList.add(bean);
        }

        for (String title : OTHER_TITLE) {
            SettingAdapter.SettingBean bean = new SettingAdapter.SettingBean(title);
            bean.content = title;
            otherList.add(bean);
        }
    }

    private void initView() {
        mainRecycler.setLayoutManager(new LinearLayoutManager(this));
        otherRecycler.setLayoutManager(new LinearLayoutManager(this));

        mainRecycler.setNestedScrollingEnabled(false);
        otherRecycler.setNestedScrollingEnabled(false);

        mainRecycler.addItemDecoration(new ColorDividerItemDecoration());
        otherRecycler.addItemDecoration(new ColorDividerItemDecoration());

        mainAdapter = new SettingAdapter(mainList,this);
        otherAdapter = new SettingAdapter(otherList,this);

        mainRecycler.setAdapter(mainAdapter);
        otherRecycler.setAdapter(otherAdapter);
    }

    private void initEvent(){

        name.setOnClickListener(view -> {

        });

        head.setOnClickListener(view->{
                presenter.openSelectAvatarDialog();
            }
        );

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_usermessage, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
        }
        else if (item.getItemId() == R.id.menu_1){

        }else {

        }

        return true; // true 告诉系统我们自己处理了点击事件
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
    public void loadDataSuccess(User tData) {

    }

    @Override
    public void loadDataError(String throwable) {

    }

    @Override
    public void changeUserSuccess() {

    }

    @Override
    public void showImage(Bitmap bitmap) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case ApiConstant.CAMERA_CODE:
                //用户点击了取消
                if (data == null) {
                    return;
                } else {
                    Bundle extras = data.getExtras();
                    if (extras != null) {
                        //获得拍的照片
                        Bitmap bitmap = extras.getParcelable("data");
                        head.setImageBitmap(bitmap);
                        Uri uri = FileConvertUtil.saveBitmapToLocal(ApiConstant.USER_AVATAR_NAME,bitmap);
                        presenter.onSelectImage(uri);
                    }
                }
                break;
            case ApiConstant.GALLERY_CODE:
                if (data == null) {
                    return;
                } else {
                    //获取到用户所选图片的Uri
                    Uri uri = data.getData();
                    presenter.onSelectImage(uri);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void tokenError(String msg) {
        showTokenErrorDialog(msg);
    }

}
