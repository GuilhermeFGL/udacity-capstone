package com.guilhermefgl.rolling.presenter.current;

import com.guilhermefgl.rolling.presenter.BasePresenter;

public interface CurrentPresenterContract extends BasePresenter {

    void getCurrentTrip();

    void removeCurrentTrip();
}
