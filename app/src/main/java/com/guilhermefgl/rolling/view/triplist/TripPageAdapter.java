package com.guilhermefgl.rolling.view.triplist;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class TripPageAdapter extends FragmentPagerAdapter {

    TripPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "test";
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return TripListFragment.newInstance(TripListFragment.BUNDLE_FILTER_ALL);
            case 1:
                return TripListFragment.newInstance(TripListFragment.BUNDLE_FILTER_USER);
        }
        return null;
    }
}
