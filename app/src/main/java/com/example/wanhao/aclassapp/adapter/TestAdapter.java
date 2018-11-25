package com.example.wanhao.aclassapp.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.wanhao.aclassapp.R;
import com.example.wanhao.aclassapp.bean.Document;
import com.example.wanhao.myview.slidelayout.SlideLayout;

import java.util.List;

public class TestAdapter extends BaseQuickAdapter<Document,BaseViewHolder> {

    OnStateChange stateChange;
    boolean[] isOpenList;

    public TestAdapter(@Nullable List<Document> data) {
        super(R.layout.item_test, data);
        if(data!=null){
            isOpenList = new boolean[data.size()];
        }

    }

    @Override
    public void setNewData(@Nullable List<Document> data) {
        if(data!=null){
            isOpenList = new boolean[data.size()];

        }
        super.setNewData(data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Document item) {

        helper.addOnClickListener(R.id.item_menu1)
                .addOnClickListener(R.id.item_menu2)
                .addOnClickListener(R.id.item_menu3)
                .addOnClickListener(R.id.item_course_content);

        SlideLayout layout = helper.getView(R.id.item_course_parent);
        layout.setOnStateChangeListener(isOpen -> {
            isOpenList[helper.getLayoutPosition()] = isOpen;
            if(stateChange!=null){
                stateChange.onStateChange(isOpen,helper.getLayoutPosition());
            }
        });
        layout.setMenuState(isOpenList[helper.getLayoutPosition()]);

    }

    @Override
    public void remove(int position) {
        for(int x = position ;x<isOpenList.length -1;x++){
            boolean temp = isOpenList[x];
            isOpenList[x] = isOpenList[x+1];
            isOpenList[x+1] = temp;
        }
        super.remove(position);
    }

    public void setStateChange(OnStateChange stateChange){
        this.stateChange = stateChange;
    }

    public interface OnStateChange{
        void onStateChange(boolean isOpen,int pos);
    }
}