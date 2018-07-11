package com.guilhermefgl.rolling.view.current;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.guilhermefgl.rolling.R;
import com.guilhermefgl.rolling.helper.component.ScrollableMapView;
import com.guilhermefgl.rolling.databinding.FragmentCurrentBinding;
import com.guilhermefgl.rolling.helper.DateFormatterHelper;
import com.guilhermefgl.rolling.helper.MapDrawerHelper;
import com.guilhermefgl.rolling.helper.MapRouter;
import com.guilhermefgl.rolling.helper.PicassoHelper;
import com.guilhermefgl.rolling.model.Place;
import com.guilhermefgl.rolling.model.Trip;
import com.guilhermefgl.rolling.view.BaseFragment;
import com.guilhermefgl.rolling.view.breakpoint.BreakPointAdapter;

public class CurrentFragment extends BaseFragment implements
        BreakPointAdapter.BreakPointAdapterItemClick, OnMapReadyCallback {

    private Trip mockTrip = new Trip();

    private FragmentCurrentBinding mBinding;

    public static CurrentFragment newInstance() {
        return new CurrentFragment();
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
                inflater, R.layout.fragment_current, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupView();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_trip_current, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_current_remove:
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    public void onBreakPointItemCLick(Place place) {

    }

    private void setupView() {
        PicassoHelper.loadImage(mockTrip.getTripBannerUrl(), mBinding.tripImage);
        mBinding.tripTitle.setText(mockTrip.getTripName());
        mBinding.includeTrip.tripDistance.setText(mockTrip.getTripDistance());
        mBinding.includeTrip.tripTime.setText(mockTrip.getTripDuration());
        mBinding.includeTrip.tripStart.setText(mockTrip.getPlaceStart().getPlaceName());
        mBinding.includeTrip.tripDestination.setText(mockTrip.getPlaceEnd().getPlaceName());

        if (getContext() != null) {
            mBinding.includeTrip.tripDate.setText(
                    DateFormatterHelper.dateToString(mockTrip.getTripDate(), getContext()));
        }

        if (!mockTrip.getPlacesPoints().isEmpty()) {
            mBinding.includeTrip.tripListBreakPoints
                    .setAdapter(new BreakPointAdapter(mockTrip.getPlacesPoints(), this));
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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        new MapDrawerHelper(getActivity(), null).drawnMap(googleMap, new MapRouter(){{
            setStartPoint(mockTrip.getPlaceStart());
            setEndPoint(mockTrip.getPlaceEnd());
            for (Place breakPoint : mockTrip.getPlacesPoints()) {
                addBreakPlace(breakPoint);
            }
        }});
    }
}
