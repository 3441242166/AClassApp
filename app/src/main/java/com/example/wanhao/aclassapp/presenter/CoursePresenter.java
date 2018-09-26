package com.example.wanhao.aclassapp.presenter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.wanhao.aclassapp.R;
import com.example.wanhao.aclassapp.activity.DocumentActivity;
import com.example.wanhao.aclassapp.activity.HomeWorkActivity;
import com.example.wanhao.aclassapp.activity.RemarkActivity;
import com.example.wanhao.aclassapp.adapter.GridAdapter;
import com.example.wanhao.aclassapp.bean.GridBean;
import com.example.wanhao.aclassapp.util.PopupUtil;
import com.example.wanhao.aclassapp.view.CourseView;

import java.util.ArrayList;

public class CoursePresenter {
    private static final String TAG = "CoursePresenter";

    private Context context;
    private CourseView view;

    public CoursePresenter(Context context, CourseView view){
        this.context  = context;
        this.view = view;
    }

    private static final String[] OTHER_TITLE = {"留言版", "课后作业", "课堂文件", "公告", "课程信息", "聊天纪录", "Em...."};
    private static final int[] OTHER_IMG = {R.mipmap.gv_animation, R.mipmap.gv_multipleltem, R.mipmap.gv_header_and_footer, R.mipmap.gv_pulltorefresh, R.mipmap.gv_section, R.mipmap.gv_empty, R.mipmap.gv_drag_and_swipe};
    private static final Class[] CLASSES = {RemarkActivity.class,HomeWorkActivity.class,DocumentActivity.class,RemarkActivity.class,RemarkActivity.class,RemarkActivity.class,RemarkActivity.class};

    public void openSelectAvatarDialog(){
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_course_more, null);
        final PopupWindow popupWindow = PopupUtil.getPopupWindow(context,view);
        //设置点击事件
        RecyclerView recyclerView = view.findViewById(R.id.dialog_course_recycler);
        recyclerView.setLayoutManager(new GridLayoutManager(context, 4));
        ArrayList<GridBean> otherList = new ArrayList<>();
        for(int x=0;x<OTHER_TITLE.length;x++){
            GridBean bean= new GridBean(OTHER_IMG[x],OTHER_TITLE[x]);
            otherList.add(bean);
        }
        GridAdapter adapter = new GridAdapter(otherList,context);
        adapter.setOnItemClickListener((adapter1, view1, position) -> {
            Toast.makeText(context,""+position,Toast.LENGTH_SHORT).show();
            this.view.startActivity(CLASSES[position],null);
            popupWindow.dismiss();
        });
        recyclerView.setAdapter(adapter);
        View parent = LayoutInflater.from(context).inflate(R.layout.content_course, null);
        //显示PopupWindow
        popupWindow.setAnimationStyle(R.style.animTranslate);
        popupWindow.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
    }

}
