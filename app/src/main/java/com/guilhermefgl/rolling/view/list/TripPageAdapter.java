package com.guilhermefgl.rolling.view.list;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TripPageAdapter extends FragmentPagerAdapter {

    private final List<TripListFragment> mFragments;

    TripPageAdapter(FragmentManager fm) {
        super(fm);

        mFragments = Arrays.asList(
                TripListFragment.newInstance(TripListFragment.BUNDLE_FILTER_ALL),
                TripListFragment.newInstance(TripListFragment.BUNDLE_FILTER_USER)
        );
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    public void setTripQuery(String query) {
        for (TripListFragment fragment : mFragments) {
            fragment.setQuery(query);
        }
    }
}
