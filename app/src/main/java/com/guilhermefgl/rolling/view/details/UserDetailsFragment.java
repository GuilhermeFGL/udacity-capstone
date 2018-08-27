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
import com.guilhermefgl.rolling.model.User;
import com.guilhermefgl.rolling.view.BaseFragment;

import java.util.ArrayList;

public class UserDetailsFragment extends BaseFragment {

    private static final String BUNDLE_USERS = "BUNDLE_USERS";

    private FragmentDetailsPersonsBinding mBinding;
    private ArrayList<User> mUsers;

    @NonNull
    public static UserDetailsFragment newInstance(@NonNull ArrayList<User> users) {
        UserDetailsFragment fragment = new UserDetailsFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(BUNDLE_USERS, users);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mUsers = getArguments().getParcelableArrayList(BUNDLE_USERS);
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

    public void updateUserList(ArrayList<User> users) {
        mUsers = users;
        setupView();
    }

    private void setupView() {
        if (mUsers != null && getContext() != null) {
            mBinding.tripPersonsResume.setText(
                    String.format(getContext().getResources().getQuantityString(
                            R.plurals.details_resume_persons, mUsers.size()),
                            mUsers.size()));
            mBinding.detailsPersonList.setAdapter(new UserAdapter(mUsers));
        }
    }

}
