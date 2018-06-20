package com.guilhermefgl.rolling.model;

import android.os.Parcel;
import android.os.Parcelable;

@SuppressWarnings({"WeakerAccess", "unused"})
public class Place implements Parcelable {

    private Long placeId;
    private String placeName;
    private Double placeLatitude;
    private Double placeLongitude;

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

    public Double getPlaceLatitude() {
        return placeLatitude;
    }

    public void setPlaceLatitude(Double placeLatitude) {
        this.placeLatitude = placeLatitude;
    }

    public Double getPlaceLongitude() {
        return placeLongitude;
    }

    public void setPlaceLongitude(Double placeLongitude) {
        this.placeLongitude = placeLongitude;
    }

    public Place() { }

    protected Place(Parcel in) {
        if (in.readByte() == 0) {
            placeId = null;
        } else {
            placeId = in.readLong();
        }
        placeName = in.readString();
        if (in.readByte() == 0) {
            placeLatitude = null;
        } else {
            placeLatitude = in.readDouble();
        }
        if (in.readByte() == 0) {
            placeLongitude = null;
        } else {
            placeLongitude = in.readDouble();
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (placeId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(placeId);
        }
        dest.writeString(placeName);
        if (placeLatitude == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(placeLatitude);
        }
        if (placeLongitude == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(placeLongitude);
        }
    }

    @Override
    public int describeContents() {
        return 0;
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
