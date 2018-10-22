package com.example.wanhao.aclassapp.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.wanhao.aclassapp.R;
import com.example.wanhao.aclassapp.bean.Homework;


import java.util.List;

public class HomeworkAdapter extends BaseQuickAdapter<Homework,BaseViewHolder> {

    public HomeworkAdapter(@Nullable List<Homework> data) {
        super(R.layout.item_homework, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Homework item) {

        helper.setText(R.id.item_homework_title,item.getTitle());
        helper.setText(R.id.item_homework_date, item.getDate());

    }
}
