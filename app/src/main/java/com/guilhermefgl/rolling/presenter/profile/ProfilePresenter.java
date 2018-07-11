package com.guilhermefgl.rolling.presenter.profile;

import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.guilhermefgl.rolling.helper.FirebaseHelper;
import com.guilhermefgl.rolling.model.User;
import com.guilhermefgl.rolling.view.profile.ProfileViewContract;

import java.io.ByteArrayOutputStream;

public class ProfilePresenter implements ProfilePresenterContract {

    private final FirebaseAuth mAuth;
    private final StorageReference mStorage;
    private final ProfileViewContract mView;

    public ProfilePresenter(@NonNull ProfileViewContract view) {
        mAuth = FirebaseHelper.getAuthInstance();
        mStorage = FirebaseHelper.getAvatarStorageInstance();
        mView = view;
        mView.setPresenter(this);
        mView.setUser(createUser(mAuth.getCurrentUser()), FirebaseHelper.isUserPasswordProvider());
    }

    @Override
    public void start() { }

    @Override
    public void stop() { }

    @Override
    public void changeName(String userName) {
        updateUser(new UserProfileChangeRequest.Builder()
                .setDisplayName(userName)
                .build());
    }

    @Override
    public void changePassword(final String userPassword, final String oldPassword) {
        final FirebaseUser user = mAuth.getCurrentUser();
        if (user == null || !FirebaseHelper.isUserPasswordProvider()) {
            return;
        }

        user.reauthenticate(EmailAuthProvider.getCredential(user.getEmail(), oldPassword))
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            user.updatePassword(userPassword)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                mView.onUpdateUserSuccess();
                                            } else {
                                                mView.onUpdateUserFailure();
                                            }
                                        }
                                    });
                        } else {
                            mView.onUpdateUserFailure();
                        }
                    }
                });
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
                mStorage.child(FirebaseHelper.createUniqueId());
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
