package com.guilhermefgl.rolling.presenter.list;

import com.guilhermefgl.rolling.presenter.BasePresenter;

public interface TripListPresenterContract extends BasePresenter {

    void setFilterAndGetTrips(Filters filter);

    enum Filters {
        ALL,
        MARKED
    }
}
