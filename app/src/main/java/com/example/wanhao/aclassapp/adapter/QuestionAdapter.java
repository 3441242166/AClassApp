package com.example.wanhao.aclassapp.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioButton;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.wanhao.aclassapp.R;
import com.example.wanhao.aclassapp.bean.ChatBean;
import com.example.wanhao.aclassapp.bean.Question;

import java.util.List;

/**
 * Created by wanhao on 2018/4/9.
 */

public class QuestionAdapter extends BaseMultiItemQuickAdapter<Question,BaseViewHolder> {

    Context context;

    public QuestionAdapter(@Nullable List<Question> data, Context context) {
        super(data);
        this.context = context;
        addItemType(Question.SIGLE, R.layout.item_qustion);
        addItemType(Question.MORE, R.layout.item_question_more);
    }

    @Override
    protected void convert(BaseViewHolder helper, Question item) {
        helper.setText(R.id.item_qustion_title,item.getQusetion());
        switch (helper.getItemViewType()) {
            case Question.SIGLE:
                helper.setText(R.id.item_qustion_a,item.getAnswers()[0]);
                helper.setText(R.id.item_qustion_b,item.getAnswers()[1]);
                helper.setText(R.id.item_qustion_c,item.getAnswers()[2]);
                helper.setText(R.id.item_qustion_d,item.getAnswers()[3]);

                RadioButton ra = helper.getView(R.id.item_qustion_a);
                ra.setChecked(item.getChooses()[0]);
                ra.setOnClickListener(v -> {
                    item.setAnswer(0);
                });
                RadioButton rb = helper.getView(R.id.item_qustion_b);
                rb.setChecked(item.getChooses()[1]);
                rb.setOnClickListener(v -> {
                    item.setAnswer(1);
                });
                RadioButton rc = helper.getView(R.id.item_qustion_c);
                rc.setChecked(item.getChooses()[2]);
                rc.setOnClickListener(v -> {
                    item.setAnswer(2);
                });
                RadioButton rd = helper.getView(R.id.item_qustion_d);
                rd.setChecked(item.getChooses()[3]);
                rd.setOnClickListener(v -> {
                    item.setAnswer(3);
                });
                break;

            case Question.MORE:
                helper.setText(R.id.item_qustion_a,item.getAnswers()[0]);
                helper.setText(R.id.item_qustion_b,item.getAnswers()[1]);
                helper.setText(R.id.item_qustion_c,item.getAnswers()[2]);
                helper.setText(R.id.item_qustion_d,item.getAnswers()[3]);

                CheckBox ca = helper.getView(R.id.item_qustion_a);
                ca.setChecked(item.getChooses()[0]);
                ca.setOnClickListener(v -> {
                    item.setAnswer(0);
                });
                CheckBox cb = helper.getView(R.id.item_qustion_b);
                cb.setChecked(item.getChooses()[1]);
                cb.setOnClickListener(v -> {
                    item.setAnswer(1);
                });
                CheckBox cc = helper.getView(R.id.item_qustion_c);
                cc.setChecked(item.getChooses()[2]);
                cc.setOnClickListener(v -> {
                    item.setAnswer(2);
                });
                CheckBox cd = helper.getView(R.id.item_qustion_d);
                cd.setChecked(item.getChooses()[3]);
                cd.setOnClickListener(v -> {
                    item.setAnswer(3);
                });
                break;
        }

    }
}
