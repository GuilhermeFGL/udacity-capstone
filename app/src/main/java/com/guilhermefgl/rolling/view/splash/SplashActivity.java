package com.guilhermefgl.rolling.view.splash;

import android.os.Bundle;
import android.view.View;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.guilhermefgl.rolling.R;
import com.guilhermefgl.rolling.view.BaseActivity;
import com.guilhermefgl.rolling.view.main.MainActivity;
import com.guilhermefgl.rolling.view.widget.TripWidgetProvider;

public class SplashActivity extends BaseActivity implements OnMapReadyCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // pre load map
        findViewById(R.id.splash_map).setVisibility(View.GONE);
        ((SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.splash_map))
                .getMapAsync(this);

        TripWidgetProvider.update(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MainActivity.startActivity(this);
    }
}
