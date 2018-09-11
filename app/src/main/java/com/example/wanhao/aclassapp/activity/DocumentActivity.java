package com.example.wanhao.aclassapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;

import com.example.wanhao.aclassapp.R;
import com.example.wanhao.aclassapp.adapter.DocumentAdapter;
import com.example.wanhao.aclassapp.base.TopBarBaseActivity;
import com.example.wanhao.aclassapp.bean.Document;
import com.example.wanhao.aclassapp.config.ApiConstant;
import com.example.wanhao.aclassapp.dialog.DocumentDialog;
import com.example.wanhao.aclassapp.presenter.DocumentPresenter;
import com.example.wanhao.aclassapp.util.ActivityCollector;
import com.example.wanhao.aclassapp.util.ColorDividerItemDecoration;
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
        setTopLeftButton(() -> finish());

        setTopRightButton("筛选", () -> dialog.show());
        
        adapter.setOnItemClickListener((adapter, view, position) -> {
            Log.i(TAG, "onItemClick: ");
            Intent intent = new Intent(DocumentActivity.this,BrowseDocumentActivity.class);
            intent.putExtra(ApiConstant.DOCUMENT_ID,list.get(position).getId());
            startActivity(intent);
        });

        dialog.setOnSelectListener((i, j) -> presenter.sortAndGroup(i,j));
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
    public void dismissProgress() {

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
        tokenAbate(msg);
    }
}
