package com.guilhermefgl.rolling.view.list;

import android.app.SearchManager;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.guilhermefgl.rolling.R;
import com.guilhermefgl.rolling.databinding.FragmentTripPageBinding;
import com.guilhermefgl.rolling.view.BaseFragment;

public class TripPageFragment extends BaseFragment implements
        BottomNavigationView.OnNavigationItemSelectedListener, SearchView.OnQueryTextListener {

    private FragmentTripPageBinding mBinding;
    private TripListFragmentInteractionListener mListener;

    @NonNull
    public static TripPageFragment newInstance() {
        return new TripPageFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_trip_page, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mBinding.mainViewPager.setAdapter(new TripPageAdapter(getChildFragmentManager()));
        if (mListener != null) {
            mBinding.mainViewPager.addOnPageChangeListener(mListener.getOnPageChangeListener());
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof TripListFragmentInteractionListener) {
            mListener = (TripListFragmentInteractionListener) context;
            mListener.setupTripListFragment(this);
        } else {
            throw new UnsupportedOperationException();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener.unSetupTripListFragment();
        mBinding.mainViewPager.addOnPageChangeListener(mListener.getOnPageChangeListener());
        mListener = null;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_trip_list, menu);

        if (getActivity() != null) {
            SearchManager searchManager = (SearchManager)
                    getActivity().getSystemService(Context.SEARCH_SERVICE);
            if (searchManager != null) {
                SearchView mSearchView = (SearchView)
                        menu.findItem(R.id.menu_trip_list_search).getActionView();
                mSearchView.setSearchableInfo(
                        searchManager.getSearchableInfo(
                                getActivity().getComponentName()));
                mSearchView.setOnQueryTextListener(this);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return item.getItemId() == R.id.menu_trip_list_search || super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_recent:
                mBinding.mainViewPager.setCurrentItem(0);
                return true;
            case R.id.navigation_marked:
                mBinding.mainViewPager.setCurrentItem(1);
                return true;
            default:
                return false;
        }
    }

    public interface TripListFragmentInteractionListener {
        void setupTripListFragment(BottomNavigationView.OnNavigationItemSelectedListener listener);
        void unSetupTripListFragment();
        ViewPager.OnPageChangeListener getOnPageChangeListener();
    }
}