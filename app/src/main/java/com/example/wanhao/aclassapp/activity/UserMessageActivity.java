package com.example.wanhao.aclassapp.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.wanhao.aclassapp.R;
import com.example.wanhao.aclassapp.base.BaseApplication;
import com.example.wanhao.aclassapp.base.TopBarBaseActivity;
import com.example.wanhao.aclassapp.bean.User;
import com.example.wanhao.aclassapp.config.ApiConstant;
import com.example.wanhao.aclassapp.presenter.UserMessagePresenter;
import com.example.wanhao.aclassapp.util.ActivityCollector;
import com.example.wanhao.aclassapp.util.FileConvertUtil;
import com.example.wanhao.aclassapp.util.SaveDataUtil;
import com.example.wanhao.aclassapp.view.IUserMessageView;

import butterknife.BindView;
import cn.pedant.SweetAlert.ProgressHelper;
import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;

public class UserMessageActivity extends TopBarBaseActivity implements IUserMessageView{
    @BindView(R.id.ac_user_head)
    CircleImageView imageView;
    @BindView(R.id.ac_user_name)
    TextView name;
    @BindView(R.id.ac_user_signature)
    TextView signature;
    @BindView(R.id.ac_user_men)
    RadioButton btMen;
    @BindView(R.id.ac_user_women)
    RadioButton btWomen;
    @BindView(R.id.ac_user_group)
    RadioGroup radioGroup;

    private User user;
    private UserMessagePresenter presenter;

    MaterialDialog dialog;

    boolean isFirst = true;

    @Override
    protected int getContentView() {
        return R.layout.activity_user_message;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        presenter = new UserMessagePresenter(this,this);
        setTitle("我的信息");

        MaterialDialog.Builder builder = new MaterialDialog.Builder(this)
                .title("Zzz...")
                .content("加载中...")
                .progress(true,100,false);

        dialog = builder.build();

        initEvent();
        presenter.getHeadImage();
        presenter.getUserMessage();

    }

    private void initEvent(){
        setTopLeftButton(new OnClickListener() {
            @Override
            public void onClick() {
                UserMessageActivity.this.finish();
            }
        });

        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText editText = new EditText(UserMessageActivity.this);
                AlertDialog.Builder builder = new AlertDialog.Builder(UserMessageActivity.this);
                builder.setTitle("输入你的昵称")
                .setNegativeButton("取消", null)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        user.setNickName(editText.getText().toString());
                        presenter.sentUserMessage(user);
                    }
                });
                builder.setView(editText);
                builder.show();
            }
        });

        signature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText editText = new EditText(UserMessageActivity.this);
                AlertDialog.Builder builder = new AlertDialog.Builder(UserMessageActivity.this);
                builder.setTitle("输入你的个性签名")
                        .setNegativeButton("取消", null)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                user.setSignature(editText.getText().toString());
                                presenter.sentUserMessage(user);
                            }
                        });
                builder.setView(editText);
                builder.show();
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.openSelectAvatarDialog();
            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.ac_user_men:
                        user.setGender("男");
                        break;
                    case R.id.ac_user_women:
                        user.setGender("女");
                        break;
                }
                if(!isFirst) {
                    presenter.sentUserMessage(user);
                }
                isFirst = false;
            }
        });
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
    public void loadDataSuccess(User tData) {
        user = tData;
        SaveDataUtil.saveToSharedPreferences(this,ApiConstant.USER_NAME,tData.getNickName());
        name.setText(tData.getNickName());
        signature.setText(tData.getSignature());
        if(tData.getGender().equals("女")){
            btWomen.setChecked(true);
        }else{
            btMen.setChecked(true);
        }
    }

    @Override
    public void loadDataError(String throwable) {
        Toast.makeText(this,throwable,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void changeUserSucess() {

        Toast.makeText(this,"修改成功",Toast.LENGTH_SHORT).show();
        name.setText(user.getNickName());
        signature.setText(user.getSignature());
        if(user.getGender().equals("女")){
            btWomen.setChecked(true);
        }else{
            btMen.setChecked(true);
        }
    }

    @Override
    public void showImage(Bitmap bitmap) {
        FileConvertUtil.saveBitmapToLocal(ApiConstant.USER_AVATAR_NAME,bitmap);
        imageView.setImageBitmap(bitmap);
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
                        imageView.setImageBitmap(bitmap);
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
        Toast.makeText(BaseApplication.getContext(),"token失效，请重新登陆", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, LodingActivity.class);
        ActivityCollector.finishAll();
        startActivity(intent);
    }

}
