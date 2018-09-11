package com.example.wanhao.aclassapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.wanhao.aclassapp.R;
import com.example.wanhao.aclassapp.bean.GridBean;

import java.util.List;

public class GridAdapter extends RecyclerView.Adapter<GridAdapter.Holder> implements View.OnClickListener,View.OnLongClickListener{

    private List<GridBean> courseList;

    private OnItemClickListener onItemClickListener;
    private OnLongItemClickListener onItemLongClickListener;

    private View view;
    private Context context;

    public GridAdapter(Context context){
        this.context = context;
    }

    public void setData(List<GridBean> courseList){
        this.courseList = courseList;
        notifyDataSetChanged();
    }

    @Override
    public GridAdapter.Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_grid, parent, false);
        GridAdapter.Holder vh = new GridAdapter.Holder(view);
        view.setOnClickListener(this);
        view.setOnLongClickListener(this);
        return vh;
    }

    @Override
    public void onBindViewHolder(GridAdapter.Holder holder, int position) {
        GridBean course = courseList.get(position);

        holder.textView.setText(course.getName());
        Glide.with(context).load(course.getImgID()).into(holder.imageView);

        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return courseList==null ? 0:courseList.size();
    }

    @Override
    public void onClick(View view) {
        if(onItemClickListener!=null){
            onItemClickListener.onItemClick(view,(int)view.getTag());
        }
    }

    @Override
    public boolean onLongClick(View view) {
        if(onItemLongClickListener!=null){
            onItemLongClickListener.onLongItemClick(view,(int)view.getTag());
        }
        return true;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public void setOnLongItemClickListener(OnLongItemClickListener listener) {
        this.onItemLongClickListener = listener;
    }

    public static class Holder extends RecyclerView.ViewHolder {
        public TextView textView;
        public ImageView imageView;
        public Holder(View view) {
            super(view);
            textView = view.findViewById(R.id.item_grid_text);
            imageView = view.findViewById(R.id.item_grid_image);
        }
    }

    public interface OnLongItemClickListener {
        void onLongItemClick(View view, int position);
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
}
