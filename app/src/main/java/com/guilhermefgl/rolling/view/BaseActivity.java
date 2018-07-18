package com.guilhermefgl.rolling.view;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;

public abstract class BaseActivity extends AppCompatActivity {

    public final static String AVATAR_FILE_TYPE = "image/*";

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    protected boolean isForeground() {
        return !(isFinishing() || isDestroyed());
    }

}
