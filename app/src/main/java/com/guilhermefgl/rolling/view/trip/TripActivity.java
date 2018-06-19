package com.guilhermefgl.rolling.view.trip;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.guilhermefgl.rolling.R;
import com.guilhermefgl.rolling.databinding.ActivityTripBinding;
import com.guilhermefgl.rolling.model.Place;
import com.guilhermefgl.rolling.view.BaseActivity;
import com.guilhermefgl.rolling.view.breakpoint.BreakPointAdapter;

import java.util.ArrayList;

public class TripActivity extends BaseActivity implements
        BreakPointAdapter.BreakPointAdapterItemClick, OnMapReadyCallback {

    public static void startActivity(BaseActivity activity) {
        activity.startActivity(
                new Intent(activity, TripActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityTripBinding mBinding =
                DataBindingUtil.setContentView(this, R.layout.activity_trip);

        setSupportActionBar(mBinding.tripToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(getString(R.string.trip_title));
        }

        BreakPointAdapter adapter = new BreakPointAdapter(null, this);
        mBinding.tripListBreakPoints.setAdapter(adapter);
        ArrayList<Place> breakPoints = new ArrayList<>();
        breakPoints.add(0, null);
        adapter.setBreakPoints(breakPoints);

        mBinding.tripDurationType.setAdapter(
                new ArrayAdapter<String>(this,
                        android.R.layout.simple_spinner_dropdown_item,
                        getResources().getStringArray(R.array.trip_duration_types)));

        mBinding.tripDistanceType.setAdapter(
                new ArrayAdapter<String>(this,
                        android.R.layout.simple_spinner_dropdown_item,
                        getResources().getStringArray(R.array.trip_distance_types)));

        ((MapFragment) getFragmentManager().findFragmentById(R.id.trip_map_fragment))
                .getMapAsync(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_new_trip, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.menu_trip_save:
                // TODO save
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        // TODO confirm
        finish();
    }

    @Override
    public void itemCLick(Place place) { }

    @Override
    public void onMapReady(GoogleMap googleMap) { }
}
