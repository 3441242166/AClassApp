package com.example.wanhao.aclassapp.activity;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.hardware.display.DisplayManager;
import android.media.Image;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.wanhao.aclassapp.R;
import com.example.wanhao.aclassapp.adapter.ChatAdapter;
import com.example.wanhao.aclassapp.base.BaseActivity;
import com.example.wanhao.aclassapp.base.BaseTokenActivity;
import com.example.wanhao.aclassapp.bean.ChatBean;
import com.example.wanhao.aclassapp.bean.Course;
import com.example.wanhao.aclassapp.config.ApiConstant;
import com.example.wanhao.aclassapp.presenter.CoursePresenter;
import com.example.wanhao.aclassapp.service.DownDocumentService;
import com.example.wanhao.aclassapp.util.ActivityCollector;
import com.example.wanhao.aclassapp.util.NotificationUtils;
import com.example.wanhao.aclassapp.view.CourseView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.wanhao.aclassapp.config.ApiConstant.ACTIVITY_DATA;
import static com.example.wanhao.aclassapp.config.ApiConstant.COURSE_ID;

public class CourseActivity extends BaseTokenActivity implements CourseView{
    private static final String TAG = "CourseActivity";

    @BindView(R.id.ac_course_toolbar)
    Toolbar toolbar;
    @BindView(R.id.ac_course_list)
    RecyclerView recyclerView;
    @BindView(R.id.ac_course_fab)
    FloatingActionButton btMore;

    CoursePresenter presenter;
    ChatAdapter adapter;
    String courseId ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course2);
        ButterKnife.bind(this);
        presenter = new CoursePresenter(this,this);

        init();
        initEvent();
    }

    private void initEvent() {
        btMore.setOnClickListener(v ->
                presenter.openSelectAvatarDialog());
    }

    private void init() {
        Course course = (Course) getIntent().getSerializableExtra(ApiConstant.COURSE_ID);
        toolbar.setTitle(course.getName());
        courseId = course.getId();

        adapter = new ChatAdapter(null,this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<ChatBean> chat = new ArrayList<>();

        for(int x=0;x<20;x++){
            ChatBean chatBean = new ChatBean();
            if(x%2==0)
                chatBean.setItemType(ChatBean.ME);
            else
                chatBean.setItemType(ChatBean.OTHER);
            chat.add(chatBean);
        }

        adapter.setNewData(chat);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void startActivity(Class activity,String data) {
        Intent intent = new Intent(this,activity);
        intent.putExtra(COURSE_ID,courseId);

        startActivity(intent);
    }
}
