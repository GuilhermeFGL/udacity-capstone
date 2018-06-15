package com.guilhermefgl.rolling.view.details;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.guilhermefgl.rolling.R;
import com.guilhermefgl.rolling.databinding.ActivityDetailsBinding;
import com.guilhermefgl.rolling.helper.PicassoHelper;
import com.guilhermefgl.rolling.model.Trip;
import com.guilhermefgl.rolling.view.BaseActivity;

public class DetailsActivity extends BaseActivity implements ViewPager.OnPageChangeListener {

    public static final String BUNDLE_TRIP = "BUNDLE_TRIP";

    private Trip mTrip;
    private ActivityDetailsBinding mBinding;

    public static void startActivity(BaseActivity activity, Trip trip) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(BUNDLE_TRIP, trip);
        activity.startActivity(
                new Intent(activity, DetailsActivity.class)
                        .putExtras(bundle));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_details);

        if(getIntent().hasExtra(BUNDLE_TRIP)) {
            mTrip = getIntent().getParcelableExtra(BUNDLE_TRIP);
        }

        if (mTrip == null) {
            finish();
        } else {
            setupView();
        }

    }

    private void setupView() {
        setSupportActionBar(mBinding.tabbedToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setTitle(mTrip.getTripName());
        }

        PicassoHelper.loadImage(mTrip.getTripBannerUrl(), mBinding.backdrop);

        DetailsPageAdapter pageAdapter = new DetailsPageAdapter(getSupportFragmentManager());
        pageAdapter.setup(this, mTrip);
        mBinding.detailsPager.setAdapter(pageAdapter);
        mBinding.tabbedTabLayout.setupWithViewPager(mBinding.detailsPager);
        mBinding.detailsPager.addOnPageChangeListener(this);
    }

    @Override
    public void onPageSelected(int position) {
        switch (position) {
            case 0:
                mBinding.fabCurrent.setVisibility(View.GONE);
                mBinding.fabCurrent.setVisibility(View.VISIBLE);
                break;
            case 1:
                mBinding.fabCurrent.setVisibility(View.VISIBLE);
                mBinding.fabCurrent.setVisibility(View.GONE);
                break;
        }

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) { }

    @Override
    public void onPageScrollStateChanged(int state) { }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
