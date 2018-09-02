package com.guilhermefgl.rolling.view.list;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.ViewGroup;

import java.util.Arrays;
import java.util.List;

public class TripPageAdapter extends FragmentPagerAdapter {

    private final List<TripListFragment> mFragments;
    private final FragmentManager mFragmentManager;

    TripPageAdapter(FragmentManager fm) {
        super(fm);

        mFragmentManager = fm;
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

    @Override
    public long getItemId(int position) {
        return System.currentTimeMillis();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        FragmentTransaction trans = mFragmentManager.beginTransaction();
        trans.remove(mFragments.get(position));
        trans.commit();
        mFragments.set(position, null);
    }

    @NonNull
    @Override
    public Fragment instantiateItem(ViewGroup container, int position){
        Fragment fragment = getItem(position);
        FragmentTransaction trans = mFragmentManager.beginTransaction();
        trans.add(container.getId(), fragment, String.valueOf(position));
        trans.commit();
        return fragment;
    }

    @Override
    public boolean isViewFromObject(View view, Object fragment) {
        return ((Fragment) fragment).getView() == view;
    }


    public void setTripQuery(String query) {
        for (TripListFragment fragment : mFragments) {
            fragment.setQuery(query);
        }
    }


}
