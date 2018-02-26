package com.example.wanhao.aclassapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wanhao.aclassapp.R;
import com.example.wanhao.aclassapp.bean.Course;

import java.util.List;

/**
 * Created by wanhao on 2018/2/24.
 */

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.Holder> implements View.OnClickListener,View.OnLongClickListener{

    private List<Course> courseList;

    private OnItemClickListener onItemClickListener;
    private OnLongItemClickListener onItemLongClickListener;

    private View view;
    private Context context;

    public CourseAdapter(Context context){
        this.context = context;
    }

    public void setData(List<Course> courseList){
        this.courseList = courseList;
        notifyDataSetChanged();
    }

    @Override
    public CourseAdapter.Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_course, parent, false);
        Holder vh = new Holder(view);
        view.setOnClickListener(this);
        view.setOnLongClickListener(this);
        return vh;
    }

    @Override
    public void onBindViewHolder(CourseAdapter.Holder holder, int position) {
        Course course = courseList.get(position);

        holder.name.setText(course.getName());
        holder.parent.setText(course.getParent());
        holder.number.setText(course.getNum());
        //Glide.with(context).load(course.getImgUrl()).into(holder.bck);

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
        public TextView name;
        public TextView parent;
        public TextView number;
        public ImageView bck;

        public Holder(View view) {
            super(view);
            name = view.findViewById(R.id.item_course_name);
            parent = view.findViewById(R.id.item_course_parent);
            number = view.findViewById(R.id.item_course_number);
            bck = view.findViewById(R.id.item_course_img);
        }
    }

    public interface OnLongItemClickListener {
        void onLongItemClick(View view, int position);
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
}
