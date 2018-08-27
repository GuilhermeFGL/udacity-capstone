package com.guilhermefgl.rolling.view.details;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.guilhermefgl.rolling.R;
import com.guilhermefgl.rolling.model.Trip;
import com.guilhermefgl.rolling.model.User;
import com.guilhermefgl.rolling.view.BaseFragment;

import java.util.ArrayList;

public class DetailsPageAdapter extends FragmentPagerAdapter {

    @Nullable
    private Trip mTrip;
    @Nullable
    private Context mContext;

    private UserDetailsFragment mUserPage;

    DetailsPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public Fragment getItem(int position) {
        BaseFragment pageFragment = null;
        if (mTrip != null) {
            switch (position) {
                case 0:
                    pageFragment = TripDetailsFragment.newInstance(mTrip);
                    break;
                case 1:
                    pageFragment = UserDetailsFragment.newInstance(new ArrayList<User>());
                    mUserPage = (UserDetailsFragment) pageFragment;
                    break;
            }
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

    public void setup(Context context, Trip trip) {
        mTrip = trip;
        mContext = context;
    }

    public void updateUserList(ArrayList<User> users) {
        if (mUserPage != null) {
            mUserPage.updateUserList(users);
        }
    }
}
