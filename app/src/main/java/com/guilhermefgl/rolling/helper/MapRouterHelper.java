package com.guilhermefgl.rolling.helper;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.maps.model.LatLng;
import com.guilhermefgl.rolling.model.Place;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@SuppressWarnings({"unused", "WeakerAccess"})
public class MapRouterHelper {

    private static final String LOCATION_FORMATTER = "geo:0,0?q=%f,%f (%s)";

    @Nullable
    private Place mStartPlace;
    @Nullable
    private Place mEndPlace;
    @NonNull
    private final ArrayList<Place> mBreakPlaces;

    public MapRouterHelper() {
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
        mBreakPlaces.remove(position - 1);
    }

    public void addBreakPlace(List<Place> breakPlaces) {
        if (breakPlaces != null) {
            mBreakPlaces.addAll(breakPlaces);
        }
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

    public static Intent createNavigationIntent(Place place) {
        return new Intent(android.content.Intent.ACTION_VIEW,
                Uri.parse(String.format(Locale.getDefault(), LOCATION_FORMATTER,
                        place.getPlaceLatitude(), place.getPlaceLongitude(), place.getPlaceName())));
    }
}
