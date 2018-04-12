package com.example.wanhao.aclassapp.adapter;

import android.support.annotation.Nullable;
import android.util.Log;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.wanhao.aclassapp.R;
import com.example.wanhao.aclassapp.bean.ChatBean;

import java.util.List;

/**
 * Created by wanhao on 2018/4/5.
 */

public class ChatAdapter extends BaseMultiItemQuickAdapter<ChatBean,BaseViewHolder> {

    public ChatAdapter(@Nullable List<ChatBean> data) {
        super(data);
        addItemType(ChatBean.ME, R.layout.item_chat_me);
        addItemType(ChatBean.OTHER, R.layout.item_chat_other);
        Log.i(TAG, "ChatAdapter: ");
    }

    @Override
    protected void convert(BaseViewHolder helper, ChatBean item) {
        Log.i(TAG, "convert: type "+ helper.getItemViewType());
        switch (helper.getItemViewType()) {
            case ChatBean.ME:
                helper.setText(R.id.item_char_me_name,item.getName());
                helper.setText(R.id.item_char_me_content, item.getContent());
                helper.setImageResource(R.id.item_char_me_head, item.getHeadImage());
                break;
            case ChatBean.OTHER:
                helper.setText(R.id.item_char_other_name,item.getName());
                helper.setText(R.id.item_char_other_content, item.getContent());
                helper.setImageResource(R.id.item_char_other_head, item.getHeadImage());;
                break;
        }


    }
}