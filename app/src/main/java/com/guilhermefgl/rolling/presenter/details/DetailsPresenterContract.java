package com.guilhermefgl.rolling.presenter.details;

import com.guilhermefgl.rolling.presenter.BasePresenter;

public interface DetailsPresenterContract extends BasePresenter {

    void addTripAsMarked();

    void removeTripAsMarked();
}
