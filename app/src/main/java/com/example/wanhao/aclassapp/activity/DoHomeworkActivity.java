package com.example.wanhao.aclassapp.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.wanhao.aclassapp.R;
import com.example.wanhao.aclassapp.adapter.QuestionAdapter;
import com.example.wanhao.aclassapp.base.TopBarBaseActivity;
import com.example.wanhao.aclassapp.bean.Question;
import com.example.wanhao.aclassapp.util.PagingScrollHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class DoHomeworkActivity extends TopBarBaseActivity {
    private static final String TAG = "DoHomeworkActivity";

    @BindView(R.id.ac_dohome_recycler)
    RecyclerView recyclerView;

    private List<Question> list;
    private QuestionAdapter adapter;

    @Override
    protected int getContentView() {
        return R.layout.activity_do_homework;
    }

    @Override
    protected void init(Bundle savedInstanceState) {

        initData();
        initView();
        initEvent();

    }

    private void initData() {
        list = new ArrayList<>();
        for(int x=0;x<10;x++){
            Question question = new Question();
            question.setTitle("第"+x+"题");
            question.setA("A");
            question.setB("B");
            question.setC("C");
            question.setD("D");
            list.add(question);
        }
    }

    private void initView() {
        adapter = new QuestionAdapter(list);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setAdapter(adapter);

        PagingScrollHelper scrollHelper = new PagingScrollHelper();
        scrollHelper.setUpRecycleView(recyclerView);
    }

    private void initEvent() {
        setTopLeftButton(new OnClickListener() {
            @Override
            public void onClick() {
                finish();
            }
        });
    }
}
