package com.guilhermefgl.rolling.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Place implements Parcelable {

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(placeId);
        dest.writeString(placeName);
        dest.writeLong(placeLatitude);
        dest.writeLong(placeLongitude);
    }

    protected Place(Parcel in) {
        placeId = in.readLong();
        placeName = in.readString();
        placeLatitude = in.readLong();
        placeLongitude = in.readLong();
    }

    public static final Creator<Place> CREATOR = new Creator<Place>() {
        @Override
        public Place createFromParcel(Parcel in) {
            return new Place(in);
        }

        @Override
        public Place[] newArray(int size) {
            return new Place[size];
        }
    };
}
