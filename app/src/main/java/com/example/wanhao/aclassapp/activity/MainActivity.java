package com.example.wanhao.aclassapp.activity;

import android.content.Intent;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.example.wanhao.aclassapp.R;
import com.example.wanhao.aclassapp.base.BaseMvpActivity;
import com.example.wanhao.aclassapp.base.IBasePresenter;
import com.example.wanhao.aclassapp.customizeview.NoScrollViewPager;
import com.example.wanhao.aclassapp.fragment.CourseListFragment;
import com.example.wanhao.aclassapp.fragment.DateFragment;
import com.example.wanhao.aclassapp.fragment.TestFragment;
import com.example.wanhao.aclassapp.fragment.TipFragment;
import com.example.wanhao.aclassapp.fragment.UserMessageFragment;
import com.example.wanhao.aclassapp.util.ActivityCollector;
import com.example.wanhao.aclassapp.util.BottomNavigationViewHelper;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseMvpActivity {
    private static final String TAG = "MainActivity";

    @BindView(R.id.ac_main_bottom)
    BottomNavigationView navigation;
    @BindView(R.id.ac_main_viewpager)
    NoScrollViewPager viewPager;

    CourseListFragment courseFragment;
    DateFragment dateFragment;
    TipFragment tipFragment;
    UserMessageFragment userMessageFragment;

    private List<Fragment> fragmentList;

    @Override
    protected IBasePresenter setPresenter() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityCollector.addActivity(this);
        ButterKnife.bind(this);

        init();
        initEvent();
    }

    private void initEvent() {
        viewPager.setOverScrollMode(View.OVER_SCROLL_NEVER);

        navigation.setOnNavigationItemSelectedListener(item -> {
            invalidateOptionsMenu();
            switch (item.getItemId()) {
                case R.id.main_menu_home:
                    viewPager.setCurrentItem(0);
                    return true;
                case R.id.main_menu_date:
                    viewPager.setCurrentItem(1);
                    return true;
                case R.id.main_menu_message:
                    viewPager.setCurrentItem(2);
                    return true;
                case R.id.main_menu_my:
                    viewPager.setCurrentItem(3);
                    return true;
            }
            return false;
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
                        navigation.setSelectedItemId(R.id.main_menu_date);
                        break;
                    case 2:
                        navigation.setSelectedItemId(R.id.main_menu_message);
                        break;
                    case 3:
                        navigation.setSelectedItemId(R.id.main_menu_my);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private void init() {
        fragmentList = new ArrayList<>();
        courseFragment = new CourseListFragment();
        userMessageFragment = new UserMessageFragment();
        tipFragment = new TipFragment();
        dateFragment = new DateFragment();

        fragmentList.add(courseFragment);
        //fragmentList.add(dateFragment);
        fragmentList.add(new TestFragment());
        fragmentList.add(tipFragment);
        fragmentList.add(userMessageFragment);

        BottomNavigationViewHelper.disableShiftMode(navigation);

        FragmentPagerAdapter adapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
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
        viewPager.setOffscreenPageLimit(3);
    }

    private long exitTime = 0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            if((System.currentTimeMillis()-exitTime) > 2000){
                Toast.makeText(this,"再按一次退出",Toast.LENGTH_SHORT).show();
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        for(Fragment fragment:fragmentList){
            fragment.onActivityResult(requestCode, resultCode, data);
        }

    }

}
