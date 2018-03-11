package com.example.wanhao.aclassapp.fragment;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.wanhao.aclassapp.R;
import com.example.wanhao.aclassapp.activity.LodingActivity;
import com.example.wanhao.aclassapp.activity.MainActivity;
import com.example.wanhao.aclassapp.adapter.CourseAdapter;
import com.example.wanhao.aclassapp.base.LazyLoadFragment;
import com.example.wanhao.aclassapp.bean.Course;
import com.example.wanhao.aclassapp.config.ApiConstant;
import com.example.wanhao.aclassapp.presenter.CourseFgPresenter;
import com.example.wanhao.aclassapp.util.ActivityCollector;
import com.example.wanhao.aclassapp.view.ICourseFgView;
import com.yalantis.phoenix.PullToRefreshView;

import java.util.List;

import butterknife.BindView;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by wanhao on 2018/2/23.
 */

public class CourseFragment extends LazyLoadFragment implements ICourseFgView {

    @BindView(R.id.fg_course_recycler)
    RecyclerView recyclerView;

    @BindView(R.id.fg_course_refresh)
    PullToRefreshView refreshView;

    private CourseFgPresenter presenter;
    private List<Course> courseList;
    private CourseAdapter adapter;
    private StaggeredGridLayoutManager mLayoutManager;

    private int deletePos = -1;

    @Override
    protected int setContentView() {
        return R.layout.fragment_course;
    }

    @Override
    protected void lazyLoad() {
        init();
        initEvent();
        presenter.getList();
    }

    private void init(){
        presenter = new CourseFgPresenter(getActivity(),this);
        mLayoutManager = new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(mLayoutManager);
        //refreshView.setNestedScrollingEnabled(false);

        adapter = new CourseAdapter(getActivity());
        recyclerView.setAdapter(adapter);

    }

    private void initEvent(){
        adapter.setOnItemClickListener(new CourseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, final int position) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.putExtra("name",courseList.get(position).getName());

                startActivity(intent);
            }

        });

        adapter.setOnLongItemClickListener(new CourseAdapter.OnLongItemClickListener() {
            @Override
            public void onLongItemClick(View view,final int position) {
                new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("确定?")
                        .setContentText("你确定要删除"+courseList.get(position).getName()+"吗？")
                        .setConfirmText("是，删除它!")
                        .setCancelText("不，点错了")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                deletePos = position;
                                sDialog.cancel();
                                presenter.delete(courseList.get(position).getId());
                            }
                        })
                        .show();
            }
        });

        refreshView.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        presenter.upDateList();
                    }
                }, 2000);
            }
        });
    }

    @Override
    public void showProgress() {
        refreshView.setRefreshing(true);
    }

    @Override
    public void disimissProgress() {
        refreshView.setRefreshing(false);
    }

    @Override
    public void loadDataSuccess(List<Course> tData) {
        courseList =tData;
        adapter.setData(courseList);
    }

    @Override
    public void loadDataError(String throwable) {
        Toast.makeText(getActivity(), throwable, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(TAG, "onActivityResult: ");
        if (data.getIntExtra("result", ApiConstant.ADD_ERROR)==ApiConstant.ADD_SUCCESS) {
            presenter.upDateList();
        }
    }


    @Override
    public void deleteSucess() {
        new SweetAlertDialog(getActivity(),SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText("删除!")
                .setContentText("删除成功!")
                .setConfirmText("确认")
                .showCancelButton(false)
                .setCancelClickListener(null)
                .setConfirmClickListener(null)
                .show();
        courseList.remove(deletePos);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void tokenError(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
        ActivityCollector.finishAll();
        Intent intent = new Intent(getActivity(), LodingActivity.class);
        startActivity(intent);
    }
}
