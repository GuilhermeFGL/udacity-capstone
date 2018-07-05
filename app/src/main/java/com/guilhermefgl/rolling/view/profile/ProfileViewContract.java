package com.guilhermefgl.rolling.view.profile;

import com.guilhermefgl.rolling.model.User;
import com.guilhermefgl.rolling.presenter.profile.ProfilePresenterContract;
import com.guilhermefgl.rolling.view.BaseView;

public interface ProfileViewContract extends BaseView<ProfilePresenterContract>,
        ProfilePresenterContract.UpdateProfileCallBack {

    void setUser(User user, boolean isPasswordProvider);

}
