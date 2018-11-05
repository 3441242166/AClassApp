package com.example.wanhao.aclassapp.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.wanhao.aclassapp.R;
import com.example.wanhao.aclassapp.bean.ChatBean;
import com.example.wanhao.aclassapp.config.ApiConstant;
import com.example.wanhao.aclassapp.util.SaveDataUtil;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by wanhao on 2018/4/5.
 */

public class ChatAdapter extends BaseMultiItemQuickAdapter<ChatBean,BaseViewHolder> {

    private Context context;

    public ChatAdapter(@Nullable List<ChatBean> data, Context context) {
        super(data);
        this.context = context;
        addItemType(ChatBean.ME, R.layout.item_chat_me);
        addItemType(ChatBean.OTHER, R.layout.item_chat_other);
        Log.i(TAG, "ChatAdapter: ");
    }

    @Override
    protected void convert(BaseViewHolder helper, ChatBean item) {
//        GlideUrl cookie = new GlideUrl(item.getUser().getAvatar()
//                , new LazyHeaders.Builder().addHeader("Authorization", SaveDataUtil.getValueFromSharedPreferences(context, ApiConstant.USER_TOKEN)).build());

        Log.i(TAG, "convert: type "+ helper.getItemViewType());
        switch (helper.getItemViewType()) {
            case ChatBean.ME:
                helper.setText(R.id.item_char_me_name,item.getUser().getNickName());
                helper.setText(R.id.item_char_me_content, item.getContent());
                //Glide.with(context).load(cookie).into((CircleImageView) helper.getView(R.id.item_char_me_head));
                break;
            case ChatBean.OTHER:
                helper.setText(R.id.item_char_other_name,item.getUser().getNickName());
                helper.setText(R.id.item_char_other_content, item.getContent());
                //Glide.with(context).load(cookie).into((CircleImageView) helper.getView(R.id.item_char_other_head));
                break;
        }


    }
}