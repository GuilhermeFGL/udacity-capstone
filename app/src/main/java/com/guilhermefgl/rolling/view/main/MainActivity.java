package com.guilhermefgl.rolling.view.main;

import android.content.Intent;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.guilhermefgl.rolling.R;
import com.guilhermefgl.rolling.databinding.ActivityMainBinding;
import com.guilhermefgl.rolling.helper.CompressBitmap;
import com.guilhermefgl.rolling.helper.PicassoHelper;
import com.guilhermefgl.rolling.helper.contracts.PickImageInteractionListener;
import com.guilhermefgl.rolling.model.User;
import com.guilhermefgl.rolling.presenter.main.MainPresenter;
import com.guilhermefgl.rolling.presenter.main.MainPresenterContract;
import com.guilhermefgl.rolling.view.BaseActivity;
import com.guilhermefgl.rolling.view.BaseFragment;
import com.guilhermefgl.rolling.view.BasePickImageFragment;
import com.guilhermefgl.rolling.view.current.CurrentFragment;
import com.guilhermefgl.rolling.view.list.TripPageFragment;
import com.guilhermefgl.rolling.view.profile.ProfileFragment;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends BaseActivity
        implements MainViewContract, NavigationView.OnNavigationItemSelectedListener, DrawerLayout.DrawerListener,
        TripPageFragment.TripListFragmentInteractionListener, PickImageInteractionListener,
        ViewPager.OnPageChangeListener, ProfileFragment.ProfileFragmentInteractionListener,
        CurrentFragment.CurrentFragmentInteractionListener {

    private static final Integer REQUEST_LOGIN = 1001;
    private static final Float VERTICAL_OFFSET_PORTRAIT = 1f;
    private static final Float VERTICAL_OFFSET_LANDSCAPE = 1.15f;

    private ActivityMainBinding mBinding;
    private FragmentManager mFragmentManager;
    private MainPresenterContract mPresenter;
    private MenuItem mMenuFragmentToSet;

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
        mBinding.drawerLayout.addDrawerListener(this);

        mBinding.mainAppBar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                float offset = getResources().getConfiguration().orientation
                        == Configuration.ORIENTATION_PORTRAIT ?
                        VERTICAL_OFFSET_PORTRAIT : VERTICAL_OFFSET_LANDSCAPE;
                mBinding.mainBottomNavigation.setTranslationY(verticalOffset * -offset);
            }
        });

        if(getSupportFragmentManager().getFragments().isEmpty()) {
            goToDefaultFragment();
        } else {
            Fragment initialFragment = mFragmentManager.findFragmentByTag(
                    generateFragmentTag(R.id.navigation_trip_list));
            if (initialFragment != null) {
                setupTripListFragment(
                        (BottomNavigationView.OnNavigationItemSelectedListener) initialFragment);
            }
        }

        new MainPresenter(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_LOGIN) {
            if (resultCode == RESULT_OK) {
                mPresenter.refresh();
            } else {
                Toast.makeText(this, R.string.error_login, Toast.LENGTH_SHORT).show();
            }
            return;
        }

        if (requestCode == REQUEST_IMAGE && resultCode == RESULT_OK && data != null) {
            Fragment currentFragment = getCurrentFragment();
            if (currentFragment instanceof BasePickImageFragment) {
                try {
                    Bitmap userImage = MediaStore.Images.Media.getBitmap(
                            this.getContentResolver(),
                            data.getData());
                    userImage = CompressBitmap.compress(userImage);
                    ((BasePickImageFragment) currentFragment).getUserImage(userImage);
                } catch (IOException e) {
                    Toast.makeText(this, R.string.error_image, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (mBinding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            mBinding.drawerLayout.closeDrawer(GravityCompat.START);
            return;
        }

        if (goToDefaultFragment()) {
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

        mMenuFragmentToSet = item;
        mBinding.drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onDrawerSlide(@NonNull View drawerView, float slideOffset) { }

    @Override
    public void onDrawerOpened(@NonNull View drawerView) { }

    @Override
    public void onDrawerStateChanged(int newState) { }

    @Override
    public void onDrawerClosed(@NonNull View drawerView) {
        if (mMenuFragmentToSet != null) {
            BaseFragment fragment = getFragmentById(mMenuFragmentToSet.getItemId());
            String tag = generateFragmentTag(mMenuFragmentToSet.getItemId());
            replaceFragment(fragment, tag, mMenuFragmentToSet.getTitle());
        }
        mMenuFragmentToSet = null;
    }

    @Override
    public void setPresenter(@NonNull MainPresenterContract presenter) {
        mPresenter = presenter;
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
        if (mBinding != null) {
            mBinding.mainBottomNavigation.setVisibility(View.VISIBLE);
            mBinding.mainBottomNavigation.setOnNavigationItemSelectedListener(listener);
        }
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

    @Override
    public void refreshUser() {
        mPresenter.refresh();
    }

    @Override
    public void onRemoveCurrentTrip() {
        goToDefaultFragment();
    }

    @Override
    public void getUserImage() {
        Intent libraryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        libraryIntent.setType(AVATAR_FILE_TYPE);

        Intent cameraIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        cameraIntent.setType(AVATAR_FILE_TYPE);

        Intent chooserIntent = Intent.createChooser(libraryIntent,
                getString(R.string.profile_avatar_chooser_title));
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {cameraIntent});

        startActivityForResult(chooserIntent, REQUEST_IMAGE);
    }

    @Override
    public void updateUser(User user) {
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
            mBinding.navView.getMenu().findItem(R.id.navigation_trip_current).setVisible(true);
            mBinding.navView.getMenu().findItem(R.id.navigation_profile).setVisible(true);
        } else {
            ((TextView) mBinding.navView.getHeaderView(0)
                    .findViewById(R.id.navigation_profile_name)).setText("");
            ((TextView) mBinding.navView.getHeaderView(0)
                    .findViewById(R.id.navigation_profile_email)).setText("");
            ((ImageView) mBinding.navView.getHeaderView(0)
                    .findViewById(R.id.navigation_profile_avatar))
                    .setImageResource(R.mipmap.ic_launcher_foreground);
            mBinding.navView.getMenu().findItem(R.id.navigation_login).setVisible(true);
            mBinding.navView.getMenu().findItem(R.id.navigation_logout).setVisible(false);
            mBinding.navView.getMenu().findItem(R.id.navigation_trip_current).setVisible(false);
            mBinding.navView.getMenu().findItem(R.id.navigation_profile).setVisible(false);
        }
    }

    @Override
    public void goToLogin(boolean isSmartLockEnabled) {
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build());
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .setIsSmartLockEnabled(isSmartLockEnabled)
                        .setLogo(R.mipmap.ic_launcher_foreground)
                        .setTheme(R.style.AppTheme)
                        .build(),
                REQUEST_LOGIN);
    }

    @Override
    public void goToLogout() {
        goToDefaultFragment();
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
                mPresenter.login();
                return null;
            case R.id.navigation_logout:
                mPresenter.logout();
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
    }

    private boolean goToDefaultFragment() {
        String initialFragmentTag = generateFragmentTag(R.id.navigation_trip_list);
        Fragment initialFragment = mFragmentManager.findFragmentByTag(initialFragmentTag);
        if (initialFragment == null) {
            replaceFragment(
                    TripPageFragment.newInstance(),
                    initialFragmentTag,
                    getString(R.string.navigation_trip_list));
            mBinding.navView.setCheckedItem(R.id.navigation_trip_list);
            return true;
        }
        return false;
    }

    private Fragment getCurrentFragment() {
        return mFragmentManager.findFragmentById(R.id.main_content);
    }
}
