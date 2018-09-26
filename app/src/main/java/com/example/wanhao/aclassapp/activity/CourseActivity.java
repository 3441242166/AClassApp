package com.example.wanhao.aclassapp.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wanhao.aclassapp.R;
import com.example.wanhao.aclassapp.base.BaseApplication;
import com.example.wanhao.aclassapp.base.BaseTokenActivity;
import com.example.wanhao.aclassapp.config.ApiConstant;
import com.example.wanhao.aclassapp.fragment.CourseListFragment;
import com.example.wanhao.aclassapp.presenter.CoursePresenters;
import com.example.wanhao.aclassapp.util.ActivityCollector;
import com.example.wanhao.aclassapp.util.SaveDataUtil;
import com.example.wanhao.aclassapp.view.ICourseView;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class CourseActivity extends BaseTokenActivity implements NavigationView.OnNavigationItemSelectedListener ,View.OnClickListener,ICourseView {
    private static final String TAG = "CourseActivity";

    @BindView(R.id.ac_course_fab) FloatingActionButton fab;
    @BindView(R.id.ac_course_toolbar) Toolbar toolbar;
    @BindView(R.id.ac_course_drawer_layout) DrawerLayout drawer;
    @BindView(R.id.ac_course_nav_view) NavigationView navigationView;

    private CircleImageView headImage;
    private TextView nameText;

    private CoursePresenters presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);
        ButterKnife.bind(this);
        init();
        presenter.getData();
    }

    private void init(){
        presenter = new CoursePresenters(this,this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        fab.setOnClickListener(this);
        navigationView.setNavigationItemSelectedListener(this);

        View view = navigationView.getHeaderView(0);
        headImage = view.findViewById(R.id.ac_course_headimage);
        nameText = view.findViewById(R.id.ac_course_name);

        headImage.setOnClickListener(view1 -> startActivity(new Intent(CourseActivity.this,UserMessageActivity.class)));
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_message) {
            startActivity(new Intent(this,UserMessageActivity.class));
        } else if (id == R.id.nav_document) {
            startActivity(new Intent(this,DocumentActivity.class));
        }else if (id == R.id.nav_send) {
            SaveDataUtil.saveToSharedPreferences(this,ApiConstant.USER_TOKEN,"");
            startActivity(new Intent(this,LodingActivity.class));
            finish();
        }else if(id == R.id.nav_homework){
            startActivity(new Intent(this,AssignmentActivity.class));
        }

        DrawerLayout drawer = findViewById(R.id.ac_course_drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private long exitTime = 0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            if((System.currentTimeMillis()-exitTime) > 2000){
                Snackbar.make(fab, "再按一次退出", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ac_course_fab:
                startActivityForResult(new Intent(this,AddCourseActivity.class),0);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode){
            case ApiConstant.ADD_SUCCESS:
                android.support.v4.app.FragmentManager manager = getSupportFragmentManager();
                CourseListFragment fragment = (CourseListFragment) manager.findFragmentById(R.id.ac_course_fragment);
                //通过id或者tag可以从manager获取fragment对象，
                if(data!=null)
                    fragment.onActivityResult(requestCode, resultCode, data);
        }

    }

    @Override
    protected void onResume(){
        super.onResume();
        presenter.getData();
    }

    @Override
    public void setHead(Bitmap bitmap) {
        headImage.setImageBitmap(bitmap);
    }

    @Override
    public void setName(String name) {
        nameText.setText(name);
    }

    @Override
    public void tokenError() {
        Toast.makeText(BaseApplication.getContext(),"token失效，请重新登陆", Toast.LENGTH_SHORT).show();
        SaveDataUtil.saveToSharedPreferences(this,ApiConstant.USER_TOKEN,"");
        Intent intent = new Intent(this, LodingActivity.class);
        ActivityCollector.finishAll();
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
