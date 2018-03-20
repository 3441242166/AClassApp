package com.example.wanhao.aclassapp.fragment;

import com.example.wanhao.aclassapp.R;
import com.example.wanhao.aclassapp.base.LazyLoadFragment;
import com.example.wanhao.aclassapp.config.ApiConstant;

/**
 * Created by wanhao on 2018/2/27.
 */

public class MainFragment extends LazyLoadFragment {

    private String courseID;

    @Override
    protected int setContentView() {
        return R.layout.framgnet_main;
    }

    @Override
    protected void lazyLoad() {
        courseID = getArguments().getString(ApiConstant.COURSE_ID);
    }
}
