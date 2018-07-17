package com.guilhermefgl.rolling.view.list;

import android.support.annotation.NonNull;

import com.guilhermefgl.rolling.model.Trip;
import com.guilhermefgl.rolling.presenter.list.TripListPresenterContract;
import com.guilhermefgl.rolling.view.BaseView;

import java.util.List;

public interface TripListViewContract extends BaseView<TripListPresenterContract> {

    void setList(@NonNull List<Trip> trips);
}