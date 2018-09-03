package com.guilhermefgl.rolling.presenter.trip;

import android.graphics.Bitmap;

import com.guilhermefgl.rolling.model.Place;
import com.guilhermefgl.rolling.model.Trip;
import com.guilhermefgl.rolling.presenter.BasePresenter;

import java.util.Date;

public interface TripPresenterContract extends BasePresenter {

    void setBanner(Bitmap banner);

    void setTitle(String title);

    void setDate(Date date);

    void setDuration(String duration);

    void setDistance(String distance);

    void setStartPlace(Place startPlace);

    void setEndPlace(Place endPlace);

    void addBreakPlace(Place breakPlace);

    void removeBreakPlace(int position);

    boolean isValid() throws UnsupportedOperationException;

    void save() throws UnsupportedOperationException;

    void saveTrip() throws UnsupportedOperationException;

    Trip getTripState();

    void setTripState(Trip tripState);

    Bitmap getBannerState();

    void drawnMap();

    interface SaveTripCallBack {

        void onSaveBannerSuccess();

        void onSaveTripSuccess();

        void onSaveTripFailure();
    }
}
