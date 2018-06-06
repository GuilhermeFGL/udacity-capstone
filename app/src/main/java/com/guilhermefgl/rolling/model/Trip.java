package com.guilhermefgl.rolling.model;

import java.util.List;

public class Trip {

    private Long tripId;
    private String tripName;
    private String tripBannerUrl;
    private Place placeStart;
    private Place placeEnd;
    private List<Place> placesPoints;

    public Long getTripId() {
        return tripId;
    }

    public void setTripId(Long tripId) {
        this.tripId = tripId;
    }

    public String getTripName() {
        return tripName;
    }

    public void setTripName(String tripName) {
        this.tripName = tripName;
    }

    public String getTripBannerUrl() {
        return tripBannerUrl;
    }

    public void setTripBannerUrl(String tripBannerUrl) {
        this.tripBannerUrl = tripBannerUrl;
    }

    public Place getPlaceStart() {
        return placeStart;
    }

    public void setPlaceStart(Place placeStart) {
        this.placeStart = placeStart;
    }

    public Place getPlaceEnd() {
        return placeEnd;
    }

    public void setPlaceEnd(Place placeEnd) {
        this.placeEnd = placeEnd;
    }

    public List<Place> getPlacesPoints() {
        return placesPoints;
    }

    public void setPlacesPoints(List<Place> placesPoints) {
        this.placesPoints = placesPoints;
    }
}
