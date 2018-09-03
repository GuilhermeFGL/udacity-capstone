package com.guilhermefgl.rolling.presenter.trip;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.guilhermefgl.rolling.R;
import com.guilhermefgl.rolling.helper.FirebaseHelper;
import com.guilhermefgl.rolling.helper.MapRouterHelper;
import com.guilhermefgl.rolling.model.Place;
import com.guilhermefgl.rolling.model.Trip;
import com.guilhermefgl.rolling.model.User;
import com.guilhermefgl.rolling.view.trip.TripViewContract;

import java.io.ByteArrayOutputStream;
import java.util.Date;

public class TripPresenter implements TripPresenterContract {

    @NonNull
    private final DatabaseReference mTripDataBase;
    @NonNull
    private final DatabaseReference mUserDataBase;
    @NonNull
    private final StorageReference mStorage;
    @NonNull
    private final FirebaseAuth mAuth;
    @NonNull
    private final Trip mTrip;
    @NonNull
    private final MapRouterHelper mRoute;
    @NonNull
    private final TripViewContract mView;
    @Nullable
    private Bitmap mBannerBitmap;

    public TripPresenter(@NonNull TripViewContract view) {
        mTripDataBase = FirebaseHelper.getTripDatabaseInstance();
        mUserDataBase = FirebaseHelper.getUserDatabaseInstance();
        mStorage = FirebaseHelper.getBannerStorageInstance();
        mAuth = FirebaseHelper.getAuthInstance();
        mTrip = new Trip();
        mRoute = new MapRouterHelper();
        mView = view;
        mView.setPresenter(this);
    }

    @Override
    public void start() { }

    @Override
    public void stop() { }

    @Override
    public void setBanner(Bitmap banner) {
        mBannerBitmap = banner;
    }

    @Override
    public void setTitle(String title) {
        mTrip.setTripName(title);
    }

    @Override
    public void setDate(Date date) {
        mTrip.setTripDate(new Date(date.getTime()));
    }

    @Override
    public void setDuration(String duration) {
        mTrip.setTripDuration(duration);
    }

    @Override
    public void setDistance(String distance) {
        mTrip.setTripDistance(distance);
    }

    @Override
    public void setStartPlace(Place startPlace) {
        mRoute.setStartPoint(startPlace.clone());
        mView.drawMap(mRoute);
    }

    @Override
    public void setEndPlace(Place endPlace) {
        mRoute.setEndPoint(endPlace.clone());
        mView.drawMap(mRoute);
    }

    @Override
    public void addBreakPlace(Place breakPlace) {
        mRoute.addBreakPlace(breakPlace.clone());
        mView.drawMap(mRoute);
    }

    @Override
    public void removeBreakPlace(int position) {
        mRoute.removeBreakPlace(position);
    }

    @Override
    public boolean isValid(Context context) throws UnsupportedOperationException {
        if (mTrip.getTripName() == null || mTrip.getTripName().isEmpty()) {
            throw new UnsupportedOperationException(context.getString(R.string.error_save_trip_title));
        }
        if (mTrip.getTripDate().before(new Date())) {
            throw new UnsupportedOperationException(context.getString(R.string.error_save_trip_date));
        }
        if (mTrip.getPlaceStart() == null) {
            throw new UnsupportedOperationException(context.getString(R.string.error_save_trip_start));
        }
        if (mTrip.getPlaceEnd() == null) {
            throw new UnsupportedOperationException(context.getString(R.string.error_save_trip_end));
        }
        if (mTrip.getTripDistance() == null) {
            throw new UnsupportedOperationException(context.getString(R.string.error_save_trip_distance));
        }
        if (mTrip.getTripDuration() == null) {
            throw new UnsupportedOperationException(context.getString(R.string.error_save_trip_duration));
        }
        return true;
    }

    @Override
    public void save(Context context) throws UnsupportedOperationException {
        String uId = FirebaseHelper.createUniqueId();
        mTrip.setTripId(uId);
        mTrip.setPlaceStart(mRoute.getStartPoint());
        mTrip.setPlaceEnd(mRoute.getEndPlace());
        mTrip.setPlacesPoints(mRoute.getBreakPlaces());
        if (isValid(context) && mAuth.getCurrentUser() != null) {
            mTrip.setUserOwner(mAuth.getCurrentUser().getUid());

            if (mBannerBitmap != null) {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                mBannerBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] data = stream.toByteArray();

                final StorageReference uploadReference = mStorage.child(uId);
                uploadReference.putBytes(data)
                        .continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                            @Override
                            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task)
                                    throws Exception {
                                if (!task.isSuccessful() && task.getException() != null) {
                                    throw task.getException();
                                }
                                return uploadReference.getDownloadUrl();
                            }
                        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                if (task.isSuccessful() && mAuth.getCurrentUser() != null) {
                                    mTrip.setTripBannerUrl(task.getResult().toString());
                                    mView.onSaveBannerSuccess();
                                } else {
                                    mView.onSaveTripFailure();
                                }
                            }
                        });
            } else {
                saveTrip();
            }
        } else {
            throw new UnsupportedOperationException(context.getString(R.string.error_save_trip));
        }
    }

    @Override
    public void saveTrip() throws UnsupportedOperationException {
        if (mAuth.getCurrentUser() != null) {
            FirebaseUser firebaseUser = mAuth.getCurrentUser();
            User user = new User();
            user.setUserId(firebaseUser.getUid());
            user.setUserName(firebaseUser.getDisplayName());
            user.setUserEmail(firebaseUser.getEmail());
            if (firebaseUser.getPhotoUrl() != null) {
                user.setUserAvatarUrl(firebaseUser.getPhotoUrl().toString());
            }
            mUserDataBase.child(mTrip.getTripId()).child(user.getUserId()).setValue(user);
            mTripDataBase.child(mTrip.getTripId())
                    .setValue(mTrip, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable DatabaseError databaseError,
                                               @NonNull DatabaseReference databaseReference) {
                            if (databaseError == null || databaseError.getMessage().isEmpty()) {
                                mView.onSaveTripSuccess();
                            } else {
                                mView.onSaveTripFailure();
                            }
                        }
                    });
        }
    }

    @Override
    public Trip getTripState() {
        mTrip.setPlaceStart(mRoute.getStartPoint());
        mTrip.setPlaceEnd(mRoute.getEndPlace());
        mTrip.setPlacesPoints(mRoute.getBreakPlaces());
        return mTrip;
    }

    @Override
    public void setTripState(Trip tripState) {
        if (tripState != null) {
            mTrip.setTripName(tripState.getTripName());
            mTrip.setTripDate(tripState.getTripDate());
            mTrip.setTripDistance(tripState.getTripDistance());
            mTrip.setTripBannerUrl(tripState.getTripBannerUrl());
            mTrip.setUserOwner(tripState.getUserOwner());
            mTrip.setTripId(tripState.getTripId());
            mTrip.setTripDuration(tripState.getTripDuration());

            mRoute.addBreakPlace(tripState.getPlacesPoints());
            mRoute.setEndPoint(tripState.getPlaceEnd());
            mRoute.setStartPoint(tripState.getPlaceStart());
            mView.drawMap(mRoute);
        }
    }

    @Override
    public Bitmap getBannerState() {
        return mBannerBitmap;
    }

    @Override
    public void drawnMap() {
        mView.drawMap(mRoute);
    }
}
