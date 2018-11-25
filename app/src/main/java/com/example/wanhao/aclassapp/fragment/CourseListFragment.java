package com.example.wanhao.aclassapp.fragment;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.example.wanhao.aclassapp.R;
import com.example.wanhao.aclassapp.activity.AddCourseActivity;
import com.example.wanhao.aclassapp.activity.CourseActivity;
import com.example.wanhao.aclassapp.adapter.CourseAdapter;
import com.example.wanhao.aclassapp.base.BaseTokenActivity;
import com.example.wanhao.aclassapp.base.IBasePresenter;
import com.example.wanhao.aclassapp.base.LazyLoadFragment;
import com.example.wanhao.aclassapp.config.ApiConstant;
import com.example.wanhao.aclassapp.db.CourseDB;
import com.example.wanhao.aclassapp.presenter.CourseListPresenter;
import com.example.wanhao.aclassapp.util.PopupUtil;
import com.example.wanhao.aclassapp.view.ICourseFgView;
import com.yalantis.phoenix.PullToRefreshView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by wanhao on 2018/2/23.
 */

public class CourseListFragment extends LazyLoadFragment<CourseListPresenter> implements ICourseFgView {
    private static final String TAG = "CourseListFragment";

    @BindView(R.id.fg_course_recycler)
    RecyclerView recyclerView;
    @BindView(R.id.fg_course_refresh)
    PullToRefreshView refreshView;
    @BindView(R.id.fg_course_add)
    FloatingActionButton fab;

    private CourseAdapter adapter;

    @Override
    protected CourseListPresenter setPresenter() {
        return new CourseListPresenter(getContext(),this);
    }

    @Override
    protected int setContentView() {
        return R.layout.fragment_course;
    }

    @Override
    protected void lazyLoad() {
        presenter = new CourseListPresenter(getContext(),this);

        init();
        initEvent();
        presenter.getListDataByDB();
    }

    private void init(){
        adapter  = new CourseAdapter(null,getContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setAdapter(adapter);
    }

    private void initEvent(){

        adapter.setOnItemClickListener((adapter, view, position) -> {
            Intent intent = new Intent(getActivity(), CourseActivity.class);

            intent.putExtra(ApiConstant.COURSE_ID ,this.adapter.getData().get(position).getCourseID());
            startActivityForResult(intent,0);

        });

        fab.setOnClickListener(v -> startActivityForResult(new Intent(getContext(), AddCourseActivity.class),ApiConstant.ADD_COURSE));

        refreshView.setOnRefreshListener(() -> refreshView.postDelayed(() -> presenter.getListDataByInternet(), 500));

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
    public void loadDataSuccess(List<CourseDB> tData) {
        adapter.setNewData(tData);
    }

    @Override
    public void errorMessage(String throwable) {
        Toast.makeText(getActivity(), throwable, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void tokenError(String msg) {
        if(getActivity()!=null)
            ((BaseTokenActivity)getActivity()).showTokenErrorDialog(msg);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case ApiConstant.ADD_COURSE:
                if(resultCode == ApiConstant.ADD_SUCCESS){
                    presenter.getListDataByInternet();
                }
            default:
                adapter.notifyDataSetChanged();
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void updateData(int index) {
        adapter.notifyItemChanged(index);
    }

}
