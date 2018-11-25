package com.example.wanhao.aclassapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wanhao.aclassapp.R;
import com.example.wanhao.aclassapp.adapter.DocumentAdapter;
import com.example.wanhao.aclassapp.base.TopBarBaseActivity;
import com.example.wanhao.aclassapp.bean.Document;
import com.example.wanhao.aclassapp.config.ApiConstant;
import com.example.wanhao.aclassapp.presenter.DocumentPresenter;
import com.example.wanhao.aclassapp.util.ColorDividerItemDecoration;
import com.example.wanhao.aclassapp.view.IDocumentView;

import java.util.List;

import butterknife.BindView;

public class DocumentActivity extends TopBarBaseActivity<DocumentPresenter> implements IDocumentView{
    private static final String TAG = "DocumentActivity";


    @BindView(R.id.ac_document_recycler)
    RecyclerView recyclerView;
    @BindView(R.id.ac_document_search)
    TextView search;

    private DocumentAdapter adapter;

    private String courseID;

    @Override
    protected int getContentView() {
        return R.layout.activity_document;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
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

        adapter.setOnItemChildClickListener((adapter, view, position) -> {
            switch (view.getId()){
                case R.id.item_menu1:
                    Toast.makeText(DocumentActivity.this,"111",Toast.LENGTH_SHORT).show();
                    break;
                case R.id.item_menu2:
                    Toast.makeText(DocumentActivity.this,"222",Toast.LENGTH_SHORT).show();
                    break;
                case R.id.item_menu3:
                    Toast.makeText(DocumentActivity.this,"333",Toast.LENGTH_SHORT).show();
                    adapter.remove(position);
                    break;
                case R.id.item_course_content:
                    presenter.checkDocument(this.adapter.getData().get(position));
                    break;
            }
        });

        adapter.setStateChange((isOpen, pos) -> {
            Log.i(TAG, "init: isOpen = "+isOpen + "  pos = "+pos);
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
    public void errorMessage(String throwable) {

    }

    @Override
    public void tokenError(String msg) {
        showTokenErrorDialog(msg);
    }

    @Override
    protected DocumentPresenter setPresenter() {
        return new DocumentPresenter(this,this);
    }
}
