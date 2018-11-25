package com.example.wanhao.aclassapp.activity;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.widget.Toast;

import com.example.wanhao.aclassapp.R;
import com.example.wanhao.aclassapp.adapter.TestAdapter;
import com.example.wanhao.aclassapp.base.IBasePresenter;
import com.example.wanhao.aclassapp.base.TopBarBaseActivity;
import com.example.wanhao.aclassapp.bean.Document;
import com.example.wanhao.aclassapp.util.ColorDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class TestActivity extends TopBarBaseActivity {
    private static final String TAG = "TestActivity";


    @BindView(R.id.ac_test_recycler)
    RecyclerView recyclerView;

    private TestAdapter adapter;

    @Override
    protected int getContentView() {
        return R.layout.activity_test;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        List<Document> list = new ArrayList<>();
        for(int x=0;x<15;x++){
            list.add(new Document());
        }
        adapter = new TestAdapter(list);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL));
        recyclerView.addItemDecoration(new ColorDividerItemDecoration());
        recyclerView.setAdapter(adapter);


        adapter.setOnItemChildClickListener((adapter, view, position) -> {
            switch (view.getId()){
                case R.id.item_menu1:
                    Toast.makeText(TestActivity.this,"111",Toast.LENGTH_SHORT).show();
                    break;
                case R.id.item_menu2:
                    Toast.makeText(TestActivity.this,"222",Toast.LENGTH_SHORT).show();
                    break;
                case R.id.item_menu3:
                    Toast.makeText(TestActivity.this,"333",Toast.LENGTH_SHORT).show();
                    adapter.remove(position);
                    break;
                case R.id.item_course_content:
                    Toast.makeText(TestActivity.this,"444",Toast.LENGTH_SHORT).show();
                    break;
            }
        });

        adapter.setStateChange((isOpen, pos) -> {
            Log.i(TAG, "init: isOpen = "+isOpen + "  pos = "+pos);
        });
    }

    @Override
    protected IBasePresenter setPresenter() {
        return null;
    }
}
