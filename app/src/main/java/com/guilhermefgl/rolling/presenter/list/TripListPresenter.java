package com.guilhermefgl.rolling.presenter.list;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.guilhermefgl.rolling.helper.FirebaseHelper;
import com.guilhermefgl.rolling.model.Trip;
import com.guilhermefgl.rolling.view.list.TripListViewContract;

import java.util.ArrayList;
import java.util.List;

public class TripListPresenter implements TripListPresenterContract, ValueEventListener {

    @NonNull
    private final DatabaseReference mTripDataBase;
    @NonNull
    private final FirebaseAuth mAuth;
    @NonNull
    private final TripListViewContract mView;
    @Nullable
    private DatabaseReference mUserHasTripDataBase;

    private List<Trip> mTrips;
    private List<String> mTripsIds;
    private Filters mFilter;
    private final ValueEventListener mUserHasTripEventListener;

    public TripListPresenter(@NonNull TripListViewContract view) {
        mTripDataBase = FirebaseHelper.getTripDatabaseInstance();
        mAuth = FirebaseHelper.getAuthInstance();
        mView = view;
        mUserHasTripEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String currentUser = mAuth.getUid();
                List<String> tripIds = new ArrayList<>();

                for (DataSnapshot tripSnapshot: dataSnapshot.getChildren()) {
                    String tripKey = tripSnapshot.getKey();
                    for (DataSnapshot userSnapshot : tripSnapshot.getChildren()) {
                        String userKey = userSnapshot.getKey();
                        if (userKey != null && userKey.equals(currentUser)) {
                            tripIds.add(tripKey);
                            break;
                        }
                    }
                }
                mTripsIds = tripIds;
                setTrip();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                mView.onDatabaseErrorListener();
            }
        };

        mView.setPresenter(this);
    }

    @Override
    public void setFilter(Filters filter) {
        mFilter = filter;
        if (mFilter == Filters.MARKED) {
            mUserHasTripDataBase = FirebaseHelper.getUserDatabaseInstance();
            mUserHasTripDataBase.addValueEventListener(mUserHasTripEventListener);
        }
    }

    @Override
    public void start() {
        mTripDataBase.addValueEventListener(this);
        if (mUserHasTripDataBase != null)  {
            mUserHasTripDataBase.addValueEventListener(mUserHasTripEventListener);
        }
    }

    @Override
    public void stop() {
        mTripDataBase.removeEventListener(this);
        if (mUserHasTripDataBase != null)  {
            mUserHasTripDataBase.removeEventListener(mUserHasTripEventListener);
        }
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        ArrayList<Trip> trips = new ArrayList<>();
        for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
            trips.add(postSnapshot.getValue(Trip.class));
        }
        mTrips = trips;
        setTrip();
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {
        mView.onDatabaseErrorListener();
    }

    private void setTrip() {
        if (mTrips != null) {
            List<Trip> trips;
            if (mFilter == Filters.ALL) {
                trips = new ArrayList<>(mTrips);
                trips.add(0, null);
                mView.setList(trips);
            } else if (mFilter == Filters.MARKED && mTripsIds != null && !mTripsIds.isEmpty()) {
                trips = new ArrayList<>();
                for (Trip trip : mTrips) {
                    if (mTripsIds.contains(trip.getTripId())) {
                        trips.add(trip);
                    }
                }
                mView.setList(trips);
            }
        }
    }
}
