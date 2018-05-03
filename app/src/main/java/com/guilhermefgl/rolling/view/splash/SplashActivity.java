package com.guilhermefgl.rolling.view.splash;

import android.os.Bundle;

import com.guilhermefgl.rolling.view.BaseActivity;
import com.guilhermefgl.rolling.view.main.MainActivity;

public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MainActivity.startActivity(this);
    }
}
