package com.guilhermefgl.rolling.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Trip implements Parcelable {

    private Long tripId;
    private String tripName;
    private String tripBannerUrl;
    private String tripDistance;
    private String tripDuration;
    private User userOwner;
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

    public String getTripDistance() {
        return tripDistance;
    }

    public void setTripDistance(String tripDistance) {
        this.tripDistance = tripDistance;
    }

    public String getTripDuration() {
        return tripDuration;
    }

    public void setTripDuration(String tripDuration) {
        this.tripDuration = tripDuration;
    }

    public User getUserOwner() {
        return userOwner;
    }

    public void setUserOwner(User userOwner) {
        this.userOwner = userOwner;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(tripId);
        dest.writeString(tripName);
        dest.writeString(tripBannerUrl);
        dest.writeString(tripDistance);
        dest.writeString(tripDuration);
        dest.writeParcelable(userOwner, flags);
        dest.writeParcelable(placeStart, flags);
        dest.writeParcelable(placeEnd, flags);
        dest.writeTypedList(placesPoints);
    }

    protected Trip(Parcel in) {
        tripId = in.readLong();
        tripName = in.readString();
        tripBannerUrl = in.readString();
        tripDistance = in.readString();
        tripDuration = in.readString();
        userOwner = in.readParcelable(User.class.getClassLoader());
        placeStart = in.readParcelable(Place.class.getClassLoader());
        placeEnd = in.readParcelable(Place.class.getClassLoader());
        placesPoints = in.createTypedArrayList(Place.CREATOR);
    }

    public static final Creator<Trip> CREATOR = new Creator<Trip>() {
        @Override
        public Trip createFromParcel(Parcel in) {
            return new Trip(in);
        }

        @Override
        public Trip[] newArray(int size) {
            return new Trip[size];
        }
    };
}
