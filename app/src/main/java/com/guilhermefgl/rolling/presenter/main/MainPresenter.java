package com.guilhermefgl.rolling.presenter.main;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.guilhermefgl.rolling.helper.FirebaseHelper;
import com.guilhermefgl.rolling.model.User;
import com.guilhermefgl.rolling.view.main.MainViewContract;

public class MainPresenter implements MainPresenterContract {

    private FirebaseAuth mAuth;
    private MainViewContract mView;
    private Boolean mSmartLockEnabled;

    public MainPresenter(@NonNull MainViewContract view) {
        mAuth = FirebaseHelper.getAuthInstance();
        mSmartLockEnabled = true;
        mView = view;
        mView.setPresenter(this);
        mView.updateUser(createUser(mAuth.getCurrentUser()));
    }

    @Override
    public void start() { }

    @Override
    public void stop() { }

    @Override
    public void login() {
        mView.goToLogin(mSmartLockEnabled);
    }

    @Override
    public void logout() {
        mAuth.signOut();
        mSmartLockEnabled = false;
        refresh();
        mView.goToLogout();
    }

    @Override
    public void refresh() {
        mView.updateUser(createUser(mAuth.getCurrentUser()));
    }

    private User createUser(@Nullable FirebaseUser currentUser) {
        return User.createFromFirebaseUser(currentUser);
    }
}
