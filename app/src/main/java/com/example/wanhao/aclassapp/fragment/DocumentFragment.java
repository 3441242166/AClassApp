package com.example.wanhao.aclassapp.fragment;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;

import com.example.wanhao.aclassapp.R;
import com.example.wanhao.aclassapp.adapter.DocumentAdapter;
import com.example.wanhao.aclassapp.base.LazyLoadFragment;
import com.example.wanhao.aclassapp.bean.Document;
import com.example.wanhao.aclassapp.config.ApiConstant;
import com.example.wanhao.aclassapp.presenter.DocumentPresenter;
import com.example.wanhao.aclassapp.util.PagingScrollHelper;
import com.example.wanhao.aclassapp.view.IDocumentView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by wanhao on 2018/2/27.
 */

public class DocumentFragment extends LazyLoadFragment implements IDocumentView{
    private static final String TAG = "DocumentFragment";

    @BindView(R.id.fg_document_beforlist)
    RecyclerView beforRv;
    @BindView(R.id.fg_document_classlist)
    RecyclerView classRv;

    DocumentAdapter documentAdapter;
    DocumentAdapter previewAdapter;

    DocumentPresenter presenter;

    private List<Document> documentList;
    private List<Document> previewList;

    private String courseID;

    @Override
    protected int setContentView() {
        return R.layout.framgnet_document;
    }

    @Override
    protected void lazyLoad() {
        courseID = getArguments().getString(ApiConstant.COURSE_ID);
        Log.i(TAG, "lazyLoad: "+courseID);
        presenter = new DocumentPresenter(this,getActivity());

        initView();

        presenter.getDocumentList(courseID,"edata");
        presenter.getDocumentList(courseID,"preview");
    }

    private void initView() {

        beforRv.setLayoutManager(new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.HORIZONTAL));

        classRv.setLayoutManager(new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.HORIZONTAL));

        new PagingScrollHelper().setUpRecycleView(beforRv);
        new PagingScrollHelper().setUpRecycleView(classRv);

        documentAdapter = new DocumentAdapter(getActivity());
        previewAdapter = new DocumentAdapter(getActivity());

        beforRv.setAdapter(this.documentAdapter);
        classRv.setAdapter(documentAdapter);

//        List<Document> documents = new ArrayList<>();
//        for(int x=0;x<5;x++){
//            Document document = new Document();
//            document.setTitle("动态规划课件");
//            document.setSize("3.45MB  来自万浩老师");
//            document.setDate(DateUtil.getNowDateString());
//            documents.add(document);
//        }
//
//        documentAdapter.setData(documents);
//        previewAdapter.setData(documents);
    }


    @Override
    public void showProgress() {

    }

    @Override
    public void disimissProgress() {

    }

    @Override
    public void loadDataSuccess(List<Document> tData,String type) {
        if(type.equals("edata")){
            documentList = tData;
            documentAdapter.setData(documentList);
        }
        if(type.equals("preview")){
            previewList = tData;
            previewAdapter.setData(previewList);
        }
    }

    @Override
    public void loadDataError(String throwable) {

    }

}
