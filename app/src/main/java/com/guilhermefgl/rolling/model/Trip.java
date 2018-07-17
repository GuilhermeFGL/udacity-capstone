package com.guilhermefgl.rolling.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Date;
import java.util.List;

@IgnoreExtraProperties
@SuppressWarnings({"WeakerAccess", "unused"})
public class Trip implements Parcelable {

    private String tripId;
    private String tripName;
    private String tripBannerUrl;
    private String tripDistance;
    private String tripDuration;
    private Date tripDate;
    private Place placeStart;
    private Place placeEnd;
    private List<Place> placesPoints;
    private String userOwner;

    public Trip() { }

    public String getTripId() {
        return tripId;
    }

    public void setTripId(String tripId) {
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

    public String getUserOwner() {
        return userOwner;
    }

    public void setUserOwner(String userOwner) {
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

    public Date getTripDate() {
        return tripDate;
    }

    public void setTripDate(Date tripDate) {
        this.tripDate = tripDate;
    }

    protected Trip(Parcel in) {
        tripId = in.readString();
        tripName = in.readString();
        tripBannerUrl = in.readString();
        tripDistance = in.readString();
        tripDuration = in.readString();
        tripDate = (Date) in.readSerializable();
        placeStart = in.readParcelable(Place.class.getClassLoader());
        placeEnd = in.readParcelable(Place.class.getClassLoader());
        placesPoints = in.createTypedArrayList(Place.CREATOR);
        userOwner = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(tripId);
        dest.writeString(tripName);
        dest.writeString(tripBannerUrl);
        dest.writeString(tripDistance);
        dest.writeString(tripDuration);
        dest.writeSerializable(tripDate);
        dest.writeParcelable(placeStart, flags);
        dest.writeParcelable(placeEnd, flags);
        dest.writeTypedList(placesPoints);
        dest.writeString(userOwner);
    }

    @Override
    public int describeContents() {
        return 0;
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
