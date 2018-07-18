package com.guilhermefgl.rolling.presenter.details;

import com.guilhermefgl.rolling.model.Trip;
import com.guilhermefgl.rolling.presenter.BasePresenter;

public interface DetailsPresenterContract extends BasePresenter {

    void setTrip(Trip trip);

    void addTripAsMarked();

    void removeTripAsMarked();
}
