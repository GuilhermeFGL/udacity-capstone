package com.guilhermefgl.rolling.view.profile;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
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

public class ProfileFragment extends BasePickImageFragment implements ProfileViewContract {

    private FragmentProfileBinding mBinding;
    private ProfilePresenterContract mPresenter;
    private PickImageInteractionListener mPickImageListener;
    private ProfileFragmentInteractionListener mListener;
    private Boolean mProfileNameChanged;

    public ProfileFragment() { }

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        mBinding.profileNameInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    changeName();
                }
            }
        });
        mBinding.profileNameInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mProfileNameChanged = true;
            }

            @Override
            public void afterTextChanged(Editable s) { }
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

        mProfileNameChanged = false;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (mProfileNameChanged) {
            mPresenter.changeName(mBinding.profileNameInput.getText().toString());
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mPresenter.start();
    }

    @Override
    public void onStop() {
        super.onStop();
        mPresenter.stop();
    }

    @Override
    public void getUserImage(Bitmap image) {
        mPresenter.changeAvatar(image);
        mBinding.profileProgress.setVisibility(View.VISIBLE);
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

    private void updateAvatar() {
        mPickImageListener.getUserImage();
    }

    private void changeName() {
        mPresenter.changeName(mBinding.profileNameInput.getText().toString());
        mBinding.profileProgress.setVisibility(View.VISIBLE);
        mProfileNameChanged = false;
    }

    private void updatePassword() {
        mPresenter.changePassword(mBinding.profilePasswordInput.getText().toString());
        mBinding.profileProgress.setVisibility(View.VISIBLE);
    }

    public interface ProfileFragmentInteractionListener {

        void refreshUser();
    }
}
