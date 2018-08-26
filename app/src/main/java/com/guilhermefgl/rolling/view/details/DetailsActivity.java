package com.guilhermefgl.rolling.view.details;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.guilhermefgl.rolling.R;
import com.guilhermefgl.rolling.databinding.ActivityDetailsBinding;
import com.guilhermefgl.rolling.helper.PicassoHelper;
import com.guilhermefgl.rolling.model.Trip;
import com.guilhermefgl.rolling.model.User;
import com.guilhermefgl.rolling.presenter.details.DetailsPresenter;
import com.guilhermefgl.rolling.presenter.details.DetailsPresenterContract;
import com.guilhermefgl.rolling.view.BaseActivity;
import com.guilhermefgl.rolling.view.widget.TripWidgetProvider;

import java.util.ArrayList;
import java.util.List;

public class DetailsActivity extends BaseActivity implements DetailsViewContract,
        ViewPager.OnPageChangeListener {

    public static final String BUNDLE_TRIP = "BUNDLE_TRIP";

    private static final String BUNDLE_TRIP_ID = "id";
    private static final String LINK_SHARE;
    static {
        LINK_SHARE = "%s\n%s://%s%s?"+ BUNDLE_TRIP_ID + "=%s";
    }

    private Trip mTrip;
    private String mTripID;
    private ActivityDetailsBinding mBinding;
    private DetailsPageAdapter mPageAdapter;
    private DetailsPresenterContract mPresenter;
    private MenuItem mMarkerMenuItem;
    private Boolean mIsMarked;
    private Boolean mIsLogged;

    public static void startActivity(BaseActivity activity, Trip trip) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(BUNDLE_TRIP, trip);
        activity.startActivity(new Intent(activity, DetailsActivity.class).putExtras(bundle));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_details);

        mIsMarked = false;
        if(getIntent().hasExtra(BUNDLE_TRIP)) {
            mTrip = getIntent().getParcelableExtra(BUNDLE_TRIP);
        }

        if (mTrip != null) {
            setupView();
        } else if (getIntent().getData() != null) {
            mTripID = getIntent().getData().getQueryParameter(BUNDLE_TRIP_ID);
            if (mTripID == null) {
                finish();
            }
        }

        mBinding.fabCurrent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPresenter != null) {
                    mPresenter.addTripAsCurrent();
                }
            }
        });
        mBinding.fabShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                share();
            }
        });

        new DetailsPresenter(this);
    }

    @Override
    protected void onDestroy() {
        mPresenter.stop();
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_details, menu);
        mMarkerMenuItem = menu.findItem(R.id.menu_trip_mark);
        setupMenu(mIsMarked);
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
                mMarkerMenuItem.setEnabled(false);
                return true;
            case android.R.id.home:
                supportFinishAfterTransition();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPageSelected(int position) {
        if (mIsLogged != null && mIsLogged && mTrip != null) {
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
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) { }

    @Override
    public void onPageScrollStateChanged(int state) { }

    @Override
    public void setUserIsLogged(boolean isLogged) {
        mIsLogged = isLogged;
        setupIsLoggedView();
    }

    @Override
    public void onLoadTripSuccess(@NonNull Trip trip) {
        mTrip = trip;
        if (!isDestroyed()) {
            setupView();
            mBinding.tripDetailsProgress.setVisibility(View.GONE);
        }
    }

    @Override
    public void onLoadTripFailure() {
        if (!isDestroyed()) {
            Toast.makeText(this, R.string.error_load_trip, Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @Override
    public void onLoadUsersSuccess(@NonNull List<User> users) {
        if (!isDestroyed() && mPageAdapter != null) {
            mPageAdapter.updateUserList((ArrayList<User>) users);
        }
    }

    @Override
    public void onLoadUserFailure() {
        if (!isDestroyed()) {
            Toast.makeText(this, R.string.error_load_user, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onLoadMarkedTripSuccess(@NonNull Boolean isMarked) {
        if (!isDestroyed()) {
            setupMenu(isMarked);
        }
    }

    @Override
    public void onLoadMarkedTripFailure() {
        if (!isDestroyed()) {
            Toast.makeText(this, R.string.error_load_trip, Toast.LENGTH_LONG).show();
            mMarkerMenuItem.setVisible(false);
        }
    }

    @Override
    public void onUpdateMarkedTripSuccess(@NonNull Boolean isMarked) {
        if (!isDestroyed()) {
            setupMenu(isMarked);
        }
    }

    @Override
    public void onUpdateMarkedTripFailure() {
        if (!isDestroyed()) {
            Toast.makeText(this, R.string.error_update_trip, Toast.LENGTH_LONG).show();
            mMarkerMenuItem.setEnabled(true);
        }
    }

    @Override
    public void onUpdateCurrentSuccess() {
        if (!isDestroyed()) {
            TripWidgetProvider.update(getApplicationContext());
            Toast.makeText(this, R.string.details_current_success, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onUpdateCurrentFailure() {
        if (!isDestroyed()) {
            Toast.makeText(this, R.string.error_update_trip, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void setPresenter(@NonNull DetailsPresenterContract presenter) {
        mPresenter = presenter;
        if (mTrip != null) {
            mPresenter.setTrip(mTrip);
            mPresenter.start();
        } else if (mTripID != null) {
            mPresenter.getTrip(mTripID);
            mPresenter.start();
            mBinding.tripDetailsProgress.setVisibility(View.VISIBLE);
            mBinding.fabCurrent.setVisibility(View.GONE);
            mBinding.fabShare.setVisibility(View.GONE);
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

        mPageAdapter = new DetailsPageAdapter(getSupportFragmentManager());
        mPageAdapter.setup(this, mTrip);
        mBinding.detailsPager.setAdapter(mPageAdapter);
        mBinding.tabbedTabLayout.setupWithViewPager(mBinding.detailsPager);
        mBinding.detailsPager.addOnPageChangeListener(this);

        setupIsLoggedView();
    }

    private void setupIsLoggedView() {
        if (mTrip != null) {
            if (mIsLogged == null || !mIsLogged) {
                mBinding.fabCurrent.hide();
                mBinding.fabShare.hide();
            } else {
                if (mBinding.detailsPager.getCurrentItem() == 0) {
                    mBinding.fabCurrent.show();
                    mBinding.fabShare.hide();
                } else if (mBinding.detailsPager.getCurrentItem() == 1) {
                    mBinding.fabCurrent.show();
                    mBinding.fabShare.hide();
                }
            }

            if (mMarkerMenuItem != null) {
                if (mIsLogged == null || !mIsLogged) {
                    mMarkerMenuItem.setVisible(false);
                } else {
                    mMarkerMenuItem.setVisible(true);
                }
            }
        }
    }

    private void setupMenu(boolean isMarked) {
        mIsMarked = isMarked;
        if (mMarkerMenuItem != null) {
            mMarkerMenuItem.setEnabled(true);
            if (isMarked) {
                mMarkerMenuItem.setIcon(R.drawable.ic_marked_white);
            } else {
                mMarkerMenuItem.setIcon(R.drawable.ic_unmarked);
            }
            if (mIsLogged == null || !mIsLogged) {
                mMarkerMenuItem.setVisible(false);
            } else {
                mMarkerMenuItem.setVisible(true);
            }
        }
    }

    private void share() {
        if (mTrip != null) {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType(ACTION_TEXT);
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));

            shareIntent.putExtra(Intent.EXTRA_TEXT,
                    String.format(LINK_SHARE,
                            mTrip.getTripName(),
                            getString(R.string.schema),
                            getString(R.string.host),
                            getString(R.string.path_details),
                            mTrip.getTripId()));

            startActivity(Intent.createChooser(shareIntent, getString(R.string.share_title)));
        }
    }
}
