package com.guilhermefgl.rolling.view.current;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.guilhermefgl.rolling.R;
import com.guilhermefgl.rolling.databinding.FragmentCurrentBinding;
import com.guilhermefgl.rolling.helper.DateFormatterHelper;
import com.guilhermefgl.rolling.helper.MapDrawerHelper;
import com.guilhermefgl.rolling.helper.MapRouterHelper;
import com.guilhermefgl.rolling.helper.PicassoHelper;
import com.guilhermefgl.rolling.helper.component.ScrollableMapView;
import com.guilhermefgl.rolling.model.Place;
import com.guilhermefgl.rolling.model.Trip;
import com.guilhermefgl.rolling.presenter.current.CurrentPresenter;
import com.guilhermefgl.rolling.presenter.current.CurrentPresenterContract;
import com.guilhermefgl.rolling.view.BaseFragment;
import com.guilhermefgl.rolling.view.breakpoint.BreakPointAdapter;
import com.guilhermefgl.rolling.view.widget.TripWidgetProvider;

public class CurrentFragment extends BaseFragment implements
        BreakPointAdapter.BreakPointAdapterItemClick, OnMapReadyCallback, CurrentViewContract,
        View.OnClickListener {

    private Trip mTrip;
    private FragmentCurrentBinding mBinding;
    private CurrentPresenterContract mPresenter;
    private GoogleMap mMap;
    private MenuItem mItemMenu;
    private CurrentFragmentInteractionListener mListener;

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

        mBinding.includeTrip.tripStart.setOnClickListener(this);
        mBinding.includeTrip.tripDestination.setOnClickListener(this);

        new CurrentPresenter(this);

        return mBinding.getRoot();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof CurrentFragmentInteractionListener) {
            mListener = (CurrentFragmentInteractionListener) context;
        } else {
            throw new UnsupportedOperationException();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_trip_current, menu);
        mItemMenu = menu.findItem(R.id.menu_current_remove);
        if (mTrip != null) {
            mItemMenu.setVisible(true);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_current_remove:
                removeAsCurrent();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        if (mTrip != null) {
            if (v.getId() == R.id.trip_start) {
                startActivity(MapRouterHelper.createNavigationIntent(mTrip.getPlaceStart()));
            } else if (v.getId() == R.id.trip_destination) {
                startActivity(MapRouterHelper.createNavigationIntent(mTrip.getPlaceEnd()));
            }
        }
    }

    @Override
    public void onBreakPointItemCLick(Place place) {
        startActivity(MapRouterHelper.createNavigationIntent(place));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (mTrip != null) {
            drawnMap();
        }
    }

    @Override
    public void setCurrentTrip(Trip trip) {
        if (isAdded()) {
            mTrip = trip;
            setupView();
            drawnMap();
            mBinding.tripCurrentProgress.setVisibility(View.GONE);
        }
    }

    @Override
    public void onGetCurrentTripFailure() {
        if (isAdded()) {
            Toast.makeText(getActivity(), R.string.error_load_trip, Toast.LENGTH_SHORT).show();
            mBinding.tripCurrentProgress.setVisibility(View.GONE);
        }
    }

    @Override
    public void setPresenter(@NonNull CurrentPresenterContract presenter) {
        mPresenter = presenter;
        mPresenter.getCurrentTrip();
        mBinding.tripCurrentProgress.setVisibility(View.VISIBLE);
    }

    private void setupView() {
        if (mTrip != null) {
            PicassoHelper.loadImage(mTrip.getTripBannerUrl(), mBinding.tripImage);
            mBinding.tripTitle.setText(mTrip.getTripName());
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

            mBinding.currentTripContent.setVisibility(View.VISIBLE);
            mBinding.currentTripEmpty.setVisibility(View.GONE);
            if (mItemMenu != null) {
                mItemMenu.setVisible(true);
            }
        } else {
            mBinding.currentTripContent.setVisibility(View.GONE);
            mBinding.currentTripEmpty.setVisibility(View.VISIBLE);
            if (mItemMenu != null) {
                mItemMenu.setVisible(false);
            }
        }
    }

    private void drawnMap() {
        if (mMap != null && mTrip != null) {
            new MapDrawerHelper(getActivity(), null).drawnMap(mMap, new MapRouterHelper() {{
                setStartPoint(mTrip.getPlaceStart());
                setEndPoint(mTrip.getPlaceEnd());
                for (Place breakPoint : mTrip.getPlacesPoints()) {
                    addBreakPlace(breakPoint);
                }
            }});
        }
    }

    private void removeAsCurrent() {
        mPresenter.removeCurrentTrip();
        mListener.onRemoveCurrentTrip();
        Toast.makeText(getActivity(), R.string.trip_current_removed, Toast.LENGTH_SHORT)
                .show();
        if (getActivity() != null) {
            TripWidgetProvider.update(getActivity().getApplicationContext());
        }
    }

    public interface CurrentFragmentInteractionListener {

        void onRemoveCurrentTrip();
    }
}
