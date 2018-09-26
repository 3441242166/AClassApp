package com.example.wanhao.aclassapp.adapter;

import android.content.Context;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.wanhao.aclassapp.R;


import java.util.List;

public class SettingAdapter extends BaseQuickAdapter<SettingAdapter.SettingBean,BaseViewHolder> {

    private Context context;

    public SettingAdapter(@Nullable List<SettingBean> data, Context context) {
        super(R.layout.item_setting, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, SettingBean item) {
        helper.setText(R.id.item_set_title,item.title);
        helper.setText(R.id.item_set_content,item.content);
    }

    public static class SettingBean{
        public String title;
        public String content;

        public SettingBean(String title) {
            this.title = title;
        }
    }
}
