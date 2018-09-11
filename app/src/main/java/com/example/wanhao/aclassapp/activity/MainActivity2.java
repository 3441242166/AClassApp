package com.example.wanhao.aclassapp.activity;

import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.wanhao.aclassapp.R;
import com.example.wanhao.aclassapp.customizeview.NoScrollViewPager;
import com.example.wanhao.aclassapp.fragment.CourseListFragment;
import com.example.wanhao.aclassapp.fragment.DateFragment;
import com.example.wanhao.aclassapp.fragment.TipFragment;
import com.example.wanhao.aclassapp.fragment.UserMessageFragment;
import com.example.wanhao.aclassapp.util.ActivityCollector;
import com.example.wanhao.aclassapp.util.BottomNavigationViewHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity2 extends AppCompatActivity {
    private static final String TAG = "MainActivity2";

    @BindView(R.id.ac_main_bottom)
    BottomNavigationView navigation;
    @BindView(R.id.ac_main_viewpager)
    NoScrollViewPager viewPager;

    CourseListFragment courseFragment;
    DateFragment dateFragment;
    TipFragment tipFragment;
    UserMessageFragment userMessageFragment;

    private List<Fragment> fragmentList;
    private FragmentPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
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
        fragmentList.add(dateFragment);
        fragmentList.add(tipFragment);
        fragmentList.add(userMessageFragment);

        BottomNavigationViewHelper.disableShiftMode(navigation);

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
        viewPager.setOffscreenPageLimit(3);
    }
}
