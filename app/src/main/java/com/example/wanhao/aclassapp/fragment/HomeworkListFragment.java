package com.example.wanhao.aclassapp.fragment;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.example.wanhao.aclassapp.adapter.HomeworkAdapter;
import com.example.wanhao.aclassapp.base.LazyLoadFragment;
import com.example.wanhao.aclassapp.bean.Homework;
import com.example.wanhao.aclassapp.util.MyItemDecoration;

import java.util.List;

/**
 * Created by wanhao on 2018/4/12.
 */

public class HomeworkListFragment extends LazyLoadFragment{

    RecyclerView recyclerView;

    private List<Homework> list;
    private HomeworkAdapter adapter;

    @Override
    protected int setContentView() {
        return 0;
    }

    @Override
    protected void lazyLoad() {

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL));
        recyclerView.addItemDecoration(new MyItemDecoration());
        recyclerView.setAdapter(adapter);

    }
}
