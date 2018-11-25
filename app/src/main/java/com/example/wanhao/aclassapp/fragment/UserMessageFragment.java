package com.example.wanhao.aclassapp.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.wanhao.aclassapp.R;
import com.example.wanhao.aclassapp.activity.DocumentActivity;
import com.example.wanhao.aclassapp.activity.LodingActivity;
import com.example.wanhao.aclassapp.activity.TestActivity;
import com.example.wanhao.aclassapp.activity.UserMessageActivity;
import com.example.wanhao.aclassapp.adapter.GridAdapter;
import com.example.wanhao.aclassapp.base.BaseTokenActivity;
import com.example.wanhao.aclassapp.base.IBasePresenter;
import com.example.wanhao.aclassapp.base.LazyLoadFragment;
import com.example.wanhao.aclassapp.bean.GridBean;
import com.example.wanhao.aclassapp.bean.User;
import com.example.wanhao.aclassapp.presenter.UserMessageFgPresenter;
import com.example.wanhao.aclassapp.presenter.UserMessagePresenter;
import com.example.wanhao.aclassapp.util.ActivityCollector;
import com.example.wanhao.aclassapp.view.IUserMessageFgView;
import com.example.wanhao.aclassapp.view.IUserMessageView;
import com.jaeger.library.StatusBarUtil;

import java.util.ArrayList;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;

public class UserMessageFragment extends LazyLoadFragment<UserMessageFgPresenter> implements IUserMessageFgView{
    private static final String TAG = "UserMessageFragment";

    private static final String[] USUALLY_TITLE = {"文件", "通知", "作业", "我的收藏"};
    private static final int[] USUALLY_IMG = {R.mipmap.gv_animation, R.mipmap.gv_multipleltem, R.mipmap.gv_header_and_footer, R.mipmap.gv_pulltorefresh};
    private static final Class[] USUALLY_CLASS = {DocumentActivity.class, TestActivity.class, DocumentActivity.class, DocumentActivity.class};

    private static final String[] OTHER_TITLE = {"一", "一", "一", "一", "一", "设置", "退出登陆"};
    private static final int[] OTHER_IMG = {R.mipmap.gv_animation, R.mipmap.gv_multipleltem, R.mipmap.gv_header_and_footer, R.mipmap.gv_pulltorefresh,
            R.mipmap.gv_section, R.mipmap.gv_empty, R.mipmap.gv_drag_and_swipe};
    // 版本检查  退出登录  修改密码

    private ArrayList<GridBean> usuallyList;
    private ArrayList<GridBean> otherList;

    @BindView(R.id.fg_my_bck)
    ImageView bck;
    @BindView(R.id.fg_my_head)
    CircleImageView head;
    @BindView(R.id.fg_my_name)
    TextView name;
    @BindView(R.id.fg_my_signature)
    TextView signature;
    @BindView(R.id.fg_my_usually)
    RecyclerView usuallyRecycler;
    @BindView(R.id.fg_my_other)
    RecyclerView otherRecycler;

    @BindView(R.id.fg_my_headlayout)
    ConstraintLayout headLayout;

    private UserMessageFgPresenter presenter;

    @Override
    protected UserMessageFgPresenter setPresenter() {
        return new UserMessageFgPresenter(getContext(),this);
    }

    @Override
    protected int setContentView() {
        return R.layout.fragment_my;
    }

    @Override
    protected void lazyLoad() {
        presenter = new UserMessageFgPresenter(getContext(),this);
        presenter.init();
        init();
        initEvent();
    }

    private void initData() {
        usuallyList = new ArrayList<>();
        otherList = new ArrayList<>();

        for(int x=0;x<USUALLY_TITLE.length;x++){
            GridBean bean= new GridBean(USUALLY_IMG[x],USUALLY_TITLE[x]);
            usuallyList.add (bean);
        }

        for(int x=0;x<OTHER_TITLE.length;x++){
            GridBean bean= new GridBean(OTHER_IMG[x],OTHER_TITLE[x]);
            otherList.add(bean);
        }
    }

    private void init() {
        initData();
        usuallyRecycler.setLayoutManager(new GridLayoutManager(getContext(), 4));
        otherRecycler.setLayoutManager(new GridLayoutManager(getContext(), 4));

        usuallyRecycler.setNestedScrollingEnabled(false);
        otherRecycler.setNestedScrollingEnabled(false);

        GridAdapter usuallyAdapter = new GridAdapter(usuallyList, getContext());
        GridAdapter otherAdapter = new GridAdapter(otherList, getContext());

        usuallyRecycler.setAdapter(usuallyAdapter);
        otherRecycler.setAdapter(otherAdapter);

        usuallyAdapter.setOnItemClickListener((adapter, view, position) -> startActivity(new Intent(getContext(),USUALLY_CLASS[position])));

        otherAdapter.setOnItemClickListener((adapter, view, position) -> {
            presenter.logout();
        });
    }

    private void initEvent() {
        headLayout.setOnClickListener(view->{
            startActivity(new Intent(getActivity(), UserMessageActivity.class));
        });
    }

    @Override
    public void setUserHead(GlideUrl bitmap) {
        Glide.with(this)
                .load(bitmap)
                .into(head);
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void dismissProgress() {

    }

    @Override
    public void loadDataSuccess(User data) {
        name.setText(data.getNickName());
        signature.setText(data.getSignature());
    }

    @Override
    public void errorMessage(String throwable) {
        Toast.makeText(getContext(),throwable,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void tokenError(String msg) {
        if(getActivity()!=null)
            ((BaseTokenActivity)getActivity()).showTokenErrorDialog(msg);
    }
}
