package com.guilhermefgl.rolling.view.list;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.guilhermefgl.rolling.R;
import com.guilhermefgl.rolling.databinding.FragmentTripListBinding;
import com.guilhermefgl.rolling.mock.TripMock;
import com.guilhermefgl.rolling.model.Trip;
import com.guilhermefgl.rolling.view.BaseActivity;
import com.guilhermefgl.rolling.view.BaseFragment;
import com.guilhermefgl.rolling.view.details.DetailsActivity;
import com.guilhermefgl.rolling.view.trip.TripActivity;

import java.util.ArrayList;
import java.util.List;

public class TripListFragment extends BaseFragment implements TripAdapter.TripAdapterItemClick {

    private static final String BUNDLE_FILTER = "BUNDLE_FILTER";

    public static final String BUNDLE_FILTER_ALL = "BUNDLE_FILTER_ALL";
    public static final String BUNDLE_FILTER_USER = "BUNDLE_FILTER_USER";

    private FragmentTripListBinding mBinding;
    private String filterParam;

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

        TripAdapter adapter = new TripAdapter(new ArrayList<Trip>(), this);
        mBinding.tripListRecycleView.setAdapter(adapter);
        if (filterParam != null) {
            switch (filterParam) {
                case BUNDLE_FILTER_ALL:
                    List<Trip> trips = TripMock.getTripList();
                    trips.add(0, null);
                    adapter.setTripList(trips);
                    break;
                case BUNDLE_FILTER_USER:
                    adapter.setTripList(TripMock.getMyTripList());
                    break;
            }
        }
    }

    @Override
    public void itemCLick(Trip trip, View transitionImageView) {
        if (getActivity() instanceof BaseActivity) {
            if (trip != null) {
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        getActivity(), transitionImageView, getString(R.string.transition_details));
                DetailsActivity.startActivity(((BaseActivity) getActivity()), trip, options);
            } else {
                TripActivity.startActivity((BaseActivity) getActivity());
            }
        }
    }
}
