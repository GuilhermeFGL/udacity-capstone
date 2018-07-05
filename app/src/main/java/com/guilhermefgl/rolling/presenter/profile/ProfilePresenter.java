package com.guilhermefgl.rolling.presenter.profile;

import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.guilhermefgl.rolling.helper.FirebaseHelper;
import com.guilhermefgl.rolling.model.User;
import com.guilhermefgl.rolling.view.profile.ProfileViewContract;

import java.io.ByteArrayOutputStream;

public class ProfilePresenter implements ProfilePresenterContract, FirebaseAuth.AuthStateListener {

    private final FirebaseAuth mAuth;
    private final StorageReference mStorage;
    private final ProfileViewContract mView;

    public ProfilePresenter(@NonNull ProfileViewContract view) {
        mAuth = FirebaseHelper.getAuthInstance();
        mStorage = FirebaseHelper.getAvatarStorageInstance();
        mView = view;
        mView.setPresenter(this);
        for(String provider : mAuth.getCurrentUser().getProviders()) {
            String p = provider;
        }
    }

    @Override
    public void start() {
        mAuth.addAuthStateListener(this);
        mView.setUser(createUser(mAuth.getCurrentUser()), FirebaseHelper.isUserPasswordProvider());
    }

    @Override
    public void stop() {
        mAuth.removeAuthStateListener(this);
    }

    @Override
    public void changeName(String userName) {
        updateUser(new UserProfileChangeRequest.Builder()
                .setDisplayName(userName)
                .build());
    }

    @Override
    public void changePassword(String userPassword) {

    }

    @Override
    public void changeAvatar(Bitmap userAvatar) {
        if (mAuth.getCurrentUser() == null) {
            return;
        }

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        userAvatar.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] data = stream.toByteArray();

        final StorageReference uploadReference =
                mStorage.child(FirebaseHelper.createUniqueFileName());
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
                            updateUser(new UserProfileChangeRequest.Builder()
                                    .setPhotoUri(task.getResult())
                                    .build());
                        } else {
                            mView.onUpdateUserFailure();
                        }
                    }
                });
    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        mView.setUser(createUser(firebaseAuth.getCurrentUser()), FirebaseHelper.isUserPasswordProvider());
    }

    private User createUser(@Nullable FirebaseUser currentUser) {
        return User.createFromFirebaseUser(currentUser);
    }

    private void updateUser(UserProfileChangeRequest profileUpdates) {
        if (mAuth.getCurrentUser() == null) {
            return;
        }

        mAuth.getCurrentUser().updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            mAuth.getCurrentUser().reload()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            mView.onUpdateUserSuccess();
                                        }
                                    });
                        } else {
                            mView.onUpdateUserFailure();
                        }
                    }
                });
    }
}
