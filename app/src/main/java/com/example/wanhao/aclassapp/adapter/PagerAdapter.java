package com.example.wanhao.aclassapp.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by wanhao on 2017/10/3.
 */

public class PagerAdapter extends FragmentPagerAdapter {

    List<Fragment> list;
    String[]ar ={"预习资料","课程资料"};

    public PagerAdapter(FragmentManager fm, List<Fragment> list) {
        super(fm);
        this.list = list;
    }

    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return ar[position];
    }
}
