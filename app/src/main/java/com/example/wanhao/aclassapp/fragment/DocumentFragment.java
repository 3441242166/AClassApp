package com.example.wanhao.aclassapp.fragment;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.example.wanhao.aclassapp.R;
import com.example.wanhao.aclassapp.adapter.DocumentAdapter;
import com.example.wanhao.aclassapp.base.LazyLoadFragment;
import com.example.wanhao.aclassapp.bean.Document;
import com.example.wanhao.aclassapp.util.DateUtil;
import com.example.wanhao.aclassapp.util.PagingScrollHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by wanhao on 2018/2/27.
 */

public class DocumentFragment extends LazyLoadFragment{
    private static final String TAG = "DocumentFragment";

    @BindView(R.id.fg_document_beforlist)
    RecyclerView beforRv;
    @BindView(R.id.fg_document_classlist)
    RecyclerView classRv;

    DocumentAdapter adapter;

    private List<Document> beforClassList;

    @Override
    protected int setContentView() {
        return R.layout.framgnet_document;
    }

    @Override
    protected void lazyLoad() {
        initView();
    }

    private void initView() {

        beforRv.setLayoutManager(new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.HORIZONTAL));

        classRv.setLayoutManager(new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.HORIZONTAL));

        new PagingScrollHelper().setUpRecycleView(beforRv);
        new PagingScrollHelper().setUpRecycleView(classRv);

        adapter = new DocumentAdapter(getActivity());

        DocumentAdapter documentAdapter = new DocumentAdapter(getActivity());

        beforRv.setAdapter(adapter);
        classRv.setAdapter(documentAdapter);

        List<Document> documents = new ArrayList<>();
        for(int x=0;x<5;x++){
            Document document = new Document();
            document.setTitle(""+x);
            document.setSize("1.23MB");
            document.setTime(DateUtil.getNowDateString());
            documents.add(document);
        }

        adapter.setData(documents);
        documentAdapter.setData(documents);
    }
}
