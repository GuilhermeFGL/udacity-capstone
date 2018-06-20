package com.guilhermefgl.rolling.view.profile;

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

import com.guilhermefgl.rolling.R;
import com.guilhermefgl.rolling.databinding.FragmentProfileBinding;
import com.guilhermefgl.rolling.helper.PicassoHelper;
import com.guilhermefgl.rolling.mock.UserMock;
import com.guilhermefgl.rolling.model.User;
import com.guilhermefgl.rolling.view.BaseFragment;

public class ProfileFragment extends BaseFragment {

    // TODO remove mock data
    private User mockProfile = UserMock.getLogedUser();

    private FragmentProfileBinding mBinding;

    public ProfileFragment() { }

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
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
                inflater, R.layout.fragment_profile, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        PicassoHelper.loadImage(mockProfile.getUserAvatarUrl(), mBinding.profileAvatar);
        mBinding.profileEmail.setText(mockProfile.getUserEmail());
        mBinding.profileNameInput.setText(mockProfile.getUserName());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_profile, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_profile_save:
                // save
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }
}
