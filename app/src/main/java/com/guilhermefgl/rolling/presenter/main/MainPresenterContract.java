package com.guilhermefgl.rolling.presenter.main;

import com.guilhermefgl.rolling.presenter.BasePresenter;
import com.guilhermefgl.rolling.view.main.MainViewContract;

public interface MainPresenterContract extends BasePresenter<MainViewContract> {

    void login();

    void logout();
}
