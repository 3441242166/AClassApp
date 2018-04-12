package com.example.wanhao.aclassapp.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.wanhao.aclassapp.R;
import com.example.wanhao.aclassapp.bean.Question;

import java.util.List;

/**
 * Created by wanhao on 2018/4/9.
 */

public class QuestionAdapter extends BaseQuickAdapter<Question,BaseViewHolder> {

    public QuestionAdapter(@Nullable List<Question> data) {
        super(R.layout.item_qustion, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Question item) {

        helper.setText(R.id.item_qustion_title,item.getTitle());
        helper.setText(R.id.item_qustion_a, item.getA());
        helper.setText(R.id.item_qustion_b,item.getB());
        helper.setText(R.id.item_qustion_c, item.getC());
        helper.setText(R.id.item_qustion_d,item.getD());

    }
}
