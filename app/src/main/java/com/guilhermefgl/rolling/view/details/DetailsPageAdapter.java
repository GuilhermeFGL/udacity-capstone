package com.guilhermefgl.rolling.view.details;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.guilhermefgl.rolling.R;
import com.guilhermefgl.rolling.model.Trip;

public class DetailsPageAdapter extends FragmentPagerAdapter {

    @Nullable
    private Trip mTrip;
    @Nullable
    private Context mContext;

    DetailsPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public Fragment getItem(int position) {
        if (mTrip != null) {
            switch (position) {
                case 0:
                    return TripDetailsFragment.newInstance(mTrip);
                case 1:
                    return UserDetailsFragment.newInstance(mTrip);
            }
        }
        return null;
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
}
