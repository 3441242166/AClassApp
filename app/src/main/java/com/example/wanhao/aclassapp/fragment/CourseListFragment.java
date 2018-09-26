package com.example.wanhao.aclassapp.fragment;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.example.wanhao.aclassapp.R;
import com.example.wanhao.aclassapp.activity.CourseActivity2;
import com.example.wanhao.aclassapp.adapter.CourseAdapter;
import com.example.wanhao.aclassapp.adapter.GridAdapter;
import com.example.wanhao.aclassapp.base.LazyLoadFragment;
import com.example.wanhao.aclassapp.bean.Course;
import com.example.wanhao.aclassapp.bean.GridBean;
import com.example.wanhao.aclassapp.config.ApiConstant;
import com.example.wanhao.aclassapp.presenter.CourseListPresenter;
import com.example.wanhao.aclassapp.util.PopupUtil;
import com.example.wanhao.aclassapp.view.ICourseFgView;
import com.yalantis.phoenix.PullToRefreshView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by wanhao on 2018/2/23.
 */

public class CourseListFragment extends LazyLoadFragment implements ICourseFgView {
    private static final String TAG = "CourseListFragment";

    @BindView(R.id.fg_course_recycler)
    RecyclerView recyclerView;

    @BindView(R.id.fg_course_refresh)
    PullToRefreshView refreshView;

    private CourseListPresenter presenter;
    private CourseAdapter adapter;

    @Override
    protected int setContentView() {
        return R.layout.fragment_course;
    }

    @Override
    protected void lazyLoad() {
        Log.i(TAG, "lazyLoad: "+getContext());
        presenter = new CourseListPresenter(getContext(),this);
        adapter  = new CourseAdapter(null,getContext());
        presenter.upDataList(false);
        init();
        initEvent();
    }

    private void init(){
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL));
        //------------------------------------------------------------------------------------------
//        List<Course> courseList = new ArrayList<>();
//        for(int x=0;x<10;x++){
//            Course temp = new Course();
//            temp.setCode("1");
//            courseList.add(temp);
//        }
//        adapter.setNewData(courseList);
        //------------------------------------------------------------------------------------------
        recyclerView.setAdapter(adapter);
    }

    private void initEvent(){

        adapter.setOnItemClickListener((adapter, view, position) -> {
            Intent intent = new Intent(getActivity(), CourseActivity2.class);
            intent.putExtra(ApiConstant.COURSE_ID,this.adapter.getData().get(position));
            startActivity(intent);
        });

        adapter.setOnItemLongClickListener((adapter, view, position) -> {
            openDialog(view);
            return true;
        });

        refreshView.setOnRefreshListener(() -> refreshView.postDelayed(() -> presenter.upDataList(false), 500));
    }

    private void openDialog(View parent) {
        Context context = getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_course, null);
        final PopupWindow popupWindow = PopupUtil.getPopupWindow(context,view, WindowManager.LayoutParams.WRAP_CONTENT,WindowManager.LayoutParams.WRAP_CONTENT);
        //设置点击事件
        Button btTop = view.findViewById(R.id.dialog_course_top);
        Button btMessage = view.findViewById(R.id.dialog_course_message);
        Button btDelete = view.findViewById(R.id.dialog_course_delete);

        btTop.setOnClickListener(v -> {

        });
        btMessage.setOnClickListener(v -> {

        });
        btDelete.setOnClickListener(v -> {

        });

        //显示PopupWindow
        popupWindow.showAsDropDown(parent);
    }

    @Override
    public void showProgress() {
        refreshView.setRefreshing(true);
    }

    @Override
    public void dismissProgress() {
        refreshView.setRefreshing(false);
    }

    @Override
    public void loadDataSuccess(List<Course> tData) {
        adapter.setNewData(tData);
    }

    @Override
    public void loadDataError(String throwable) {
        Toast.makeText(getActivity(), throwable, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void tokenError(String msg) {

    }
}
