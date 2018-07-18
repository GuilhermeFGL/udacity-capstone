package com.guilhermefgl.rolling.view.details;

import com.guilhermefgl.rolling.model.Trip;
import com.guilhermefgl.rolling.model.User;
import com.guilhermefgl.rolling.presenter.details.DetailsPresenterContract;
import com.guilhermefgl.rolling.view.BaseView;

import java.util.List;

public interface DetailsViewContract extends BaseView<DetailsPresenterContract> {

    void onLoadTripSucess(Trip trip);

    void onLoadTripFailure();

    void onLoadUsersSucess(List<User> users);

    void onLoadUserFailure();

    void onLoadMarkedTripSucess(Boolean isMarked);

    void onLoadMarkedTripFailure();

    void onUpdateMarkedTripSucess(Boolean isMarked);

    void onUpdateMarkedTripFailure();
}
