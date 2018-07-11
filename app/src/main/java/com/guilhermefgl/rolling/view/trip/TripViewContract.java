package com.guilhermefgl.rolling.view.trip;

import com.guilhermefgl.rolling.helper.MapRouter;
import com.guilhermefgl.rolling.presenter.trip.TripPresenterContract;
import com.guilhermefgl.rolling.view.BaseView;

public interface TripViewContract extends BaseView<TripPresenterContract>,
        TripPresenterContract.SaveTripCallBack {

    void drawMap(MapRouter mapRouter);
}
