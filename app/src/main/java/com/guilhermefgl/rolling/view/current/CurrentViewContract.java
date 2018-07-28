package com.guilhermefgl.rolling.view.current;

import com.guilhermefgl.rolling.model.Trip;
import com.guilhermefgl.rolling.presenter.current.CurrentPresenterContract;
import com.guilhermefgl.rolling.view.BaseView;

public interface CurrentViewContract extends BaseView<CurrentPresenterContract> {

    void setCurrentTrip(Trip trip);

    void onGetCurrentTripFailure();
}
