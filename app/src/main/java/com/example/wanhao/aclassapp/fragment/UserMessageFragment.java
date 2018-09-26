package com.example.wanhao.aclassapp.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.example.wanhao.aclassapp.R;
import com.example.wanhao.aclassapp.activity.UserMessageActivity;
import com.example.wanhao.aclassapp.adapter.GridAdapter;
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

public class UserMessageFragment extends LazyLoadFragment implements IUserMessageFgView{
    private static final String TAG = "UserMessageFragment";

    private static final String[] USUALLY_TITLE = {"Animation", "MultipleItem", "Header/Footer", "PullToRefresh"};
    private static final int[] USUALLY_IMG = {R.mipmap.gv_animation, R.mipmap.gv_multipleltem, R.mipmap.gv_header_and_footer, R.mipmap.gv_pulltorefresh};

    private static final String[] OTHER_TITLE = {"Animation", "MultipleItem", "Header/Footer", "PullToRefresh", "Section", "EmptyView", "DragAndSwipe", "ItemClick", "ExpandableItem", "DataBinding", "UpFetchData"};
    private static final int[] OTHER_IMG = {R.mipmap.gv_animation, R.mipmap.gv_multipleltem, R.mipmap.gv_header_and_footer, R.mipmap.gv_pulltorefresh, R.mipmap.gv_section, R.mipmap.gv_empty, R.mipmap.gv_drag_and_swipe, R.mipmap.gv_item_click, R.mipmap.gv_expandable, R.mipmap.gv_databinding,R.mipmap.gv_multipleltem};

    private ArrayList<GridBean> usuallyList;
    private ArrayList<GridBean> otherList;
    private GridAdapter usuallyAdapter;
    private GridAdapter otherAdapter;

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
    protected int setContentView() {
        return R.layout.fragment_my;
    }

    @Override
    protected void lazyLoad() {
        initData();
        init();
        presenter.init();
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
        presenter = new UserMessageFgPresenter(getContext(),this);
        usuallyRecycler.setLayoutManager(new GridLayoutManager(getContext(), 4));
        otherRecycler.setLayoutManager(new GridLayoutManager(getContext(), 4));

        usuallyRecycler.setNestedScrollingEnabled(false);
        otherRecycler.setNestedScrollingEnabled(false);

        usuallyAdapter = new GridAdapter(usuallyList,getContext());
        otherAdapter = new GridAdapter(otherList,getContext());

        usuallyRecycler.setAdapter(usuallyAdapter);
        otherRecycler.setAdapter(otherAdapter);
    }

    private void initEvent() {
        headLayout.setOnClickListener(view->{
            startActivity(new Intent(getActivity(), UserMessageActivity.class));
        });
    }

    @Override
    public void setUserMessage(User userMessage) {
        name.setText(userMessage.getNickName());
        signature.setText(userMessage.getSignature());
    }

    @Override
    public void setUserHead(GlideUrl bitmap) {
        Glide.with(this)
                .load(bitmap)
                .into(head);
    }

}
