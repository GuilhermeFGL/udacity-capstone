package com.guilhermefgl.rolling.view.profile;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.guilhermefgl.rolling.R;
import com.guilhermefgl.rolling.databinding.FragmentProfileBinding;
import com.guilhermefgl.rolling.helper.PicassoHelper;
import com.guilhermefgl.rolling.helper.contracts.PickImageInteractionListener;
import com.guilhermefgl.rolling.model.User;
import com.guilhermefgl.rolling.presenter.profile.ProfilePresenter;
import com.guilhermefgl.rolling.presenter.profile.ProfilePresenterContract;
import com.guilhermefgl.rolling.view.BasePickImageFragment;

public class ProfileFragment extends BasePickImageFragment
        implements ProfileViewContract, PasswordInputDialog.PasswordDialogCallback {

    private static final Integer FRAGMENT_PASSWORD_CODE = 1001;
    private static final String FRAGMENT_PASSWORD_TAG = "FRAGMENT_PASSWORD_TAG";

    private FragmentProfileBinding mBinding;
    private ProfilePresenterContract mPresenter;
    private PickImageInteractionListener mPickImageListener;
    private ProfileFragmentInteractionListener mListener;
    private User mUser;

    public ProfileFragment() { }

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
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

        mBinding.includeProfile.profileAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateAvatar();
            }
        });
        mBinding.includeProfile.includeProfileInput.profilePasswordAction.setOnClickListener(
                new View.OnClickListener() {
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
        if (context instanceof ProfileFragmentInteractionListener) {
            mListener = (ProfileFragmentInteractionListener) context;
        } else {
            throw new UnsupportedOperationException();
        }

        if (context instanceof PickImageInteractionListener) {
            mPickImageListener = (PickImageInteractionListener) context;
        } else {
            throw new UnsupportedOperationException();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (mUser != null
                && !mUser.getUserName().equals(
                        mBinding.includeProfile.includeProfileInput.profileNameInput.getText().toString())) {
            mPresenter.changeName(
                    mBinding.includeProfile.includeProfileInput.profileNameInput.getText().toString());
        }
    }

    @Override
    public void getUserImage(Bitmap image) {
        mPresenter.changeAvatar(image);
        mBinding.includeProfile.profileAvatar.setImageBitmap(image);
        mBinding.profileProgress.setVisibility(View.VISIBLE);
    }

    @Override
    public void setPresenter(@NonNull ProfilePresenterContract presenter) {
        mPresenter = presenter;
    }

    @Override
    public void setUser(User user, boolean isPasswordProvider) {
        mUser = user;
        if (user != null) {
            PicassoHelper.loadImage(user.getUserAvatarUrl(), mBinding.includeProfile.profileAvatar);
            mBinding.includeProfile.includeProfileInput.profileEmail.setText(user.getUserEmail());
            mBinding.includeProfile.includeProfileInput.profileNameInput.setText(user.getUserName());

            if (!isPasswordProvider) {
                mBinding.includeProfile.includeProfileInput.profilePasswordAction
                        .setVisibility(View.GONE);
                mBinding.includeProfile.includeProfileInput.profilePasswordInput
                        .setVisibility(View.GONE);
                mBinding.includeProfile.includeProfileInput.profilePasswordPlaceholder
                        .setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onUpdateUserSuccess() {
        mListener.refreshUser();
        mBinding.profileProgress.setVisibility(View.GONE);
    }

    @Override
    public void onUpdateUserFailure() {
        if (isAdded()) {
            Toast.makeText(getContext(), R.string.error_update_profile, Toast.LENGTH_LONG).show();
            mBinding.profileProgress.setVisibility(View.GONE);
        }
    }

    @Override
    public void onSubmitPassword(String oldPassword) {
        mBinding.profileProgress.setVisibility(View.VISIBLE);
        mPresenter.changePassword(
                mBinding.includeProfile.includeProfileInput.profilePasswordInput.getText().toString(),
                oldPassword);
    }

    private void updateAvatar() {
        mPickImageListener.getUserImage();
    }

    private void updatePassword() {
        if (getActivity() != null) {
            PasswordInputDialog dialog = PasswordInputDialog.newInstance();
            dialog.setTargetFragment(ProfileFragment.this, FRAGMENT_PASSWORD_CODE);
            dialog.show(getActivity().getSupportFragmentManager(), FRAGMENT_PASSWORD_TAG);
        }
    }

    public interface ProfileFragmentInteractionListener {

        void refreshUser();
    }
}
