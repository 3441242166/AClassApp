package com.example.wanhao.aclassapp.fragment;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.widget.Toast;

import com.example.wanhao.aclassapp.R;
import com.example.wanhao.aclassapp.activity.CourseActivity2;
import com.example.wanhao.aclassapp.adapter.CourseAdapter;
import com.example.wanhao.aclassapp.base.LazyLoadFragment;
import com.example.wanhao.aclassapp.bean.Course;
import com.example.wanhao.aclassapp.config.ApiConstant;
import com.example.wanhao.aclassapp.presenter.CourseListPresenter;
import com.example.wanhao.aclassapp.view.ICourseFgView;
import com.yalantis.phoenix.PullToRefreshView;

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
    private List<Course> courseList;
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
        presenter.upDataList(true);
        init();
        initEvent();
    }

    private void init(){
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL));

        recyclerView.setAdapter(adapter);

    }

    private void initEvent(){

        adapter.setOnItemClickListener((adapter, view, position) -> {
            Intent intent = new Intent(getActivity(), CourseActivity2.class);
            intent.putExtra(ApiConstant.COURSE_NAME,courseList.get(position).getName());
            intent.putExtra(ApiConstant.COURSE_ID,courseList.get(position).getId());
            Log.i(TAG, "onItemClick: "+courseList.get(position).getId());
            startActivity(intent);
        });

        adapter.setOnItemLongClickListener((adapter, view, position) -> {

            return false;
        });

        refreshView.setOnRefreshListener(() -> refreshView.postDelayed(() -> presenter.upDataList(false), 500));
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
        courseList =tData;
        adapter.setNewData(courseList);
    }

    @Override
    public void loadDataError(String throwable) {
        Toast.makeText(getActivity(), throwable, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void tokenError(String msg) {

    }
}
