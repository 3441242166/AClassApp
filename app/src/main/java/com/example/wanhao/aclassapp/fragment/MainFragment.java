package com.example.wanhao.aclassapp.fragment;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
    @BindView(R.id.fg_main_input)
    EditText editText;
    @BindView(R.id.fg_main_send)
    Button send;

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
        list = new ArrayList<>();
        initView();
        initEvent();
    }

    private void initView() {
        adapter = new ChatAdapter(null,getContext());

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setAdapter(adapter);
    }

    public void initEvent(){
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.sendMessage(editText.getText().toString());
            }
        });
    }

    @Override
    public void error(String message) {
        Toast.makeText(getContext(),message,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void newNewMessage(ChatBean message) {
        list.add(message);
        adapter.setNewData(list);
    }

    @Override
    public void getHistoryMessage(List<ChatBean> list) {

    }


}
