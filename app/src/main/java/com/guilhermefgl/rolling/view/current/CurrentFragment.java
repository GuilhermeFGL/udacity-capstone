package com.guilhermefgl.rolling.view.current;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.guilhermefgl.rolling.R;
import com.guilhermefgl.rolling.databinding.FragmentCurrentBinding;
import com.guilhermefgl.rolling.helper.PicassoHelper;
import com.guilhermefgl.rolling.mock.TripMock;
import com.guilhermefgl.rolling.model.Trip;
import com.guilhermefgl.rolling.view.BaseFragment;
import com.guilhermefgl.rolling.view.breakpoint.BreakPointAdapter;

public class CurrentFragment extends BaseFragment {

    // TODO remove mock data
    private Trip mockTrip = TripMock.getMyCurrentTrip();

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

    private void setupView() {
        mBinding.tripTitle.setText(mockTrip.getTripName());
        PicassoHelper.loadImage(mockTrip.getTripBannerUrl(), mBinding.tripImage);
        mBinding.includeNavigation.tripDistance.setText(mockTrip.getTripDistance());
        mBinding.includeNavigation.tripTime.setText(mockTrip.getTripDuration());
        mBinding.includeNavigation.tripStart.setText(mockTrip.getPlaceStart().getPlaceName());
        mBinding.includeNavigation.tripDestination.setText(mockTrip.getPlaceEnd().getPlaceName());

        mBinding.includeNavigation.tripListBreakPoints
                .setLayoutManager(new LinearLayoutManager(getActivity()));
        mBinding.includeNavigation.tripListBreakPoints
                .setAdapter(new BreakPointAdapter(mockTrip.getPlacesPoints()));
    }
}
