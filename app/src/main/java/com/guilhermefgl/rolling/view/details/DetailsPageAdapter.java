package com.guilhermefgl.rolling.view.details;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.ViewGroup;

import com.guilhermefgl.rolling.R;
import com.guilhermefgl.rolling.model.Trip;
import com.guilhermefgl.rolling.model.User;
import com.guilhermefgl.rolling.view.BaseFragment;

import java.util.ArrayList;
import java.util.List;

public class DetailsPageAdapter extends FragmentPagerAdapter {
    ;
    @Nullable
    private Trip mTrip;
    @Nullable
    private Context mContext;

    private UserDetailsFragment mUserPage;
    private final FragmentManager mFragmentManager;
    private final List<BaseFragment> mFragments;
    private ArrayList<User> mUsers;

    DetailsPageAdapter(FragmentManager fm) {
        super(fm);
        mFragmentManager = fm;
        mFragments = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public Fragment getItem(int position) {
        BaseFragment pageFragment = null;
        if (mTrip != null) {
            pageFragment = mFragments.get(position);
        }
        return pageFragment;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if (mContext != null){
            return mContext.getResources().getStringArray(R.array.details_tab_titles)[position];
        }
        return null;
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

    public void setup(Context context, Trip trip) {
        mTrip = trip;
        mContext = context;
        mFragments.add(TripDetailsFragment.newInstance(mTrip));
        mFragments.add(UserDetailsFragment.newInstance(mUsers != null ?
                mUsers : new ArrayList<User>()));
        mUserPage = (UserDetailsFragment) mFragments.get(1);
    }

    public void updateUserList(ArrayList<User> users) {
        mUsers = users;
        if (mUserPage != null) {
            mUserPage.updateUserList(users);
        }
    }
}
