package com.example.wanhao.aclassapp.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.wanhao.aclassapp.R;
import com.example.wanhao.aclassapp.bean.GridBean;

import java.util.List;

/**
 * Created by wanhao on 2018/3/10.
 */

    public class MyAdapter extends BaseQuickAdapter<GridBean,BaseViewHolder> {

        private List<GridBean> list;

        private Context context;

        public MyAdapter(int layoutResId, List data, Context context) {
            super(layoutResId, data);
            this.list = data;
            this.context = context;
        }

        @Override
        protected void convert(BaseViewHolder helper, GridBean item) {
                helper.setText(R.id.item_grid_text,item.getName());
                Glide.with(context).load(item.getImgID()).into((ImageView) helper.getView(R.id.item_grid_image));
            }
    }
