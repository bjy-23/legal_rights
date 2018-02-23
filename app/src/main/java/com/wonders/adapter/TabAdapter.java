package com.wonders.adapter;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;
import java.util.List;

/**
 * Created by chan on 10/11/16.
 */
public class TabAdapter extends FragmentPagerAdapter {
    private String[] titles = {"检查信息", "监管记录", "巡查录入", "历史不合格信息"};
    private List<Fragment> fragments;

    public TabAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);

        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
