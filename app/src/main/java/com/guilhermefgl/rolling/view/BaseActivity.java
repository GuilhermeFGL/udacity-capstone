package com.guilhermefgl.rolling.view;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;

public abstract class BaseActivity extends AppCompatActivity {

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

}
