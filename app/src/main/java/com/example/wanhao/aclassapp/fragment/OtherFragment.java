package com.example.wanhao.aclassapp.fragment;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.example.wanhao.aclassapp.R;
import com.example.wanhao.aclassapp.activity.HomeWorkActivity;
import com.example.wanhao.aclassapp.activity.RemarkActivity;
import com.example.wanhao.aclassapp.adapter.CourseAdapter;
import com.example.wanhao.aclassapp.adapter.GridAdapter;
import com.example.wanhao.aclassapp.base.LazyLoadFragment;
import com.example.wanhao.aclassapp.bean.GridBean;
import com.example.wanhao.aclassapp.config.ApiConstant;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by wanhao on 2018/2/27.
 */

public class OtherFragment extends LazyLoadFragment {

    @BindView(R.id.fg_other_grid)
    RecyclerView gridView;

    private List<GridBean> dataList;
    private GridAdapter adapter;

    private String courseID;

    @Override
    protected int setContentView() {
        return R.layout.framgnet_other;
    }

    @Override
    protected void lazyLoad() {

        initData();
        initView();
        initEvent();

    }

    private void initData() {
        courseID = getArguments().getString(ApiConstant.COURSE_ID);

        dataList = new ArrayList<>();
        GridBean bean= new GridBean(R.drawable.icon_other_0,"留言板");

        dataList.add(bean);

        bean= new GridBean(R.drawable.icon_other_0,"课后习题");
        dataList.add(bean);

//        for (int i = 0; i <25; i++) {
//            GridBean bean= new GridBean();
//            bean.setImgID(R.drawable.mainicon_0);
//            bean.setName("sss");
//            dataList.add(bean);
//        }

    }

    private void initView() {

        adapter=new GridAdapter(getActivity());
        adapter.setData(dataList);
        gridView.setHasFixedSize(true);
        gridView.setLayoutManager(new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL));
        gridView.setAdapter(adapter);
    }

    private void initEvent() {

        adapter.setOnItemClickListener(new CourseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent;
                switch (position){
                    case 0:
                        intent = new Intent(getActivity(), RemarkActivity.class);
                        intent.putExtra(ApiConstant.COURSE_ID,courseID);
                        startActivity(intent);
                        break;
                    case 1:
                        intent = new Intent(getActivity(), HomeWorkActivity.class);
                        intent.putExtra(ApiConstant.COURSE_ID,courseID);
                        startActivity(intent);
                        break;
                }
            }
        });

    }
}
