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
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.guilhermefgl.rolling.helper.FirebaseHelper;
import com.guilhermefgl.rolling.helper.MapRouter;
import com.guilhermefgl.rolling.model.Place;
import com.guilhermefgl.rolling.model.Trip;
import com.guilhermefgl.rolling.view.trip.TripViewContract;

import java.io.ByteArrayOutputStream;
import java.util.Date;

public class TripPresenter implements TripPresenterContract {

    @NonNull
    private final DatabaseReference mDataBase;
    @NonNull
    private final StorageReference mStorage;
    @NonNull
    private final FirebaseAuth mAuth;
    @NonNull
    private final Trip mTrip;
    @NonNull
    private final MapRouter mRoute;
    @NonNull
    private final TripViewContract mView;
    @Nullable
    private Bitmap mBannerBitmap;

    public TripPresenter(@NonNull TripViewContract view) {
        mDataBase = FirebaseHelper.getTripDatabaseInstance();
        mStorage = FirebaseHelper.getBannerStorageInstance();
        mAuth = FirebaseHelper.getAuthInstance();
        mTrip = new Trip();
        mRoute = new MapRouter();
        mView = view;
        mView.setPresenter(this);
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

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
        mTrip.setTripDate(date);
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
        mRoute.setStartPoint(startPlace);
    }

    @Override
    public void setEndPlace(Place endPlace) {
        mRoute.setEndPoint(endPlace);
    }

    @Override
    public void addBreakPlace(Place breakPlace) {
        mRoute.addBreakPlace(breakPlace);
    }

    @Override
    public void removeBreakPlace(int position) {
        mRoute.removeBreakPlace(position);
    }

    @Override
    public boolean isValid(Context context) throws UnsupportedOperationException {
        if (mTrip.getTripName() == null || mTrip.getTripName().isEmpty()) {
            throw new UnsupportedOperationException("Invalid title");
        }
        if (mTrip.getTripDate().before(new Date())) {
            throw new UnsupportedOperationException("Invalid date");
        }
        if (mTrip.getPlaceStart() == null) {
            throw new UnsupportedOperationException("Invalid starter place");
        }
        if (mTrip.getPlaceEnd() == null) {
            throw new UnsupportedOperationException("Invalid destination place");
        }
        if (mTrip.getTripDistance() == null || mTrip.getTripDistance().equals("0")) {
            throw new UnsupportedOperationException("Invalid distance");
        }
        if (mTrip.getTripDuration() == null) {
            throw new UnsupportedOperationException("Invalid duration");
        }
        return true;
    }

    @Override
    public void save(Context context) throws UnsupportedOperationException {
        if (isValid(context) && mAuth.getCurrentUser() != null) {
            String uId = FirebaseHelper.createUniqueId();

            mTrip.setTripId(uId);
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
                                    postTrip();
                                } else {
                                    mView.onSaveTripFailure();
                                }
                            }
                        });
            } else {
                postTrip();
            }
        } else {
            throw new UnsupportedOperationException("Invalid trip");
        }
    }

    private void postTrip() {
        mDataBase.child(mTrip.getTripId())
                .setValue(mTrip, new DatabaseReference.CompletionListener(){
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
