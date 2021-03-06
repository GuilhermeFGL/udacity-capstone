package com.guilhermefgl.rolling.view.list;

import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.guilhermefgl.rolling.R;
import com.guilhermefgl.rolling.databinding.FragmentTripListBinding;
import com.guilhermefgl.rolling.model.Trip;
import com.guilhermefgl.rolling.presenter.list.TripListPresenter;
import com.guilhermefgl.rolling.presenter.list.TripListPresenterContract;
import com.guilhermefgl.rolling.view.BaseActivity;
import com.guilhermefgl.rolling.view.BaseFragment;
import com.guilhermefgl.rolling.view.details.DetailsActivity;
import com.guilhermefgl.rolling.view.trip.TripActivity;

import java.util.ArrayList;
import java.util.List;

public class TripListFragment extends BaseFragment implements TripListViewContract,
        TripAdapter.TripAdapterItemClick {

    private static final String BUNDLE_FILTER = "BUNDLE_FILTER";
    private static final Integer GRID_COUNT_PORTRAIT = 1;
    private static final Integer GRID_COUNT_LANDSCAPE = 2;

    public static final String BUNDLE_FILTER_ALL = "BUNDLE_FILTER_ALL";
    public static final String BUNDLE_FILTER_USER = "BUNDLE_FILTER_USER";

    private FragmentTripListBinding mBinding;
    private TripAdapter mAdapter;
    private String filterParam;
    private TripListPresenterContract mPresenter;
    private String mQuery;

    @NonNull
    public static TripListFragment newInstance(String filter) {
        TripListFragment fragment = new TripListFragment();
        Bundle args = new Bundle();
        args.putString(BUNDLE_FILTER, filter);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            filterParam = getArguments().getString(BUNDLE_FILTER);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_trip_list, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mBinding.tripListRecycleView.setLayoutManager(
                new GridLayoutManager(
                        getContext(),
                        getResources().getConfiguration().orientation
                                == Configuration.ORIENTATION_PORTRAIT ?
                                GRID_COUNT_PORTRAIT: GRID_COUNT_LANDSCAPE));

        mAdapter = new TripAdapter(new ArrayList<Trip>(), this);
        mBinding.tripListRecycleView.setAdapter(mAdapter);
        new TripListPresenter(this);
    }

    @Override
    public void onDetach() {
        mPresenter.stop();
        super.onDetach();
    }

    @Override
    public void itemCLick(Trip trip) {
        if (getActivity() instanceof BaseActivity) {
            if (trip != null) {
                DetailsActivity.startActivity(((BaseActivity) getActivity()), trip);
            } else {
                TripActivity.startActivity((BaseActivity) getActivity());
            }
        }
    }

    @Override
    public void setPresenter(@NonNull TripListPresenterContract presenter) {
        mPresenter = presenter;
        mBinding.tripListProgress.setVisibility(View.VISIBLE);

        if (mQuery != null) {
            mPresenter.setQuery(mQuery);
        }

        if (filterParam != null) {
            switch (filterParam) {
                case BUNDLE_FILTER_ALL:
                    mPresenter.setFilter(TripListPresenterContract.Filters.ALL);
                    break;
                case BUNDLE_FILTER_USER:
                    mPresenter.setFilter(TripListPresenterContract.Filters.MARKED);
                    break;
            }
            mPresenter.start();
        }
    }

    @Override
    public void setList(@NonNull List<Trip> trips) {
        mBinding.tripListProgress.setVisibility(View.GONE);
        mAdapter.setTripList(trips);
        if (!trips.isEmpty()) {
            mBinding.tripListEmpty.setVisibility(View.GONE);
            mBinding.tripListMarkedEmpty.setVisibility(View.GONE);
            mBinding.tripListRecycleView.setVisibility(View.VISIBLE);
        } else {
            mBinding.tripListRecycleView.setVisibility(View.GONE);
            if (filterParam.equals(BUNDLE_FILTER_ALL)) {
                mBinding.tripListEmpty.setVisibility(View.VISIBLE);
            } else if (filterParam.equals(BUNDLE_FILTER_USER)) {
                mBinding.tripListMarkedEmpty.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onDatabaseErrorListener() {
        Toast.makeText(getActivity(), R.string.error_network, Toast.LENGTH_SHORT).show();
    }

    public void setQuery(String query) {
        mQuery = query;
        if (mPresenter != null) {
            mPresenter.setQuery(query);
        }
    }
}
