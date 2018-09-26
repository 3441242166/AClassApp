package com.example.wanhao.aclassapp.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.wanhao.aclassapp.R;
import com.example.wanhao.aclassapp.bean.GridBean;

import java.util.List;

public class GridAdapter extends BaseQuickAdapter<GridBean,BaseViewHolder> {

    private Context context;

    public GridAdapter(@Nullable List<GridBean> data, Context context) {
        super(R.layout.item_grid, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, GridBean item) {
        helper.setText(R.id.item_grid_text,item.getName());
        Glide.with(context).load(item.getImgID()).crossFade().into((ImageView) helper.getView(R.id.item_grid_image));
    }
}
