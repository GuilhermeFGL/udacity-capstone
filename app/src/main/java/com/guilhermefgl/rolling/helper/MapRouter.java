package com.guilhermefgl.rolling.helper;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.maps.model.LatLng;
import com.guilhermefgl.rolling.model.Place;

import java.util.ArrayList;

public class MapRouter {

    @Nullable
    private Place mStartPlace;
    @Nullable
    private Place mEndPlace;
    @NonNull
    private ArrayList<Place> mBreakPlaces;

    public MapRouter() {
        mBreakPlaces = new ArrayList<>();
    }

    public Place getStartPoint() {
        return mStartPlace;
    }

    public void setStartPoint(Place startPlace) {
        mStartPlace = startPlace;
    }

    public Place getEndPlace() {
        return mEndPlace;
    }

    public void setEndPoint(Place endPlace) {
        mEndPlace = endPlace;
    }

    public void addBreakPlace(Place breakPlace) {
        mBreakPlaces.add(breakPlace);
    }

    public void removeBreakPlace(int position) {
        mBreakPlaces.remove(position);
    }

    public ArrayList<Place> getBreakPlaces() {
        return mBreakPlaces;
    }

    public ArrayList<Place> getPlaces() {
        ArrayList<Place> places = new ArrayList<>(mBreakPlaces);
        if (mStartPlace != null) {
            places.add(0, mStartPlace);
        }
        if (mEndPlace != null) {
            places.add(mEndPlace);
        }
        return places;
    }

    public ArrayList<LatLng> getPositions() {
        ArrayList<LatLng> positions = new ArrayList<>();
        ArrayList<Place> places = getPlaces();
        for(Place place : places) {
            positions.add(new LatLng(place.getPlaceLatitude(), place.getPlaceLongitude()));
        }
        return positions;
    }
}
