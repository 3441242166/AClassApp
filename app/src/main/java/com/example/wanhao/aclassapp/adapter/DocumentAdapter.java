package com.example.wanhao.aclassapp.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.wanhao.aclassapp.R;
import com.example.wanhao.aclassapp.bean.Document;
import com.example.wanhao.aclassapp.util.FileConvertUtil;
import com.example.wanhao.aclassapp.util.FileSizeUtil;

import java.util.List;

/**
 * Created by wanhao on 2018/4/1.
 */

public class DocumentAdapter extends BaseQuickAdapter<Document,BaseViewHolder> {

    public DocumentAdapter(@Nullable List<Document> data) {
        super(R.layout.item_document, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Document item) {

        helper.setText(R.id.item_document_name,item.getTitle());
        helper.setText(R.id.item_document_size,FileSizeUtil.FormetFileSize(Integer.valueOf(item.getSize()))+" 来自 "+item.getUser());
        helper.setText(R.id.item_document_time,item.getDate());
        helper.setImageResource(R.id.item_document_image,FileConvertUtil.getDocumentImageID(item.getTitle()));

    }
}
