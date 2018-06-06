package com.guilhermefgl.rolling.model;

public class Place {

    private Long placeId;
    private String placeName;
    private Long placeLatitude;
    private Long placeLongitude;

    public Long getPlaceId() {
        return placeId;
    }

    public void setPlaceId(Long placeId) {
        this.placeId = placeId;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public Long getPlaceLatitude() {
        return placeLatitude;
    }

    public void setPlaceLatitude(Long placeLatitude) {
        this.placeLatitude = placeLatitude;
    }

    public Long getPlaceLongitude() {
        return placeLongitude;
    }

    public void setPlaceLongitude(Long placeLongitude) {
        this.placeLongitude = placeLongitude;
    }
}
