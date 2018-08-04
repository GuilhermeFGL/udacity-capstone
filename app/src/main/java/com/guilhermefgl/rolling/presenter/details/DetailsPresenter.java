package com.guilhermefgl.rolling.presenter.details;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.guilhermefgl.rolling.helper.FirebaseHelper;
import com.guilhermefgl.rolling.model.Trip;
import com.guilhermefgl.rolling.model.User;
import com.guilhermefgl.rolling.view.details.DetailsViewContract;

import java.util.ArrayList;

public class DetailsPresenter implements DetailsPresenterContract {

    @NonNull
    private final DatabaseReference mTripDataBase;
    @NonNull
    private final DatabaseReference mUserDataBase;
    @NonNull
    private final DatabaseReference mCurrentDataBase;
    @NonNull
    private final FirebaseAuth mAuth;
    @NonNull
    private final DetailsViewContract mView;

    private final ValueEventListener mTripDatabaseListener;
    private final ValueEventListener mUserDatabaseListener;

    @Nullable
    private Trip mTrip;
    @Nullable
    private String mTripId;

    public DetailsPresenter(@NonNull DetailsViewContract view) {
        mTripDataBase = FirebaseHelper.getTripDatabaseInstance();
        mUserDataBase = FirebaseHelper.getUserDatabaseInstance();
        mCurrentDataBase = FirebaseHelper.getCurrentDatabaseInstance();
        mAuth = FirebaseHelper.getAuthInstance();
        mView = view;

        mTripDatabaseListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        mUserDatabaseListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean isTripMarked = false;
                ArrayList<User> users = new ArrayList<>();

                String currentUserID = mAuth.getCurrentUser() != null ?
                        mAuth.getCurrentUser().getUid() : "";

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    User user = postSnapshot.getValue(User.class);
                    users.add(user);
                    if (user != null && user.getUserId() != null
                            && user.getUserId().equals(currentUserID)) {
                        isTripMarked = true;
                    }
                }

                mView.onLoadUsersSuccess(users);
                mView.onLoadMarkedTripSuccess(isTripMarked);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                mView.onLoadUserFailure();
                mView.onLoadMarkedTripFailure();
            }
        };

        mView.setPresenter(this);
        mView.setUserIsLogged(mAuth.getCurrentUser() != null);
    }

    @Override
    public void start() {
        if (mTrip != null) {
            mUserDataBase.child(mTrip.getTripId()).addValueEventListener(mUserDatabaseListener);
        } else if (mTripId != null) {
            mTripDataBase.child(mTripId).addValueEventListener(mTripDatabaseListener);
        }
    }

    @Override
    public void stop() {
        if (mTrip != null) {
            mUserDataBase.child(mTrip.getTripId()).removeEventListener(mUserDatabaseListener);
        } else if (mTripId != null) {
            mTripDataBase.child(mTripId).removeEventListener(mTripDatabaseListener);
        }
    }

    @Override
    public void getTrip(@NonNull String tripId) {
        mTripId = tripId;
    }

    @Override
    public void setTrip(@NonNull Trip trip) {
        mTrip = trip;
    }

    @Override
    public void addTripAsMarked() {
        if (mTrip != null && mAuth.getCurrentUser() != null) {
            FirebaseUser firebaseUser = mAuth.getCurrentUser();
            User user = new User();
            user.setUserId(firebaseUser.getUid());
            user.setUserName(firebaseUser.getDisplayName());
            user.setUserEmail(firebaseUser.getEmail());
            if (firebaseUser.getPhotoUrl() != null) {
                user.setUserAvatarUrl(firebaseUser.getPhotoUrl().toString());
            }

            mUserDataBase
                    .child(mTrip.getTripId())
                    .child(user.getUserId()).setValue(user).addOnCompleteListener(
                    new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                mView.onUpdateMarkedTripSuccess(true);
                            } else {
                                mView.onUpdateMarkedTripFailure();
                            }
                        }
                    });
        } else {
            mView.onUpdateMarkedTripFailure();
        }

    }

    @Override
    public void removeTripAsMarked() {
        if (mTrip != null && mAuth.getCurrentUser() != null) {
            mUserDataBase
                    .child(mTrip.getTripId())
                    .child(mAuth.getCurrentUser().getUid()).removeValue().addOnCompleteListener(
                    new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                mView.onUpdateMarkedTripSuccess(false);
                            } else {
                                mView.onUpdateMarkedTripFailure();
                            }
                        }
                    });
        } else {
            mView.onUpdateMarkedTripFailure();
        }
    }

    @Override
    public void addTripAsCurrent() {
        if (mTrip != null && mAuth.getCurrentUser() != null) {
            mCurrentDataBase.child(mAuth.getCurrentUser().getUid()).setValue(mTrip.getTripId())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                mView.onUpdateCurrentSuccess();
                            } else {
                                mView.onUpdateCurrentFailure();
                            }
                        }
                    });
        } else {
            mView.onUpdateCurrentFailure();
        }
    }
}
