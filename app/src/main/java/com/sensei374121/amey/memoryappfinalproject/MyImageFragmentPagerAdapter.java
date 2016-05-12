package com.sensei374121.amey.memoryappfinalproject;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

public class MyImageFragmentPagerAdapter extends FragmentPagerAdapter {
    int count;
    List<String> mylist;

    public MyImageFragmentPagerAdapter(FragmentManager fm, List<String> mylist) {
        super(fm);
        //this.count = count;
        this.mylist = mylist;

    }

    @Override
    public Fragment getItem(int position) {
        return ImageViewPagerFragment.newInstance(mylist.get(position));
    }

    @Override
    public int getCount() {
        return mylist.size();
    }
}
