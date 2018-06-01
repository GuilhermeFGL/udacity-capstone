package com.guilhermefgl.rolling.view.main;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;

import com.guilhermefgl.rolling.R;
import com.guilhermefgl.rolling.databinding.ActivityMainBinding;
import com.guilhermefgl.rolling.view.BaseActivity;
import com.guilhermefgl.rolling.view.BaseFragment;
import com.guilhermefgl.rolling.view.current.CurrentFragment;
import com.guilhermefgl.rolling.view.profile.ProfileFragment;
import com.guilhermefgl.rolling.view.triplist.TripListFragment;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ActivityMainBinding mBinding;
    private FragmentManager mFragmentManager;

    public static void startActivity(BaseActivity activity) {
        activity.startActivity(
                new Intent(activity, MainActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
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
    }

    @Override
    public void onBackPressed() {
        if (mBinding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            mBinding.drawerLayout.closeDrawer(GravityCompat.START);
            return;
        }

        String initialFragmentTag = generateFragmentTag(R.id.navigation_trip_list);
        Fragment initialFragment = mFragmentManager.findFragmentByTag(initialFragmentTag);
        if (initialFragment != null && !initialFragment.isVisible()) {
            replaceFragment(
                    TripListFragment.newInstance(),
                    initialFragmentTag,
                    getString(R.string.navigation_trip_list));
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
                return TripListFragment.newInstance();
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
}
