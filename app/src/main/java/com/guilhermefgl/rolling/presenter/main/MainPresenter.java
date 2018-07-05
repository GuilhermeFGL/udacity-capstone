package com.guilhermefgl.rolling.presenter.main;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.guilhermefgl.rolling.helper.FirebaseHelper;
import com.guilhermefgl.rolling.model.User;
import com.guilhermefgl.rolling.view.main.MainViewContract;

public class MainPresenter implements MainPresenterContract, FirebaseAuth.AuthStateListener {

    private FirebaseAuth mAuth;
    private MainViewContract mView;
    private Boolean mSmartLockEnabled;

    public MainPresenter(@NonNull MainViewContract view) {
        mAuth = FirebaseHelper.getAuthInstance();
        mSmartLockEnabled = true;
        mView = view;
        mView.setPresenter(this);
    }

    @Override
    public void start() {
        mAuth.addAuthStateListener(this);
        mView.updateUser(createUser(mAuth.getCurrentUser()));
    }

    @Override
    public void stop() {
        mAuth.removeAuthStateListener(this);
    }

    @Override
    public void login() {
        mView.goToLogin(mSmartLockEnabled);
    }

    @Override
    public void logout() {
        mAuth.signOut();
        mSmartLockEnabled = false;
        mView.goToLogout();
    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        mView.updateUser(createUser(firebaseAuth.getCurrentUser()));
    }

    private User createUser(@Nullable FirebaseUser currentUser) {
        return User.createFromFirebaseUser(currentUser);
    }
}
