package com.example.wanhao.aclassapp.fragment;

import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.example.wanhao.aclassapp.R;
import com.example.wanhao.aclassapp.adapter.GridAdapter;
import com.example.wanhao.aclassapp.base.LazyLoadFragment;
import com.example.wanhao.aclassapp.bean.GridBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by wanhao on 2018/2/27.
 */

public class OtherFragment extends LazyLoadFragment {

    @BindView(R.id.fg_other_grid)
    GridView gridView;

    private List<GridBean> dataList;

    private GridAdapter adapter;

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

        dataList = new ArrayList<>();
        for (int i = 0; i <25; i++) {
            GridBean bean= new GridBean();
            bean.setImgID(R.drawable.mainicon_0);
            bean.setName("sss");
            dataList.add(bean);
        }
    }

    private void initView() {

        adapter=new GridAdapter(dataList,getActivity());

        gridView.setAdapter(adapter);
    }

    private void initEvent() {

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getActivity(),""+i+l,Toast.LENGTH_SHORT).show();
            }
        });

    }
}
