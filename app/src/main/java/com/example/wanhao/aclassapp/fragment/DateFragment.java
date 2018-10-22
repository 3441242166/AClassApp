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

    private static final String[] OTHER_TITLE = {"Animation", "MultipleItem", "Header/Footer", "PullToRefresh", "Section", "设置", "退出登陆", "ItemClick"};
    private static final int[] OTHER_IMG = {R.mipmap.gv_animation, R.mipmap.gv_multipleltem, R.mipmap.gv_header_and_footer, R.mipmap.gv_pulltorefresh, R.mipmap.gv_section, R.mipmap.gv_empty, R.mipmap.gv_drag_and_swipe, R.mipmap.gv_item_click};


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
        for(int x=0;x<OTHER_TITLE.length;x++){
            menuList.add(new GridBean(OTHER_IMG[x],OTHER_TITLE[x]));
        }
        menuRecycler.setLayoutManager(new GridLayoutManager(getContext(), 4));
        menuAdapter = new GridAdapter(menuList,getContext());
        menuRecycler.setAdapter(menuAdapter);
    }
}
