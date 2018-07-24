package com.example.wanhao.aclassapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.wanhao.aclassapp.R;
import com.example.wanhao.aclassapp.adapter.DocumentAdapter;
import com.example.wanhao.aclassapp.base.TopBarBaseActivity;
import com.example.wanhao.aclassapp.bean.sqlbean.Document;
import com.example.wanhao.aclassapp.config.ApiConstant;
import com.example.wanhao.aclassapp.dialog.DocumentDialog;
import com.example.wanhao.aclassapp.presenter.DocumentPresenter;
import com.example.wanhao.aclassapp.util.ActivityCollector;
import com.example.wanhao.aclassapp.util.ColorDividerItemDecoration;
import com.example.wanhao.aclassapp.util.MyItemDecoration;
import com.example.wanhao.aclassapp.view.IDocumentView;

import java.util.List;

import butterknife.BindView;

public class DocumentActivity extends TopBarBaseActivity implements IDocumentView{
    private static final String TAG = "DocumentActivity";


    @BindView(R.id.ac_document_recycler)
    RecyclerView recyclerView;

    private DocumentAdapter adapter;
    private List<Document> list;

    private DocumentPresenter presenter;
    private DocumentDialog dialog;

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
        dialog = new DocumentDialog(this);

        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL));
        recyclerView.addItemDecoration(new ColorDividerItemDecoration());
        adapter = new DocumentAdapter(null);
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
                dialog.show();
            }
        });
        
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Log.i(TAG, "onItemClick: ");
                Intent intent = new Intent(DocumentActivity.this,BrowseDocumentActivity.class);
                intent.putExtra(ApiConstant.DOCUMENT_ID,list.get(position).getId());
                startActivity(intent);
            }
        });

        dialog.setOnSelectListener(new DocumentDialog.onSelectListener() {
            @Override
            public void onSelect(int i, int j) {
                presenter.sortAndGroup(i,j);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }


    @Override
    public void showProgress() {

    }

    @Override
    public void disimissProgress() {

    }

    @Override
    public void loadDataSuccess(List<Document> tData) {
        list = tData;
        adapter.setNewData(tData);
    }

    @Override
    public void loadDataError(String throwable) {

    }

    @Override
    public void tokenError(String msg) {

    }
}
