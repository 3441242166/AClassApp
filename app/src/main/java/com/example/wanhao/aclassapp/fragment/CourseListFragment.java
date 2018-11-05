package com.example.wanhao.aclassapp.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
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
import com.example.wanhao.aclassapp.bean.ChatBean;
import com.example.wanhao.aclassapp.bean.Course;
import com.example.wanhao.aclassapp.broadcast.CourseReceiver;
import com.example.wanhao.aclassapp.broadcast.DownloadReceiver;
import com.example.wanhao.aclassapp.broadcast.MainReceiver;
import com.example.wanhao.aclassapp.config.ApiConstant;
import com.example.wanhao.aclassapp.presenter.CourseListPresenter;
import com.example.wanhao.aclassapp.util.PopupUtil;
import com.example.wanhao.aclassapp.util.SaveDataUtil;
import com.example.wanhao.aclassapp.view.ICourseFgView;
import com.yalantis.phoenix.PullToRefreshView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.realm.Realm;

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
    private MainReceiver receiver;

    private Context context;

    @Override
    protected int setContentView() {
        context = getContext();
        return R.layout.fragment_course;
    }

    @Override
    protected void lazyLoad() {
        presenter = new CourseListPresenter(getContext(),this);

        receiver = new MainReceiver();
        IntentFilter filter = new IntentFilter(ApiConstant.COURSE_ACTION);
        filter.setPriority(888);
        context.registerReceiver(receiver,filter);

        init();
        initEvent();
        presenter.upDataList(true);
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

            intent.putExtra(ApiConstant.COURSE_ID,this.adapter.getData().get(position).getId());
            intent.putExtra(ApiConstant.COURSE_NAME,this.adapter.getData().get(position).getName());
            startActivityForResult(intent,0);
        });

        adapter.setOnItemLongClickListener((adapter, view, position) -> {
            openDialog(view);
            return true;
        });

        fab.setOnClickListener(v -> startActivityForResult(new Intent(getContext(), AddCourseActivity.class),ApiConstant.ADD_COURSE));

        refreshView.setOnRefreshListener(() -> refreshView.postDelayed(() -> presenter.upDataList(false), 500));

        receiver.setOnNewMessageListener(data -> {
            Log.i(TAG, "onNewMessage: content = " + data.getContent());
            Realm realm = Realm.getDefaultInstance();
            realm.beginTransaction();

            Course course = realm.where(Course.class)
                    .equalTo("id",data.getCourseID()+"")
                    .findFirst();
            course.setUnReadNum(course.getUnReadNum()+1);

            realm.commitTransaction();
            adapter.notifyDataSetChanged();
        });
    }

    private void openDialog(View parent) {
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
            default:
                adapter.notifyDataSetChanged();
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        context.unregisterReceiver(receiver);
    }
}
