package com.guilhermefgl.rolling.presenter.details;

import android.support.annotation.NonNull;

import com.guilhermefgl.rolling.model.Trip;
import com.guilhermefgl.rolling.presenter.BasePresenter;

public interface DetailsPresenterContract extends BasePresenter {

    void setTrip(@NonNull Trip trip);

    void addTripAsMarked();

    void removeTripAsMarked();

    void addTripAsCurrent();
}
