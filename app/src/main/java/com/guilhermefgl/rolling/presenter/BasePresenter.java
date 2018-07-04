package com.guilhermefgl.rolling.presenter;

import android.support.annotation.NonNull;

import com.guilhermefgl.rolling.view.BaseView;

public interface BasePresenter<T extends BaseView> {

    void setView(@NonNull T view);

    void start();

    void stop();
}
