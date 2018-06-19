package com.guilhermefgl.rolling.view.main;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.guilhermefgl.rolling.R;
import com.guilhermefgl.rolling.databinding.ActivityMainBinding;
import com.guilhermefgl.rolling.helper.PicassoHelper;
import com.guilhermefgl.rolling.mock.UserMock;
import com.guilhermefgl.rolling.model.User;
import com.guilhermefgl.rolling.view.BaseActivity;
import com.guilhermefgl.rolling.view.BaseFragment;
import com.guilhermefgl.rolling.view.current.CurrentFragment;
import com.guilhermefgl.rolling.view.list.TripPageFragment;
import com.guilhermefgl.rolling.view.profile.ProfileFragment;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        TripPageFragment.TripListFragmentInteractionListener, ViewPager.OnPageChangeListener {

    // TODO remove mock data
    User mockLoggedUser = UserMock.getLogeedUser();

    private ActivityMainBinding mBinding;
    private FragmentManager mFragmentManager;

    public static void startActivity(BaseActivity activity) {
        activity.startActivity(new Intent(activity, MainActivity.class));
        activity.finish();
        activity.overridePendingTransition( 0, R.anim.fade_out);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        mFragmentManager = getSupportFragmentManager();

        setSupportActionBar(mBinding.mainToolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mBinding.drawerLayout, mBinding.mainToolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mBinding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        mBinding.navView.setNavigationItemSelectedListener(this);
        mBinding.navView.setCheckedItem(R.id.navigation_trip_list);

        mBinding.mainAppBar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                mBinding.mainBottomNavigation.setTranslationY(verticalOffset*-1);
            }
        });

        updateLayoutLoggedUser(mockLoggedUser);

        replaceFragment(
                TripPageFragment.newInstance(),
                generateFragmentTag(R.id.navigation_trip_list),
                getString(R.string.navigation_trip_list));
    }

    @Override
    public void onBackPressed() {
        if (mBinding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            mBinding.drawerLayout.closeDrawer(GravityCompat.START);
            return;
        }

        String initialFragmentTag = generateFragmentTag(R.id.navigation_trip_list);
        Fragment initialFragment = mFragmentManager.findFragmentByTag(initialFragmentTag);
        if (initialFragment == null) {
            replaceFragment(
                    TripPageFragment.newInstance(),
                    initialFragmentTag,
                    getString(R.string.navigation_trip_list));
            mBinding.navView.setCheckedItem(R.id.navigation_trip_list);
            return;
        }

        super.onBackPressed();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        BaseFragment fragment = getFragmentById(item.getItemId());
        String tag = generateFragmentTag(item.getItemId());

        if (fragment == null || mFragmentManager.findFragmentByTag(tag) != null) {
            return false;
        }

        replaceFragment(fragment, tag, item.getTitle());
        return true;
    }

    @Nullable
    private BaseFragment getFragmentById(int id) {
        switch (id) {
            case R.id.navigation_trip_list:
                return TripPageFragment.newInstance();
            case R.id.navigation_trip_current:
                return CurrentFragment.newInstance();
            case R.id.navigation_profile:
                return ProfileFragment.newInstance();
            case R.id.navigation_login:
                return null;
            case R.id.navigation_logout:
                return null;
            default:
                return null;
        }
    }

    @Override
    public void onPageSelected(int position) {
        switch (position) {
            case 0:
                mBinding.mainBottomNavigation.setSelectedItemId(R.id.navigation_recent);
                break;
            case 1:
                mBinding.mainBottomNavigation.setSelectedItemId(R.id.navigation_marked);
                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) { }

    @Override
    public void onPageScrollStateChanged(int state) { }

    @Override
    public void setupTripListFragment(BottomNavigationView.OnNavigationItemSelectedListener listener) {
        mBinding.mainBottomNavigation.setVisibility(View.VISIBLE);
        mBinding.mainBottomNavigation.setOnNavigationItemSelectedListener(listener);
    }

    @Override
    public void unSetupTripListFragment() {
        mBinding.mainBottomNavigation.setVisibility(View.GONE);
        mBinding.mainBottomNavigation.setOnNavigationItemSelectedListener(null);
    }

    @Override
    public ViewPager.OnPageChangeListener getOnPageChangeListener() {
        return this;
    }

    private String generateFragmentTag(int tag) {
        return String.valueOf(tag);
    }

    private void replaceFragment(BaseFragment fragment, String tag, CharSequence title) {
        mFragmentManager.beginTransaction()
                .replace(R.id.main_content, fragment, tag)
                .commit();
        setTitle(title);
        mBinding.drawerLayout.closeDrawer(GravityCompat.START);
    }

    private void updateLayoutLoggedUser(User user) {
        if (user != null) {
            ((TextView) mBinding.navView.getHeaderView(0)
                    .findViewById(R.id.navigation_profile_name))
                    .setText(user.getUserName());
            ((TextView) mBinding.navView.getHeaderView(0)
                    .findViewById(R.id.navigation_profile_email))
                    .setText(user.getUserEmail());
            PicassoHelper.loadImage(user.getUserAvatarUrl(),
                    ((ImageView) mBinding.navView.getHeaderView(0)
                            .findViewById(R.id.navigation_profile_avatar)));
            mBinding.navView.getMenu().findItem(R.id.navigation_login).setVisible(false);
            mBinding.navView.getMenu().findItem(R.id.navigation_logout).setVisible(true);
        } else {
            ((TextView) mBinding.navView.getHeaderView(0)
                    .findViewById(R.id.navigation_profile_name)).setText("");
            ((TextView) mBinding.navView.getHeaderView(0)
                    .findViewById(R.id.navigation_profile_email)).setText("");
            ((ImageView) mBinding.navView.getHeaderView(0)
                    .findViewById(R.id.navigation_profile_email))
                    .setImageResource(R.mipmap.ic_launcher_foreground);
            mBinding.navView.getMenu().findItem(R.id.navigation_login).setVisible(true);
            mBinding.navView.getMenu().findItem(R.id.navigation_logout).setVisible(false);
        }
    }
}
