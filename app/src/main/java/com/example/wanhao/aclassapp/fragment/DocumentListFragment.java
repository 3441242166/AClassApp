package com.example.wanhao.aclassapp.fragment;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.wanhao.aclassapp.R;
import com.example.wanhao.aclassapp.activity.BrowseDocumentActivity;
import com.example.wanhao.aclassapp.adapter.DocumentAdapter;
import com.example.wanhao.aclassapp.base.LazyLoadFragment;
import com.example.wanhao.aclassapp.bean.sqlbean.Document;
import com.example.wanhao.aclassapp.config.ApiConstant;
import com.example.wanhao.aclassapp.presenter.DocumentFgPresenter;
import com.example.wanhao.aclassapp.util.MyItemDecoration;
import com.example.wanhao.aclassapp.view.IDocumentFgView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by wanhao on 2018/4/1.
 */

public class DocumentListFragment extends LazyLoadFragment  implements IDocumentFgView {

    @BindView(R.id.fg_document_list_recycler)
    RecyclerView recyclerView;

    DocumentFgPresenter presenter;
    List<Document> list;
    private String courseID;
    private String documentType;
    private DocumentAdapter adapters;

    private View notDataView;
    private View errorView;

    @Override
    protected int setContentView() {
        return R.layout.fragment_document_list;
    }

    @Override
    protected void lazyLoad() {
        courseID = (String) getArguments().get(ApiConstant.COURSE_ID);
        documentType = (String) getArguments().get(ApiConstant.DOCUMENT_TYPE);
        Log.i(TAG, "lazyLoad: documentType "+documentType);

        presenter = new DocumentFgPresenter(this,getActivity());

        initView();
        initEvent();
        presenter.getDocumentList(courseID,documentType);
        Log.i(TAG, "getDocumentList: "+documentType+" tData ");
    }

    private void initView() {
        adapters = new DocumentAdapter(null);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL));
        recyclerView.addItemDecoration(new MyItemDecoration());
        recyclerView.setAdapter(adapters);

        notDataView = getLayoutInflater().inflate(R.layout.empty_view, (ViewGroup) recyclerView.getParent(), false);

        errorView = getLayoutInflater().inflate(R.layout.error_view, (ViewGroup) recyclerView.getParent(), false);

        adapters.setEmptyView(notDataView);
    }

    private void initEvent(){
        adapters.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(getActivity(),BrowseDocumentActivity.class);
                intent.putExtra(ApiConstant.DOCUMENT_ID,list.get(position).getId());
                startActivity(intent);
            }
        });

        errorView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapters.setEmptyView(notDataView);
                presenter.getDocumentList(courseID,documentType);
            }
        });

        notDataView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.getDocumentList(courseID,documentType);
            }
        });
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void disimissProgress() {

    }

    @Override
    public void loadDataError(String throwable) {
        //adapters.setEmptyView(errorView);
        Log.i(TAG, "loadDataError: "+documentType);
    }

    @Override
    public void loadDataSuccess(List<Document> tData, String type) {
        Log.i(TAG, "loadDataSuccess: "+documentType+" tData "+tData.get(0).getTitle());
        list=tData;
        adapters.setNewData(list);
    }

    @Override
    public void tokenError() {
        Log.i(TAG, "tokenError: "+documentType);
    }
}
