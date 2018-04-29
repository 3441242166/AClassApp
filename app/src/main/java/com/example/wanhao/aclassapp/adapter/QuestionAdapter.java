package com.example.wanhao.aclassapp.adapter;

import android.support.annotation.Nullable;
import android.widget.Button;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.wanhao.aclassapp.R;
import com.example.wanhao.aclassapp.bean.Homework;
import com.example.wanhao.aclassapp.bean.Question;

import java.util.List;

/**
 * Created by wanhao on 2018/4/9.
 */

public class QuestionAdapter extends BaseQuickAdapter<Homework,BaseViewHolder> {

    public QuestionAdapter(@Nullable List<Homework> data) {
        super(R.layout.item_qustion, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Homework item) {

        int pos = item.getContent().indexOf('A');
        String question = item.getContent().substring(0,pos);
        String anwser = item.getContent().substring(pos,item.getContent().length());
        String ar[] = anwser.split("：");

        helper.setText(R.id.item_qustion_title,"第"+helper.getAdapterPosition()+"题  "+question);
        helper.setText(R.id.item_qustion_a, ar[0]);
        helper.setText(R.id.item_qustion_b,ar[1]);
        helper.setText(R.id.item_qustion_c, ar[2]);
        helper.setText(R.id.item_qustion_d,ar[3]);

        helper.addOnClickListener(R.id.item_qustion_a);
        helper.addOnClickListener(R.id.item_qustion_b);
        helper.addOnClickListener(R.id.item_qustion_c);
        helper.addOnClickListener(R.id.item_qustion_d);
        helper.addOnClickListener(R.id.item_qustion_next);

        if(item.getChoose()!=-1){
            ((Button)helper.getView(R.id.item_qustion_a)).setEnabled(true);
            ((Button)helper.getView(R.id.item_qustion_b)).setEnabled(true);
            ((Button)helper.getView(R.id.item_qustion_c)).setEnabled(true);
            ((Button)helper.getView(R.id.item_qustion_d)).setEnabled(true);
            switch (item.getChoose()){
                case 0:
                    ((Button)helper.getView(R.id.item_qustion_a)).setEnabled(false);
                    break;
                case 1:
                    ((Button)helper.getView(R.id.item_qustion_b)).setEnabled(false);
                    break;
                case 2:
                    ((Button)helper.getView(R.id.item_qustion_c)).setEnabled(false);
                    break;
                case 3:
                    ((Button)helper.getView(R.id.item_qustion_d)).setEnabled(false);
                    break;
            }
        }
    }
}
