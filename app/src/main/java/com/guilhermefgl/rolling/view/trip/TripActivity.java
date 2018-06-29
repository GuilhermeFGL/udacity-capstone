package com.guilhermefgl.rolling.view.trip;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.guilhermefgl.rolling.R;
import com.guilhermefgl.rolling.component.ScrollableMapView;
import com.guilhermefgl.rolling.databinding.ActivityTripBinding;
import com.guilhermefgl.rolling.helper.MapDrawer;
import com.guilhermefgl.rolling.helper.MapRouter;
import com.guilhermefgl.rolling.model.Place;
import com.guilhermefgl.rolling.view.BaseActivity;
import com.guilhermefgl.rolling.view.breakpoint.BreakPointAdapter;

import java.util.ArrayList;

public class TripActivity extends BaseActivity implements
        BreakPointAdapter.BreakPointAdapterItemClick, OnMapReadyCallback, View.OnClickListener, MapDrawer.MapDrawnCallBack {

    private static final Integer RESULT_START = 1001;
    private static final Integer RESULT_END = 1002;
    private static final Integer RESULT_BREAK_POINT = 1003;

    private ActivityTripBinding mBinding;
    private BreakPointAdapter mAdapter;
    private GoogleMap mMap;
    private MapDrawer mMapDrawer;
    private MapRouter mMapRouter;

    public static void startActivity(BaseActivity activity) {
        activity.startActivity(
                new Intent(activity, TripActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_trip);

        setSupportActionBar(mBinding.tripToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(getString(R.string.trip_title));
        }

        mAdapter = new BreakPointAdapter(null, this);
        mBinding.tripListBreakPoints.setAdapter(mAdapter);
        ArrayList<Place> breakPoints = new ArrayList<>();
        breakPoints.add(0, null);
        mAdapter.setBreakPoints(breakPoints);

        mBinding.tripDurationType.setAdapter(
                new ArrayAdapter<>(this,
                        android.R.layout.simple_spinner_dropdown_item,
                        getResources().getStringArray(R.array.trip_duration_types)));

        ScrollableMapView supportMapFragment = ((ScrollableMapView)
                getSupportFragmentManager().findFragmentById(R.id.trip_map_fragment));
        supportMapFragment.getMapAsync(this);
        supportMapFragment.setListener(new ScrollableMapView.OnTouchListener() {
            @Override
            public void onTouch() {
                mBinding.tripScroll.requestDisallowInterceptTouchEvent(true);
            }
        });

        mBinding.tripStart.setOnClickListener(this);
        mBinding.tripDestination.setOnClickListener(this);

        mMapDrawer = new MapDrawer(this, this);
        mMapRouter = new MapRouter();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == RESULT_START || requestCode == RESULT_END
                    || requestCode == RESULT_BREAK_POINT) {
                final com.google.android.gms.location.places.Place googlePlace =
                        PlaceAutocomplete.getPlace(this, data);

                if (requestCode == RESULT_START) {
                    mBinding.tripStart.setText(googlePlace.getName());
                    mMapRouter.setStartPoint(new Place() {{
                        setPlaceName(String.valueOf(googlePlace.getName()));
                        setPlaceLatitude(googlePlace.getLatLng().latitude);
                        setPlaceLongitude(googlePlace.getLatLng().longitude);
                    }});
                } else if (requestCode == RESULT_END) {
                    mBinding.tripDestination.setText(googlePlace.getName());
                    mMapRouter.setEndPoint(new Place() {{
                        setPlaceName(String.valueOf(googlePlace.getName()));
                        setPlaceLatitude(googlePlace.getLatLng().latitude);
                        setPlaceLongitude(googlePlace.getLatLng().longitude);
                    }});
                } else {
                    Place place = new Place() {{
                        setPlaceName(googlePlace.getName().toString());
                        setPlaceLatitude(googlePlace.getLatLng().latitude);
                        setPlaceLongitude(googlePlace.getLatLng().longitude);
                    }};
                    mAdapter.addBreakPoints(place);
                    mMapRouter.addBreakPlace(place);
                }

                mMapDrawer.drawnMap(mMap, mMapRouter);
            }
        }
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
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.trip_start) {
            openSearchPlaceWidget(RESULT_START);
        } else if (v.getId() == R.id.trip_destination) {
            openSearchPlaceWidget(RESULT_END);
        }
    }

    @Override
    public void itemCLick(Place place) {
        if (place == null) {
            openSearchPlaceWidget(RESULT_BREAK_POINT);
        }
    }

    @Override
    public void onMapDrawnFinish(Double distance) {
        mBinding.tripDistance.setText(getString(R.string.trip_distance_format, distance));
    }

    private void openSearchPlaceWidget(Integer result) {
        try {
            startActivityForResult(
                    new PlaceAutocomplete
                            .IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                            .build(this),
                    result);
        } catch (Exception e) {
            Toast.makeText(this,
                    getString(R.string.error_google_autocomplete_widget),
                    Toast.LENGTH_LONG)
                    .show();
        }
    }
}
