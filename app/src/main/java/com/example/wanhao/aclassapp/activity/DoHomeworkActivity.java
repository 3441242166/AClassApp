package com.example.wanhao.aclassapp.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.LinearSnapHelper;
import android.widget.Button;
import android.widget.TextView;

import com.example.wanhao.aclassapp.R;
import com.example.wanhao.aclassapp.adapter.QuestionAdapter;
import com.example.wanhao.aclassapp.base.TopBarBaseActivity;
import com.example.wanhao.aclassapp.bean.Homework;
import com.example.wanhao.aclassapp.bean.Question;
import com.example.wanhao.aclassapp.db.CourseDB;
import com.example.wanhao.aclassapp.presenter.DoHomeworkPresenter;
import com.example.wanhao.aclassapp.util.PagingScrollHelper;
import com.example.wanhao.aclassapp.view.DoHomeworkView;

import java.util.List;

import butterknife.BindView;

public class DoHomeworkActivity extends TopBarBaseActivity implements DoHomeworkView {
    private static final String TAG = "DoHomeworkActivity";

    private DoHomeworkPresenter presenter = new DoHomeworkPresenter(this,this);

    @BindView(R.id.ac_dohomework_recycler)
    RecyclerView recyclerView;
    @BindView(R.id.ac_dohomework_next)
    Button next;
    @BindView(R.id.ac_dohomework_finish)
    Button finish;
    @BindView(R.id.ac_dohomework_index)
    TextView index;

    private QuestionAdapter adapter = new QuestionAdapter(null,this);
    private PagingScrollHelper scrollHelper = new PagingScrollHelper();


    Homework homework;
    String courseID;
    int nowPos = 0;

    @Override
    protected int getContentView() {
        return R.layout.activity_do_homework;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        homework = (Homework) getIntent().getSerializableExtra("homework");
        courseID = getIntent().getStringExtra("courseID");
        setTitle(homework.getTitle());
        index.setText("1/"+homework.getSum());
        presenter.getHomeworkList(courseID,""+homework.getId());

        initView();
        initEvent();
    }

    private void initEvent() {
        setTopLeftButton(this::finish);
        scrollHelper.setOnPageChangeListener(pos -> {
            nowPos = pos;
            index.setText((pos+1)+"/"+homework.getSum());
        });

        next.setOnClickListener(v -> {
            if(adapter.getData().size()>nowPos+1)
                recyclerView.scrollToPosition(++nowPos);
        });

        finish.setOnClickListener(v -> {
            presenter.postAnswer(adapter.getData());
        });
    }

    private void initView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        new LinearSnapHelper().attachToRecyclerView(recyclerView);
        recyclerView.setAdapter(adapter);
        scrollHelper.setUpRecycleView(recyclerView);
    }

    @Override
    public void tokenError(String msg) {

    }

    @Override
    public void showProgress() {

    }

    @Override
    public void dismissProgress() {

    }

    @Override
    public void loadDataSuccess(List<Question> tData) {
        adapter.setNewData(tData);
    }

    @Override
    public void loadDataError(String throwable) {

    }
}
