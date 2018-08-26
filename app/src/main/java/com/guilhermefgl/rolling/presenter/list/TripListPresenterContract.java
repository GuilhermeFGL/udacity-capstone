package com.guilhermefgl.rolling.presenter.list;

import com.guilhermefgl.rolling.presenter.BasePresenter;

public interface TripListPresenterContract extends BasePresenter {

    void setFilter(Filters filter);

    void setQuery(String query);

    enum Filters {
        ALL,
        MARKED
    }
}
