package com.example.wanhao.aclassapp.activity;

import android.content.Intent;
import android.hardware.display.DisplayManager;
import android.media.Image;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.wanhao.aclassapp.R;
import com.example.wanhao.aclassapp.bean.Course;
import com.example.wanhao.aclassapp.config.ApiConstant;
import com.example.wanhao.aclassapp.presenter.CoursePresenter;
import com.example.wanhao.aclassapp.util.ActivityCollector;
import com.example.wanhao.aclassapp.view.CourseView;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.wanhao.aclassapp.config.ApiConstant.ACTIVITY_DATA;
import static com.example.wanhao.aclassapp.config.ApiConstant.COURSE_ID;

public class CourseActivity2 extends AppCompatActivity implements CourseView{
    private static final String TAG = "CourseActivity2";

    @BindView(R.id.ac_course_toolbar)
    Toolbar toolbar;
    @BindView(R.id.ac_course_list)
    RecyclerView recyclerView;
    @BindView(R.id.ac_course_fab)
    FloatingActionButton btMore;

    CoursePresenter presenter;
    String courseId ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course2);
        ActivityCollector.addActivity(this);
        ButterKnife.bind(this);
        presenter = new CoursePresenter(this,this);
        Intent intent = getIntent();
        Course course = (Course) intent.getSerializableExtra(ApiConstant.COURSE_ID);
        toolbar.setTitle(course.getName());
        courseId = course.getId();
        init();
    }

    private void init() {
        btMore.setOnClickListener(v ->
                presenter.openSelectAvatarDialog());
    }

    @Override
    public void startActivity(Class activity,String data) {
        Intent intent = new Intent(this,activity);
        intent.putExtra(COURSE_ID,courseId);

        startActivity(intent);
    }
}
