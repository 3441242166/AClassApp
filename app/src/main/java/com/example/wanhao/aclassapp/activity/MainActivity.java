package com.example.wanhao.aclassapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.wanhao.aclassapp.R;
import com.example.wanhao.aclassapp.config.ApiConstant;
import com.example.wanhao.aclassapp.customizeview.NoScrollViewPager;
import com.example.wanhao.aclassapp.fragment.DocumentFragment;
import com.example.wanhao.aclassapp.fragment.MainFragment;
import com.example.wanhao.aclassapp.fragment.OtherFragment;
import com.example.wanhao.aclassapp.util.ActivityCollector;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @BindView(R.id.ac_main_bottom)
    BottomNavigationView navigation;
    @BindView(R.id.ac_main_viewpager)
    NoScrollViewPager viewPager;
    @BindView(R.id.fg_other_toolbar)
    Toolbar toolbar;


    private OtherFragment otherFragment;
    private DocumentFragment documentFragment;
    private MainFragment mainFragment;
    private List<Fragment> fragmentList;
    private FragmentPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityCollector.addActivity(this);
        ButterKnife.bind(this);
        initView();
        initEvent();
    }

    private void initView() {
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.icon_back);
        fragmentList = new ArrayList<>();
        mainFragment = new MainFragment();
        otherFragment = new OtherFragment();
        documentFragment = new DocumentFragment();

        Bundle bundle = new Bundle();
        bundle.putString(ApiConstant.COURSE_ID,getIntent().getStringExtra(ApiConstant.COURSE_ID));//这里的values就是我们要传的值
        mainFragment.setArguments(bundle);
        otherFragment.setArguments(bundle);
        documentFragment.setArguments(bundle);

        fragmentList.add(mainFragment);
        fragmentList.add(documentFragment);
        fragmentList.add(otherFragment);

        adapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragmentList.get(position);
            }
            @Override
            public int getCount() {
                return fragmentList.size();
            }
        };
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(5);

    }


    private void initEvent() {
        //viewPager.setNoScroll(true);
        viewPager.setOverScrollMode(viewPager.OVER_SCROLL_NEVER);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                invalidateOptionsMenu();
                switch (item.getItemId()) {
                    case R.id.main_menu_home:
                        viewPager.setCurrentItem(0);
                        return true;
                    case R.id.main_menu_message:
                        viewPager.setCurrentItem(1);
                        return true;
                    case R.id.main_menu_bbs:
                        viewPager.setCurrentItem(2);
                        return true;
                }
                return false;
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        navigation.setSelectedItemId(R.id.main_menu_home);
                        break;
                    case 1:
                        navigation.setSelectedItemId(R.id.main_menu_message);
                        break;
                    case 2:
                        navigation.setSelectedItemId(R.id.main_menu_bbs);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        Log.i(TAG, "onPrepareOptionsMenu: ");
        // 动态设置ToolBar状态
        switch (viewPager.getCurrentItem()) {
            case 0:
                menu.findItem(R.id.main_toolbar_one).setVisible(true);
                menu.findItem(R.id.main_toolbar_two).setVisible(false);
                menu.findItem(R.id.main_toolbar_three).setVisible(false);
                break;
            case 1:
                menu.findItem(R.id.main_toolbar_one).setVisible(false);
                menu.findItem(R.id.main_toolbar_two).setVisible(true);
                menu.findItem(R.id.main_toolbar_three).setVisible(false);
                break;
            case 2:
                menu.findItem(R.id.main_toolbar_one).setVisible(false);
                menu.findItem(R.id.main_toolbar_two).setVisible(false);
                menu.findItem(R.id.main_toolbar_three).setVisible(true);
                break;
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home){
            finish();
        }
        //noinspection SimplifiableIfStatement
        if (id == R.id.main_toolbar_one) {

            return true;
        }
        if (id == R.id.main_toolbar_two) {
            startActivity(new Intent(MainActivity.this,DocumentActivity.class));
            return true;
        }
        if (id == R.id.main_toolbar_three) {

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        //Toolbar 必须在onCreate()之后设置标题文本，否则默认标签将覆盖我们的设置
        if (toolbar != null) {
            toolbar.setTitle(getIntent().getStringExtra(ApiConstant.COURSE_NAME));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
