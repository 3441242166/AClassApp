package com.example.wanhao.aclassapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.wanhao.aclassapp.R;
import com.example.wanhao.aclassapp.bean.Assignment;
import com.example.wanhao.aclassapp.bean.Course;
import com.example.wanhao.aclassapp.config.ApiConstant;
import com.example.wanhao.aclassapp.db.CourseDB;
import com.example.wanhao.aclassapp.util.SaveDataUtil;

import java.util.List;

/**
 * Created by wanhao on 2018/2/24.
 */

public class CourseAdapter extends BaseQuickAdapter<CourseDB,BaseViewHolder> {

    private Context context;

    public CourseAdapter(@Nullable List<CourseDB> data,Context context) {
        super(R.layout.item_course, data);
        this.context = context;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void convert(BaseViewHolder helper, CourseDB item) {

        helper.setText(R.id.item_course_name,item.getName());
        helper.setText(R.id.item_course_parent, item.getMajor());
        helper.setText(R.id.item_course_number,"一共有 "+item.getStudentSum()+" 人");
        TextView worring = helper.getView(R.id.item_course_warring);

        if(item.getUnRead()>0){
            worring.setVisibility(View.VISIBLE);
            worring.setText(""+item.getUnRead());
        }else {
            worring.setVisibility(View.GONE);
        }

        Glide.with(context).load(item.getPicture()).crossFade().into((ImageView) helper.getView(R.id.item_course_img));
    }
}
