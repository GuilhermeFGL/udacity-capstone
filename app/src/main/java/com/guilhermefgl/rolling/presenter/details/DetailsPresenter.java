package com.guilhermefgl.rolling.presenter.details;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.guilhermefgl.rolling.helper.FirebaseHelper;
import com.guilhermefgl.rolling.model.User;
import com.guilhermefgl.rolling.view.details.DetailsViewContract;

public class DetailsPresenter implements DetailsPresenterContract {

    @NonNull
    private final DatabaseReference mTripDataBase;
    @NonNull
    private final DatabaseReference mUserDataBase;
    @NonNull
    private final FirebaseAuth mAuth;
    @NonNull
    private final DetailsViewContract mView;

    public DetailsPresenter(@NonNull DetailsViewContract view) {
        mTripDataBase = FirebaseHelper.getTripDatabaseInstance();
        mUserDataBase = FirebaseHelper.getUserDatabaseInstance();
        mAuth = FirebaseHelper.getAuthInstance();
        mView = view;
        mView.setPresenter(this);
    }

    @Override
    public void start() { }

    @Override
    public void stop() { }

    @Override
    public void addTripAsMarked() {
        if (mAuth.getCurrentUser() != null) {
            FirebaseUser firebaseUser = mAuth.getCurrentUser();
            User user = new User();
            user.setUserId(firebaseUser.getUid());
            user.setUserName(firebaseUser.getDisplayName());
            user.setUserEmail(firebaseUser.getEmail());
            if (firebaseUser.getPhotoUrl() != null) {
                user.setUserAvatarUrl(firebaseUser.getPhotoUrl().toString());
            }

            mUserDataBase.child(user.getUserId()).setValue(user).addOnCompleteListener(
                    new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                mView.onUpdateMarkedTripSucess(true);
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
        if (mAuth.getCurrentUser() != null) {
            mUserDataBase.child(mAuth.getCurrentUser().getUid()).removeValue().addOnCompleteListener(
                    new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                mView.onUpdateMarkedTripSucess(false);
                            } else {
                                mView.onUpdateMarkedTripFailure();
                            }
                        }
                    });
        }
    }
}
