package com.guilhermefgl.rolling.presenter.list;

import android.support.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.guilhermefgl.rolling.helper.FirebaseHelper;
import com.guilhermefgl.rolling.model.Trip;
import com.guilhermefgl.rolling.view.list.TripListViewContract;

import java.util.ArrayList;

public class TripListPresenter implements TripListPresenterContract, ValueEventListener {

    @NonNull
    private final DatabaseReference mDataBase;
    @NonNull
    private final FirebaseAuth mAuth;
    @NonNull
    private final TripListViewContract mView;

    private Filters mFilter;

    public TripListPresenter(@NonNull TripListViewContract view) {
        mDataBase = FirebaseHelper.getTripDatabaseInstance();
        mAuth = FirebaseHelper.getAuthInstance();
        mView = view;
        mView.setPresenter(this);
    }

    @Override
    public void setFilterAndGetTrips(Filters filter) {
        mFilter = filter;
        mDataBase.addValueEventListener(this);
    }

    @Override
    public void start() { }

    @Override
    public void stop() { }

    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        ArrayList<Trip> trips = new ArrayList<>();
        int orderIndex = 0;

        if (mFilter == Filters.ALL) {
            trips.add(null);
            orderIndex = 1;
        }

        for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
            if (mFilter == Filters.ALL) {
                trips.add(orderIndex, postSnapshot.getValue(Trip.class));
            }
        }

        mView.setList(trips);
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {
        mView.onDatabaseErrorListener();
    }
}
