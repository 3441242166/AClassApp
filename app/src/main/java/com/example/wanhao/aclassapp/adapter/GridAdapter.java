package com.example.wanhao.aclassapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.wanhao.aclassapp.R;
import com.example.wanhao.aclassapp.bean.GridBean;

import java.util.List;

/**
 * Created by wanhao on 2018/3/10.
 */

public class GridAdapter extends BaseAdapter {

    private List<GridBean> list;

    private Context context;
    private LayoutInflater inflater;

    public GridAdapter(List<GridBean> list, Context context){
        this.list = list;
        this.context = context;
        // 布局装载器对象
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public GridBean getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        ViewHolder holder;

        if (view == null) {
            //LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            view = inflater.inflate(R.layout.item_grid, viewGroup, false);
            holder = new ViewHolder();
            holder.textView = (TextView) view.findViewById(R.id.item_grid_text);
            holder.imageView = (ImageView) view.findViewById(R.id.item_grid_image);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        GridBean item = list.get(i);
        holder.textView.setText(item.getName());
        Glide.with(context).load(item.getImgID()).into(holder.imageView);
        return view;

    }

    private class ViewHolder {
        TextView textView;
        ImageView imageView;
    }
}
