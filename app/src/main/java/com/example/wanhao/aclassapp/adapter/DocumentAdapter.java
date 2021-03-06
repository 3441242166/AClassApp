package com.example.wanhao.aclassapp.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.wanhao.aclassapp.R;
import com.example.wanhao.aclassapp.bean.Document;
import com.example.wanhao.aclassapp.util.FileConvertUtil;
import com.example.wanhao.aclassapp.util.FileSizeUtil;
import com.example.wanhao.myview.slidelayout.SlideLayout;

import java.util.List;

/**
 * Created by wanhao on 2018/4/1.
 */

public class DocumentAdapter extends BaseQuickAdapter<Document,BaseViewHolder> {

    TestAdapter.OnStateChange stateChange;
    boolean[] isOpenList;

    public DocumentAdapter(@Nullable List<Document> data) {
        super(R.layout.item_document, data);
        if(data!=null){
            isOpenList = new boolean[data.size()];
        }
    }

    @Override
    public void setNewData(@Nullable List<Document> data) {
        super.setNewData(data);
        if(data!=null){
            isOpenList = new boolean[data.size()];
        }
    }

    @Override
    protected void convert(BaseViewHolder helper, Document item) {

        helper.addOnClickListener(R.id.item_menu1)
                .addOnClickListener(R.id.item_menu2)
                .addOnClickListener(R.id.item_menu3)
                .addOnClickListener(R.id.item_course_content);

        SlideLayout layout = helper.getView(R.id.item_document_content);
        layout.setOnStateChangeListener(isOpen -> {
            isOpenList[helper.getLayoutPosition()] = isOpen;
            if(stateChange!=null){
                stateChange.onStateChange(isOpen,helper.getLayoutPosition());
            }
        });
        layout.setMenuState(isOpenList[helper.getLayoutPosition()]);

        helper.setText(R.id.item_document_name,item.getTitle());
        helper.setText(R.id.item_document_size,FileSizeUtil.FormetFileSize(Integer.valueOf(item.getSize()))+" 来自 "+item.getUser());
        helper.setText(R.id.item_document_time,item.getDate());
        helper.setImageResource(R.id.item_document_image,FileConvertUtil.getDocumentImageID(item.getTitle()));

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

    public void setStateChange(TestAdapter.OnStateChange stateChange){
        this.stateChange = stateChange;
    }

    public interface OnStateChange{
        void onStateChange(boolean isOpen,int pos);
    }
}
