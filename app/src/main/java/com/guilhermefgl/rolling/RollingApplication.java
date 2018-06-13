package com.guilhermefgl.rolling;

import android.app.Application;

import com.squareup.picasso.Picasso;

public class RollingApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            Picasso picasso = Picasso.get();
            picasso.setIndicatorsEnabled(true); // cache indicator
            picasso.setLoggingEnabled(true);    // logger
        }

    }

}
