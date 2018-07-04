package com.guilhermefgl.rolling.view.main;

import com.guilhermefgl.rolling.model.User;
import com.guilhermefgl.rolling.presenter.main.MainPresenterContract;
import com.guilhermefgl.rolling.view.BaseView;

public interface MainViewContract extends BaseView<MainPresenterContract> {

    void updateUser(User user);

    void goToLogin(boolean isSmartLockEnabled);

    void goToLogout();

}
