package com.guilhermefgl.rolling.view.details;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.guilhermefgl.rolling.R;
import com.guilhermefgl.rolling.databinding.FragmentDetailsTripBinding;
import com.guilhermefgl.rolling.model.Trip;
import com.guilhermefgl.rolling.view.BaseFragment;
import com.guilhermefgl.rolling.view.breakpoint.BreakPointAdapter;

public class TripDetailsFragment extends BaseFragment {

    private static final String BUNDLE_TRIP = "BUNDLE_FILTER";

    private FragmentDetailsTripBinding mBinding;
    private Trip mTrip;

    @NonNull
    public static TripDetailsFragment newInstance(@NonNull  Trip trip) {
        TripDetailsFragment fragment = new TripDetailsFragment();
        Bundle args = new Bundle();
        args.putParcelable(BUNDLE_TRIP, trip);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTrip = getArguments().getParcelable(BUNDLE_TRIP);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_details_trip, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupView();
    }

    private void setupView() {
        mBinding.includeTrip.tripDistance.setText(mTrip.getTripDistance());
        mBinding.includeTrip.tripTime.setText(mTrip.getTripDuration());
        mBinding.includeTrip.tripStart.setText(mTrip.getPlaceStart().getPlaceName());
        mBinding.includeTrip.tripDestination.setText(mTrip.getPlaceEnd().getPlaceName());
        mBinding.includeTrip.tripListBreakPoints
                .setAdapter(new BreakPointAdapter(mTrip.getPlacesPoints()));
    }

}
