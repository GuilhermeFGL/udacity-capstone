package com.guilhermefgl.rolling.view.profile;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
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
import com.guilhermefgl.rolling.helper.contracts.PickImageInteractionListener;
import com.guilhermefgl.rolling.model.User;
import com.guilhermefgl.rolling.presenter.profile.ProfilePresenter;
import com.guilhermefgl.rolling.presenter.profile.ProfilePresenterContract;
import com.guilhermefgl.rolling.view.BasePickImageFragment;

public class ProfileFragment extends BasePickImageFragment implements ProfileViewContract {

    private FragmentProfileBinding mBinding;
    private ProfilePresenterContract mPresenter;
    private PickImageInteractionListener mListener;

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

        mBinding.profileAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateAvatar();
            }
        });
        mBinding.profilePasswordAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePassword();
            }
        });

        new ProfilePresenter(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof PickImageInteractionListener) {
            mListener = (PickImageInteractionListener) context;
        } else {
            throw new UnsupportedOperationException();
        }
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

    @Override
    public void getUserImage(Bitmap image) {
        mPresenter.changeAvatar(image);
    }

    @Override
    public void setPresenter(@NonNull ProfilePresenterContract presenter) {
        mPresenter = presenter;
    }

    @Override
    public void setUser(User user) {
        PicassoHelper.loadImage(user.getUserAvatarUrl(), mBinding.profileAvatar);
        mBinding.profileEmail.setText(user.getUserEmail());
        mBinding.profileNameInput.setText(user.getUserName());
    }

    private void updateAvatar() {
        mListener.getUserImage();
    }

    private void updatePassword() {
        mPresenter.changePassword(mBinding.profilePasswordInput.getText().toString());
    }
}
