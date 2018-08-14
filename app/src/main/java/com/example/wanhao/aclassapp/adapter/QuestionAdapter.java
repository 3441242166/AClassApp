package com.example.wanhao.aclassapp.adapter;

import android.content.Context;
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

    Context context;

    public QuestionAdapter(@Nullable List<Question> data, Context context) {
        super(R.layout.item_qustion, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, Question item) {

        helper.setText(R.id.item_qustion_title,item.getQuestion());

        helper.setText(R.id.item_qustion_A,item.getList().get(0));
        helper.setText(R.id.item_qustion_B,item.getList().get(1));
        helper.setText(R.id.item_qustion_C,item.getList().get(2));
        helper.setText(R.id.item_qustion_D,item.getList().get(3));

//        helper.addOnClickListener(R.id.item_qustion_a);
//        helper.addOnClickListener(R.id.item_qustion_b);
//        helper.addOnClickListener(R.id.item_qustion_c);
//        helper.addOnClickListener(R.id.item_qustion_d);
//        helper.addOnClickListener(R.id.item_qustion_next);
//
//        if(item.getChoose()!=-1){
//            ((Button)helper.getView(R.id.item_qustion_a)).setEnabled(true);
//            ((Button)helper.getView(R.id.item_qustion_b)).setEnabled(true);
//            ((Button)helper.getView(R.id.item_qustion_c)).setEnabled(true);
//            ((Button)helper.getView(R.id.item_qustion_d)).setEnabled(true);
//            switch (item.getChoose()){
//                case 0:
//                    ((Button)helper.getView(R.id.item_qustion_a)).setEnabled(false);
//                    break;
//                case 1:
//                    ((Button)helper.getView(R.id.item_qustion_b)).setEnabled(false);
//                    break;
//                case 2:
//                    ((Button)helper.getView(R.id.item_qustion_c)).setEnabled(false);
//                    break;
//                case 3:
//                    ((Button)helper.getView(R.id.item_qustion_d)).setEnabled(false);
//                    break;
//            }
//        }
    }
}
