package com.guilhermefgl.rolling.presenter.current;

import android.support.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.guilhermefgl.rolling.helper.FirebaseHelper;
import com.guilhermefgl.rolling.model.Trip;
import com.guilhermefgl.rolling.view.current.CurrentViewContract;

public class CurrentPresenter implements CurrentPresenterContract {

    @NonNull
    private final DatabaseReference mCurrentDataBase;
    @NonNull
    private final DatabaseReference mTripDataBase;
    @NonNull
    private final FirebaseAuth mAuth;
    @NonNull
    private final CurrentViewContract mView;

    public CurrentPresenter(@NonNull CurrentViewContract view) {
        mCurrentDataBase = FirebaseHelper.getCurrentDatabaseInstance();
        mTripDataBase = FirebaseHelper.getTripDatabaseInstance();
        mAuth = FirebaseHelper.getAuthInstance();
        mView = view;
        mView.setPresenter(this);
    }

    @Override
    public void start() { }

    @Override
    public void stop() { }

    @Override
    public void getCurrentTrip() {
        if (mAuth.getCurrentUser() != null) {
            mCurrentDataBase.child(mAuth.getCurrentUser().getUid())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            getTrip((String) dataSnapshot.getValue());
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            mView.onGetCurrentTripFailure();
                        }
                    });
        } else {
            mView.onGetCurrentTripFailure();
        }
    }

    @Override
    public void removeCurrentTrip() {
        if (mAuth.getCurrentUser() != null) {
            mCurrentDataBase.child(mAuth.getCurrentUser().getUid()).removeValue();
        }
    }

    private void getTrip(String tripId) {
        mTripDataBase.child(tripId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mView.setCurrentTrip((Trip) dataSnapshot.getValue());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                mView.onGetCurrentTripFailure();
            }
        });
    }
}
