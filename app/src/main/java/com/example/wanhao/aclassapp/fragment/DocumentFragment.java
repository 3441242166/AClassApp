package com.example.wanhao.aclassapp.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.example.wanhao.aclassapp.R;
import com.example.wanhao.aclassapp.adapter.PagerAdapter;
import com.example.wanhao.aclassapp.base.LazyLoadFragment;
import com.example.wanhao.aclassapp.config.ApiConstant;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by wanhao on 2018/2/27.
 */

public class DocumentFragment extends LazyLoadFragment {
    private static final String TAG = "DocumentFragment";

    @BindView(R.id.fg_document_tab)
    TabLayout tableLayout;
    @BindView(R.id.fg_document_pager)
    ViewPager viewPager;

    private String courseID;
    List<Fragment> list = new ArrayList<>();

    @Override
    protected int setContentView() {
        return R.layout.framgnet_document;
    }

    @Override
    protected void lazyLoad() {
        courseID = getArguments().getString(ApiConstant.COURSE_ID);

        DocumentListFragment fragment = new DocumentListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ApiConstant.COURSE_ID,courseID);
        bundle.putString(ApiConstant.DOCUMENT_TYPE,ApiConstant.DOCUMENT_PREVIEW);
        fragment.setArguments(bundle);

        list.add(fragment);
        fragment = new DocumentListFragment();
        bundle = new Bundle();
        bundle.putString(ApiConstant.COURSE_ID,courseID);
        bundle.putString(ApiConstant.DOCUMENT_TYPE,ApiConstant.DOCUMENT_EDATA);
        fragment.setArguments(bundle);
        list.add(fragment);

        viewPager.setAdapter(new PagerAdapter(getActivity().getSupportFragmentManager(),list));
        tableLayout.setupWithViewPager(viewPager);

        initView();
        initEvent();

    }

    private void initView() {



    }

    private void initEvent(){

    }

}
