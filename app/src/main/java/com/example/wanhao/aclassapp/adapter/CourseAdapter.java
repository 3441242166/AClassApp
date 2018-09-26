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
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.wanhao.aclassapp.R;
import com.example.wanhao.aclassapp.bean.Assignment;
import com.example.wanhao.aclassapp.bean.Course;
import com.example.wanhao.aclassapp.config.ApiConstant;
import com.example.wanhao.aclassapp.util.SaveDataUtil;

import java.util.List;

/**
 * Created by wanhao on 2018/2/24.
 */

public class CourseAdapter extends BaseQuickAdapter<Course,BaseViewHolder> {

    private Context context;

    public CourseAdapter(@Nullable List<Course> data,Context context) {
        super(R.layout.item_course, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, Course item) {

        helper.setText(R.id.item_course_name,item.getName());
        helper.setText(R.id.item_course_parent, item.getParent());
        helper.setText(R.id.item_course_number,"一共有 "+item.getNum()+" 人");

//        GlideUrl cookie = new GlideUrl(item.getImgUrl()
//                , new LazyHeaders.Builder().addHeader("Authorization", SaveDataUtil.getValueFromSharedPreferences(context,ApiConstant.USER_TOKEN)).build());
//        Glide.with(context).load(cookie).crossFade().into((ImageView) helper.getView(R.id.item_course_img));

        Glide.with(context).load(item.getImgUrl()).crossFade().into((ImageView) helper.getView(R.id.item_course_img));
    }
}
