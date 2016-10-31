package com.example.tby.test2;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * Created by tby on 2016/10/30.
 */

public class myPagerAdapter extends FragmentStatePagerAdapter {
    private List<fragmentNews>fragList;
    private List<String>titleList;
    public myPagerAdapter(FragmentManager fm, List<fragmentNews> fragList, List<String>titleList) {
        super(fm);
        // TODO Auto-generated constructor stub
        this.fragList=fragList;
        this.titleList=titleList;
    }

    @Override
    public Fragment getItem(int arg0) {
        // TODO Auto-generated method stub
        return fragList.get(arg0);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return fragList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // TODO Auto-generated method stub
        return titleList.get(position);
    }
}
