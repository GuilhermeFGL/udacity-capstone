package com.guilhermefgl.rolling.presenter.profile;

import android.graphics.Bitmap;

import com.guilhermefgl.rolling.presenter.BasePresenter;

public interface ProfilePresenterContract extends BasePresenter {

    void changeName(String userName);

    void changePassword(String userPassword);

    void changeAvatar(Bitmap userAvatar);
}
