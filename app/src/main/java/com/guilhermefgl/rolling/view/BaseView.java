package com.guilhermefgl.rolling.view;

import android.support.annotation.NonNull;

import com.guilhermefgl.rolling.presenter.BasePresenter;

public interface BaseView<T extends BasePresenter> {

    void setPresenter(@NonNull T presenter);
}
