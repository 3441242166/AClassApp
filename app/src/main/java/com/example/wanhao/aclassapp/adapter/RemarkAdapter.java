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
import com.example.wanhao.aclassapp.bean.requestbean.Remark;
import com.example.wanhao.aclassapp.config.ApiConstant;
import com.example.wanhao.aclassapp.util.SaveDataUtil;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by wanhao on 2018/4/7.
 */

public class RemarkAdapter extends BaseMultiItemQuickAdapter<Remark,BaseViewHolder> {

    private List<Remark> data;
    private Context context;

    public RemarkAdapter(@Nullable List<Remark> data, Context context) {
        super(data);
        this.context = context;
        this.data = data;
        addItemType(Remark.NORMAL, R.layout.item_remark);
        addItemType(Remark.SPECIAL, R.layout.item_remark_remark);
    }

    @Override
    public void setNewData(@Nullable List<Remark> data) {
        this.data = data;
        super.setNewData(data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Remark item) {
        Log.i(TAG, "convert: type "+ helper.getItemViewType());
        GlideUrl cookie = new GlideUrl(item.getUserNmae().getAvatar()
                , new LazyHeaders.Builder().addHeader("Authorization", SaveDataUtil.getValueFromSharedPreferences(context,ApiConstant.USER_TOKEN)).build());

        switch (helper.getItemViewType()) {
            case Remark.NORMAL:
                Glide.with(context).load(cookie).into((CircleImageView) helper.getView(R.id.item_remark_head));
                helper.setText(R.id.item_remark_date,item.getDate());
                helper.setText(R.id.item_remark_name, item.getUserNmae().getNickName());
                helper.setText(R.id.item_remark_contant, item.getContent());
                break;
            case Remark.SPECIAL:
                helper.setText(R.id.item_remark_date,item.getDate());
                helper.setText(R.id.item_remark_name, item.getUserNmae().getNickName());
                helper.setText(R.id.item_remark_contant, item.getContent());
                Glide.with(context).load(cookie).into((CircleImageView) helper.getView(R.id.item_remark_head));

                Remark remark = new Remark();
                for(int x=0;x<data.size();x++){
                    if(item.getReply()==data.get(x).getId())
                        remark = data.get(x);
                }
                helper.setText(R.id.item_remark_remark_date,remark.getDate());
                helper.setText(R.id.item_remark_remark_name, item.getUserNmae().getNickName());
                helper.setText(R.id.item_remark_remark_contant, remark.getContent());
                Glide.with(context).load(cookie).into((CircleImageView) helper.getView(R.id.item_remark_remark_head));

                break;
        }


    }
}