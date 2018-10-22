package com.example.wanhao.aclassapp.fragment;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.example.wanhao.aclassapp.R;
import com.example.wanhao.aclassapp.activity.AddCourseActivity;
import com.example.wanhao.aclassapp.activity.CourseActivity;
import com.example.wanhao.aclassapp.adapter.CourseAdapter;
import com.example.wanhao.aclassapp.base.LazyLoadFragment;
import com.example.wanhao.aclassapp.bean.Course;
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
    @BindView(R.id.fg_course_add)
    FloatingActionButton fab;

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
        List<Course> courseList = new ArrayList<>();
        for(int x=0;x<10;x++){
            Course temp = new Course();
            courseList.add(temp);
        }
        adapter.setNewData(courseList);
        //------------------------------------------------------------------------------------------
        recyclerView.setAdapter(adapter);
    }

    private void initEvent(){

        adapter.setOnItemClickListener((adapter, view, position) -> {
            Intent intent = new Intent(getActivity(), CourseActivity.class);
            intent.putExtra(ApiConstant.COURSE_ID,this.adapter.getData().get(position));
            startActivity(intent);
        });

        adapter.setOnItemLongClickListener((adapter, view, position) -> {
            openDialog(view);
            return true;
        });

        fab.setOnClickListener(v -> {
            startActivityForResult(new Intent(getContext(), AddCourseActivity.class),ApiConstant.ADD_COURSE);
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
        Toast.makeText(getContext(),""+parent.getWidth(),Toast.LENGTH_SHORT).show();
        popupWindow.showAsDropDown(parent,parent.getWidth()/2,-parent.getHeight()/3);
        //popupWindow.showAtLocation(parent,1,1,1);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case ApiConstant.ADD_COURSE:
                if(resultCode == ApiConstant.ADD_SUCCESS){
                    presenter.upDataList(false);
                }
        }
    }
}
