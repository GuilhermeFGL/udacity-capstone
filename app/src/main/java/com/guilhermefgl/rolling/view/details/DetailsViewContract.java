package com.guilhermefgl.rolling.view.details;

import android.support.annotation.NonNull;

import com.guilhermefgl.rolling.model.Trip;
import com.guilhermefgl.rolling.model.User;
import com.guilhermefgl.rolling.presenter.details.DetailsPresenterContract;
import com.guilhermefgl.rolling.view.BaseView;

import java.util.List;

public interface DetailsViewContract extends BaseView<DetailsPresenterContract> {

    void onLoadTripSuccess(@NonNull Trip trip);

    void onLoadTripFailure();

    void onLoadUsersSuccess(@NonNull List<User> users);

    void onLoadUserFailure();

    void onLoadMarkedTripSuccess(@NonNull Boolean isMarked);

    void onLoadMarkedTripFailure();

    void onUpdateMarkedTripSuccess(@NonNull Boolean isMarked);

    void onUpdateMarkedTripFailure();

    void onUpdateCurrentSuccess();

    void onUpdateCurrentFailure();
}
