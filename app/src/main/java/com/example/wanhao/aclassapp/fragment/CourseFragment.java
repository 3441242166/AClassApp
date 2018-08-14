package com.example.wanhao.aclassapp.fragment;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.wanhao.aclassapp.R;
import com.example.wanhao.aclassapp.activity.MainActivity;
import com.example.wanhao.aclassapp.adapter.CourseAdapter;
import com.example.wanhao.aclassapp.base.LazyLoadFragment;
import com.example.wanhao.aclassapp.bean.Course;
import com.example.wanhao.aclassapp.config.ApiConstant;
import com.example.wanhao.aclassapp.presenter.CourseFgPresenter;
import com.example.wanhao.aclassapp.view.ICourseFgView;
import com.yalantis.phoenix.PullToRefreshView;

import java.util.List;

import butterknife.BindView;

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

    MaterialDialog dialog;
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
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL));
        //refreshView.setNestedScrollingEnabled(false);

        adapter = new CourseAdapter(getActivity());
        recyclerView.setAdapter(adapter);

        MaterialDialog.Builder builder = new MaterialDialog.Builder(getActivity())
                .title("警告")
                .content("确定要删除此门课程吗")
                .positiveText("删除")
                .negativeText("点错了")
                .onAny(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        if (which == DialogAction.POSITIVE) {
                            presenter.delete(String.valueOf(deletePos+1));
                        }

                    }
                });
        dialog = builder.build();

    }

    private void initEvent(){
        adapter.setOnItemClickListener(new CourseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, final int position) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.putExtra(ApiConstant.COURSE_NAME,courseList.get(position).getName());
                intent.putExtra(ApiConstant.COURSE_ID,courseList.get(position).getId());
                Log.i(TAG, "onItemClick: "+courseList.get(position).getId());
                startActivity(intent);
            }

        });

        adapter.setOnLongItemClickListener(new CourseAdapter.OnLongItemClickListener() {
            @Override
            public void onLongItemClick(View view,final int position) {
                deletePos =position;
                dialog.show();
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

        courseList.remove(deletePos);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void tokenError(String msg) {
        tokenError(msg);
    }
}
