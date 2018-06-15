package com.guilhermefgl.rolling.view.details;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.guilhermefgl.rolling.R;
import com.guilhermefgl.rolling.databinding.FragmentDetailsPersonsBinding;
import com.guilhermefgl.rolling.model.Trip;
import com.guilhermefgl.rolling.view.BaseFragment;

public class UserDetailsFragment extends BaseFragment {

    private static final String BUNDLE_TRIP = "BUNDLE_FILTER";

    private FragmentDetailsPersonsBinding mBinding;
    private Trip mTrip;

    @NonNull
    public static UserDetailsFragment newInstance(@NonNull Trip trip) {
        UserDetailsFragment fragment = new UserDetailsFragment();
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
                inflater, R.layout.fragment_details_persons, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupView();
    }

    private void setupView() {
        mBinding.tripPersonsResume.setText(
                getString(R.string.details_person_resume, mTrip.getPersons().size()));
        mBinding.detailsPersonList.setAdapter(new UserAdapter(mTrip.getPersons()));
    }

}
