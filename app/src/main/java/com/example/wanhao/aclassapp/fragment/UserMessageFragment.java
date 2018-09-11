package com.example.wanhao.aclassapp.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.support.v7.app.AlertDialog;
import android.widget.EditText;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.wanhao.aclassapp.R;
import com.example.wanhao.aclassapp.activity.UserMessageActivity;
import com.example.wanhao.aclassapp.base.LazyLoadFragment;
import com.example.wanhao.aclassapp.bean.User;
import com.example.wanhao.aclassapp.presenter.UserMessagePresenter;
import com.example.wanhao.aclassapp.util.DialogUtil;
import com.example.wanhao.aclassapp.view.IUserMessageView;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;

public class UserMessageFragment extends LazyLoadFragment{
    private static final String TAG = "UserMessageFragment";


    @BindView(R.id.ac_user_head)
    CircleImageView imageView;
    @BindView(R.id.ac_user_name)
    TextView name;
    @BindView(R.id.ac_user_signature)
    TextView signature;



    @Override
    protected int setContentView() {
        return R.layout.activity_user_message;
    }

    @Override
    protected void lazyLoad() {

    }

}
