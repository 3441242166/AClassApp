package com.example.wanhao.aclassapp.fragment;

import com.example.wanhao.aclassapp.R;
import com.example.wanhao.aclassapp.base.IBasePresenter;
import com.example.wanhao.aclassapp.base.LazyLoadFragment;

public class TestFragment extends LazyLoadFragment {
    @Override
    protected IBasePresenter setPresenter() {
        return null;
    }

    @Override
    protected int setContentView() {
        return R.layout.fragment_test;
    }

    @Override
    protected void lazyLoad() {

    }
}
