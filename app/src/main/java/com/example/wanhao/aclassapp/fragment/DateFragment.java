package com.example.wanhao.aclassapp.fragment;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.wanhao.aclassapp.R;
import com.example.wanhao.aclassapp.adapter.GridAdapter;
import com.example.wanhao.aclassapp.base.LazyLoadFragment;
import com.example.wanhao.aclassapp.bean.GridBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class DateFragment extends LazyLoadFragment{

    @BindView(R.id.fg_document_menu)
    RecyclerView menuRecycler;
    @BindView(R.id.fg_document_collect)
    RecyclerView typeRecycler;

    GridAdapter menuAdapter;

    private static String[] title = {};
    private static int[] imgs={};

    @Override
    protected int setContentView() {
        return R.layout.fragment_date;
    }

    @Override
    protected void lazyLoad() {
        initView();
    }

    private void initView() {
        List<GridBean> menuList = new ArrayList<>();
        for(int x=0;x<title.length;x++){
            menuList.add(new GridBean(imgs[x],title[x]));
        }
        menuRecycler.setLayoutManager(new GridLayoutManager(getContext(), 3));
        menuAdapter = new GridAdapter(menuList,getContext());
        menuRecycler.setAdapter(menuAdapter);
    }
}
