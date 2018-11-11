package com.example.wanhao.aclassapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.widget.TextView;

import com.example.wanhao.aclassapp.R;
import com.example.wanhao.aclassapp.adapter.DocumentAdapter;
import com.example.wanhao.aclassapp.base.TopBarBaseActivity;
import com.example.wanhao.aclassapp.bean.Document;
import com.example.wanhao.aclassapp.config.ApiConstant;
import com.example.wanhao.aclassapp.db.CourseDB;
import com.example.wanhao.aclassapp.presenter.DocumentPresenter;
import com.example.wanhao.aclassapp.util.ColorDividerItemDecoration;
import com.example.wanhao.aclassapp.view.IDocumentView;

import java.util.List;

import butterknife.BindView;

public class DocumentActivity extends TopBarBaseActivity implements IDocumentView{
    private static final String TAG = "DocumentActivity";


    @BindView(R.id.ac_document_recycler)
    RecyclerView recyclerView;
    @BindView(R.id.ac_document_search)
    TextView search;

    private DocumentAdapter adapter;

    private DocumentPresenter presenter;

    private String courseID;

    @Override
    protected int getContentView() {
        return R.layout.activity_document;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        presenter = new DocumentPresenter(this,this);

        initView();
        initEvent();
        courseID = getIntent().getStringExtra(ApiConstant.COURSE_ID);
        presenter.getListByInternet(courseID);
    }

    private void initView() {

        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL));
        recyclerView.addItemDecoration(new ColorDividerItemDecoration());

        adapter = new DocumentAdapter(null);
        recyclerView.setAdapter(adapter);

    }

    private void initEvent() {
        setTopLeftButton(this::finish);

        adapter.setOnItemClickListener((adapter, view, position) -> {
            Log.i(TAG, "onItemClick: ");
            presenter.checkDocument(this.adapter.getData().get(position));
            Intent intent = new Intent(DocumentActivity.this,BrowseDocumentActivity.class);
            intent.putExtra(ApiConstant.DOCUMENT,this.adapter.getData().get(position));
            intent.putExtra(ApiConstant.COURSE_ID,courseID);
            startActivity(intent);
        });

        search.setOnClickListener(v ->
                startActivity(new Intent(DocumentActivity.this,SearchActivity.class)));
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void dismissProgress() {

    }

    @Override
    public void loadDataSuccess(List<Document> tData) {
        adapter.setNewData(tData);
    }

    @Override
    public void loadDataError(String throwable) {

    }

    @Override
    public void tokenError(String msg) {
        showTokenErrorDialog(msg);
    }
}
