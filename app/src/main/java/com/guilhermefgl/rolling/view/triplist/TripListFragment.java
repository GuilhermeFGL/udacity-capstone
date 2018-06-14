package com.guilhermefgl.rolling.view.triplist;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.guilhermefgl.rolling.R;
import com.guilhermefgl.rolling.databinding.FragmentTripListBinding;
import com.guilhermefgl.rolling.view.BaseFragment;

public class TripListFragment extends BaseFragment {

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
    }

}
