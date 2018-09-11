package com.example.wanhao.aclassapp.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.wanhao.aclassapp.R;
import com.example.wanhao.aclassapp.bean.Assignment;

import java.util.List;

public class AssignmentAdapter extends BaseQuickAdapter<Assignment,BaseViewHolder> {

    public AssignmentAdapter(@Nullable List<Assignment> data) {
        super(R.layout.item_assignment, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Assignment item) {

        helper.setText(R.id.ac_assign_title,item.getTitle());
        helper.setText(R.id.ac_assign_b_date, item.getcDate());
        helper.setText(R.id.ac_assign_e_date,item.geteDate());
        helper.setText(R.id.ac_assign_content, item.getContent());
        helper.setText(R.id.ac_assign_man,item.getPostMan());

    }
}

