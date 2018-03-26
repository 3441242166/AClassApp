package com.example.wanhao.aclassapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;

import com.example.wanhao.aclassapp.R;
import com.example.wanhao.aclassapp.adapter.DocumentSectionAdapter;
import com.example.wanhao.aclassapp.base.TopBarBaseActivity;
import com.example.wanhao.aclassapp.bean.Document;
import com.example.wanhao.aclassapp.config.ApiConstant;
import com.example.wanhao.aclassapp.presenter.DocumentPresenter;
import com.example.wanhao.aclassapp.util.MyItemDecoration;
import com.example.wanhao.aclassapp.view.IDocumentView;

import java.util.List;

import butterknife.BindView;

public class DocumentActivity extends TopBarBaseActivity implements IDocumentView{
    private static final String TAG = "DocumentActivity";


    @BindView(R.id.ac_document_recycler)
    RecyclerView recyclerView;

    private DocumentSectionAdapter adapter;
    List<List<Document>> lists;

    private DocumentPresenter presenter;

    @Override
    protected int getContentView() {
        return R.layout.activity_document;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        presenter = new DocumentPresenter(this,this);
        initView();
        initEvent();
        presenter.getListByCourse();
    }

    private void initView() {
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL));
        recyclerView.addItemDecoration(new MyItemDecoration());
        adapter = new DocumentSectionAdapter(this,null);
        recyclerView.setAdapter(adapter);
    }

    private void initEvent() {
        setTopLeftButton(new OnClickListener() {
            @Override
            public void onClick() {
                finish();
            }
        });

        setTopRightButton("筛选", new OnClickListener() {
            @Override
            public void onClick() {

            }
        });

        adapter.setOnItemClickListener(new DocumentSectionAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int section, int position) {
                Log.i(TAG, "onItemClick: ");
                Intent intent = new Intent(DocumentActivity.this,BrowseDocumentActivity.class);
                intent.putExtra(ApiConstant.Document_ID,lists.get(section).get(position).getId());
                startActivity(intent);
            }
        });
        
        adapter.setOnItemLongClickListener(new DocumentSectionAdapter.OnItemLongClickListener() {
            @Override
            public void onLongItemClick(int section, int position) {
                Log.i(TAG, "onLongItemClick: ");
                //startActivity(new Intent(DocumentActivity.this,BrowseDocumentActivity.class));
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
    public void loadDataSuccess(List<List<Document>> tData) {
        lists = tData;
        adapter.setData(tData);
    }

    @Override
    public void loadDataError(String throwable) {

    }

    @Override
    public void tokenError(String msg) {

    }
}
