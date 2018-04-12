package com.example.wanhao.aclassapp.fragment;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.widget.Toast;

import com.example.wanhao.aclassapp.R;
import com.example.wanhao.aclassapp.adapter.ChatAdapter;
import com.example.wanhao.aclassapp.base.LazyLoadFragment;
import com.example.wanhao.aclassapp.bean.ChatBean;
import com.example.wanhao.aclassapp.config.ApiConstant;
import com.example.wanhao.aclassapp.presenter.ChatPresenter;
import com.example.wanhao.aclassapp.view.ChatView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by wanhao on 2018/2/27.
 */

public class MainFragment extends LazyLoadFragment implements ChatView{
    private static final String TAG = "MainFragment";

    @BindView(R.id.fg_main_recycler)
    RecyclerView recyclerView;

    private List<ChatBean> list;
    private ChatAdapter adapter;

    private String courseID;
    private ChatPresenter presenter;

    @Override
    protected int setContentView() {
        return R.layout.framgnet_main;
    }

    @Override
    protected void lazyLoad() {
        courseID = getArguments().getString(ApiConstant.COURSE_ID);
        presenter = new ChatPresenter(getContext(),this,courseID);
        initData();
        initView();
        presenter.sendMessage("this is message");
    }

    private void initData() {
        list = new ArrayList<>();

        for(int x=0;x<20;x++){
            ChatBean bean = new ChatBean();
            bean.setContent("在吗？在忙吗？你好啊？嗯哼？");
            bean.setName("万浩的账号");
            bean.setHeadImage(R.drawable.bck);
            bean.setType(x%2+1);
            list.add(bean);
        }



    }

    private void initView() {
        adapter = new ChatAdapter(list);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setAdapter(adapter);

    }

    @Override
    public void error(String message) {
        Toast.makeText(getContext(),message,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showNewMessage(String message) {
        ChatBean bean = new ChatBean();
        bean.setType(ChatBean.OTHER);
        bean.setName("其他人");
        bean.setHeadImage(R.drawable.bck);
        bean.setContent(message);
        list.add(bean);
        adapter.setNewData(list);
    }
}
