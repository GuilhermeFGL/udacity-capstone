package com.guilhermefgl.rolling.view.details;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.guilhermefgl.rolling.R;
import com.guilhermefgl.rolling.databinding.FragmentDetailsTripBinding;
import com.guilhermefgl.rolling.helper.DateFormatterHelper;
import com.guilhermefgl.rolling.helper.MapDrawerHelper;
import com.guilhermefgl.rolling.helper.MapRouter;
import com.guilhermefgl.rolling.helper.component.ScrollableMapView;
import com.guilhermefgl.rolling.model.Place;
import com.guilhermefgl.rolling.model.Trip;
import com.guilhermefgl.rolling.view.BaseFragment;
import com.guilhermefgl.rolling.view.breakpoint.BreakPointAdapter;

public class TripDetailsFragment extends BaseFragment implements
        BreakPointAdapter.BreakPointAdapterItemClick, OnMapReadyCallback {

    private static final String BUNDLE_TRIP = "BUNDLE_TRIP";

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

        ((SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.include_trip_map_fragment))
                .getMapAsync(this);

        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupView();
    }

    @Override
    public void onBreakPointItemCLick(Place place) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        new MapDrawerHelper(getActivity(), null).drawnMap(googleMap, new MapRouter(){{
            setStartPoint(mTrip.getPlaceStart());
            setEndPoint(mTrip.getPlaceEnd());
            for (Place breakPoint : mTrip.getPlacesPoints()) {
                addBreakPlace(breakPoint);
            }
        }});
    }

    public void updateTrip(Trip trip) {
        mTrip = trip;
        setupView();
    }

    private void setupView() {
        if (mTrip != null) {
            mBinding.includeTrip.tripDistance.setText(mTrip.getTripDistance());
            mBinding.includeTrip.tripTime.setText(mTrip.getTripDuration());
            mBinding.includeTrip.tripStart.setText(mTrip.getPlaceStart().getPlaceName());
            mBinding.includeTrip.tripDestination.setText(mTrip.getPlaceEnd().getPlaceName());

            if (getContext() != null) {
                mBinding.includeTrip.tripDate.setText(
                        DateFormatterHelper.dateToString(mTrip.getTripDate(), getContext()));
            }

            if (!mTrip.getPlacesPoints().isEmpty()) {
                mBinding.includeTrip.tripListBreakPoints
                        .setAdapter(new BreakPointAdapter(mTrip.getPlacesPoints(), this));
            } else {
                mBinding.includeTrip.tripDivider1.setVisibility(View.GONE);
                mBinding.includeTrip.tripBreakPointsLabel.setVisibility(View.GONE);
                mBinding.includeTrip.tripListBreakPoints.setVisibility(View.GONE);
            }

            ScrollableMapView supportMapFragment = ((ScrollableMapView)
                    getChildFragmentManager().findFragmentById(R.id.include_trip_map_fragment));
            supportMapFragment.getMapAsync(this);
            supportMapFragment.setListener(new ScrollableMapView.OnTouchListener() {
                @Override
                public void onTouch() {
                    mBinding.tripScroll.requestDisallowInterceptTouchEvent(true);
                }
            });
        }
    }
}
