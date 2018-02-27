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
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.wanhao.aclassapp.R;
import com.example.wanhao.aclassapp.config.ApiConstant;
import com.example.wanhao.aclassapp.fragment.CourseFragment;
import com.example.wanhao.aclassapp.util.FileConvertUtil;
import com.example.wanhao.aclassapp.util.SaveDataUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class CourseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener ,View.OnClickListener{
    private static final String TAG = "CourseActivity";

    @BindView(R.id.ac_course_fab) FloatingActionButton fab;
    @BindView(R.id.ac_course_toolbar) Toolbar toolbar;
    @BindView(R.id.ac_course_drawer_layout) DrawerLayout drawer;
    @BindView(R.id.ac_course_nav_view) NavigationView navigationView;

    private CircleImageView headImage;
    private TextView nameText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);
        ButterKnife.bind(this);
        init();
        initEvent();
    }

    private void init(){
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

    }

    private void initEvent(){
        Bitmap bitmap = FileConvertUtil.getBitmapFromLocal(ApiConstant.USER_AVATAR_NAME);
        if(bitmap!=null){
            headImage.setImageBitmap(bitmap);
        }

        String name = SaveDataUtil.getValueFromSharedPreferences(this, ApiConstant.USER_NAME);
        if(!TextUtils.isEmpty(name)){
            nameText.setText(name);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.course, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            startActivity(new Intent(this,UserMessageActivity.class));
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {
            SaveDataUtil.saveToSharedPreferences(this,ApiConstant.USER_TOKEN,"");
            startActivity(new Intent(this,LodingActivity.class));
            finish();
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
        android.support.v4.app.FragmentManager manager = getSupportFragmentManager();
        CourseFragment fragment = (CourseFragment) manager.findFragmentById(R.id.ac_course_fragment);
        //通过id或者tag可以从manager获取fragment对象，
        Log.i(TAG, "onActivityResult: ");
        if(data!=null)
            fragment.onActivityResult(requestCode, resultCode, data);
    }
}
