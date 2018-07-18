package com.guilhermefgl.rolling.view.details;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.guilhermefgl.rolling.R;
import com.guilhermefgl.rolling.databinding.ActivityDetailsBinding;
import com.guilhermefgl.rolling.helper.PicassoHelper;
import com.guilhermefgl.rolling.model.Trip;
import com.guilhermefgl.rolling.model.User;
import com.guilhermefgl.rolling.presenter.details.DetailsPresenter;
import com.guilhermefgl.rolling.presenter.details.DetailsPresenterContract;
import com.guilhermefgl.rolling.view.BaseActivity;

import java.util.List;

public class DetailsActivity extends BaseActivity implements DetailsViewContract,
        ViewPager.OnPageChangeListener {

    public static final String BUNDLE_TRIP = "BUNDLE_TRIP";

    private Trip mTrip;
    private ActivityDetailsBinding mBinding;
    private DetailsPresenterContract mPresenter;
    private MenuItem mMarkerMenuItem;
    private Boolean mIsMarked;

    public static void startActivity(BaseActivity activity, Trip trip, ActivityOptionsCompat options) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(BUNDLE_TRIP, trip);
        if (options != null) {
            activity.startActivity(
                    new Intent(activity, DetailsActivity.class).putExtras(bundle),
                    options.toBundle());
        } else {
            activity.startActivity(new Intent(activity, DetailsActivity.class).putExtras(bundle));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_details);

        mIsMarked = false;
        if(getIntent().hasExtra(BUNDLE_TRIP)) {
            mTrip = getIntent().getParcelableExtra(BUNDLE_TRIP);
        }

        if (mTrip == null) {
            finish();
        } else {
            setupView();
        }

        new DetailsPresenter(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_details, menu);
        mMarkerMenuItem = menu.findItem(R.id.menu_trip_mark);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_trip_mark:
                if (mIsMarked) {
                    mPresenter.removeTripAsMarked();
                } else {
                    mPresenter.addTripAsMarked();
                }
                return true;
            case android.R.id.home:
                supportFinishAfterTransition();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPageSelected(int position) {
        switch (position) {
            case 0:
                mBinding.fabCurrent.show();
                mBinding.fabShare.hide();
                break;
            case 1:
                mBinding.fabCurrent.hide();
                mBinding.fabShare.show();
                break;
        }

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) { }

    @Override
    public void onPageScrollStateChanged(int state) { }

    @Override
    public void onLoadTripSuccess(@NonNull Trip trip) {
        mTrip = trip;
    }

    @Override
    public void onLoadTripFailure() {
        if (isForeground()) {
            Toast.makeText(this, R.string.error_load_trip, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onLoadUsersSuccess(@NonNull List<User> users) {
    }

    @Override
    public void onLoadUserFailure() {
        if (isForeground()) {
            Toast.makeText(this, R.string.error_load_user, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onLoadMarkedTripSuccess(@NonNull Boolean isMarked) {
        if (isForeground()) {
            setupMenu(isMarked);
        }
    }

    @Override
    public void onLoadMarkedTripFailure() {
        if (isForeground()) {
            Toast.makeText(this, R.string.error_load_trip, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onUpdateMarkedTripSuccess(@NonNull Boolean isMarked) {
        if (isForeground()) {
            setupMenu(isMarked);
        }
    }

    @Override
    public void onUpdateMarkedTripFailure() {
        if (isForeground()) {
            Toast.makeText(this, R.string.error_update_trip, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void setPresenter(@NonNull DetailsPresenterContract presenter) {
        mPresenter = presenter;
        if (mTrip != null) {
            mPresenter.setTrip(mTrip);
        }
    }

    private void setupView() {
        mBinding.tabbedActionbar.setTitle(mTrip.getTripName());
        mBinding.tabbedAppBar.setExpanded(true, true);
        setSupportActionBar(mBinding.tabbedToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }

        PicassoHelper.loadImage(mTrip.getTripBannerUrl(), mBinding.backdrop);

        DetailsPageAdapter pageAdapter = new DetailsPageAdapter(getSupportFragmentManager());
        pageAdapter.setup(this, mTrip);
        mBinding.detailsPager.setAdapter(pageAdapter);
        mBinding.tabbedTabLayout.setupWithViewPager(mBinding.detailsPager);
        mBinding.detailsPager.addOnPageChangeListener(this);
    }

    private void setupMenu(boolean isMarked) {
        mIsMarked = isMarked;
        if (mMarkerMenuItem != null) {
            if (isMarked) {
                mMarkerMenuItem.setIcon(R.drawable.ic_marked_white);
            } else {
                mMarkerMenuItem.setIcon(R.drawable.ic_unmarked);
            }
        }
    }
}
