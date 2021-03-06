package com.guilhermefgl.rolling.view.trip;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.guilhermefgl.rolling.R;
import com.guilhermefgl.rolling.databinding.ActivityTripBinding;
import com.guilhermefgl.rolling.helper.CompressBitmap;
import com.guilhermefgl.rolling.helper.DateFormatterHelper;
import com.guilhermefgl.rolling.helper.DateTimeEditText;
import com.guilhermefgl.rolling.helper.MapDrawerHelper;
import com.guilhermefgl.rolling.helper.MapRouterHelper;
import com.guilhermefgl.rolling.helper.component.ProgressDialog;
import com.guilhermefgl.rolling.helper.component.ScrollableMapView;
import com.guilhermefgl.rolling.model.Place;
import com.guilhermefgl.rolling.model.Trip;
import com.guilhermefgl.rolling.presenter.trip.TripPresenter;
import com.guilhermefgl.rolling.presenter.trip.TripPresenterContract;
import com.guilhermefgl.rolling.view.BaseActivity;
import com.guilhermefgl.rolling.view.breakpoint.BreakPointAdapter;
import com.guilhermefgl.rolling.view.breakpoint.BreakPointItemTouchHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class TripActivity extends BaseActivity implements
        BreakPointAdapter.BreakPointAdapterItemClick, OnMapReadyCallback, View.OnClickListener,
        MapDrawerHelper.MapDrawnCallBack, BreakPointItemTouchHelper.BreakPointItemTouchListener,
        TripViewContract {

    private static final Integer RESULT_START = 1001;
    private static final Integer RESULT_END = 1002;
    private static final Integer RESULT_BREAK_POINT = 1003;
    private static final Integer REQUEST_IMAGE = 1004;
    private static final String FRAGMENT_PROGRESS_TAG = "FRAGMENT_PROGRESS_TAG";
    private static final String STATE_TRIP = "STATE_TRIP";
    private static final String STATE_BANNER = "STATE_BANNER";

    private ActivityTripBinding mBinding;
    private BreakPointAdapter mAdapter;
    private GoogleMap mMap;
    private MapDrawerHelper mMapDrawerHelper;
    private TripPresenterContract mPresenter;
    private ProgressDialog mProgressDialog;
    private Trip mTripState;
    private Bitmap mBannerState;

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
        new ItemTouchHelper(new BreakPointItemTouchHelper(0, ItemTouchHelper.LEFT, this))
                .attachToRecyclerView(mBinding.tripListBreakPoints);

        mBinding.tripDurationType.setAdapter(
                new ArrayAdapter<>(this,
                        android.R.layout.simple_spinner_dropdown_item,
                        getResources().getStringArray(R.array.trip_duration_types)));

        ScrollableMapView supportMapFragment = ((ScrollableMapView)
                getSupportFragmentManager().findFragmentById(R.id.include_trip_map_fragment));
        supportMapFragment.getMapAsync(this);
        supportMapFragment.setListener(new ScrollableMapView.OnTouchListener() {
            @Override
            public void onTouch() {
                mBinding.tripScroll.requestDisallowInterceptTouchEvent(true);
            }
        });

        mBinding.tripStart.setOnClickListener(this);
        mBinding.tripDestination.setOnClickListener(this);
        mBinding.tripImage.setOnClickListener(this);

        mMapDrawerHelper = new MapDrawerHelper(this, this);
        mProgressDialog = new ProgressDialog();

        if (savedInstanceState != null) {
            mTripState = savedInstanceState.getParcelable(STATE_TRIP);
            mBannerState = savedInstanceState.getParcelable(STATE_BANNER);
        }

        new TripPresenter(this);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        try {
            setTripState();
        } catch (Exception ignored) { }
        savedInstanceState.putParcelable(STATE_TRIP, mPresenter.getTripState());
        savedInstanceState.putParcelable(STATE_BANNER, mPresenter.getBannerState());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == RESULT_START || requestCode == RESULT_END
                    || requestCode == RESULT_BREAK_POINT) {
                final com.google.android.gms.location.places.Place googlePlace =
                        PlaceAutocomplete.getPlace(this, data);
                Place place = new Place() {{
                    setPlaceName(String.valueOf(googlePlace.getName()));
                    setPlaceLatitude(googlePlace.getLatLng().latitude);
                    setPlaceLongitude(googlePlace.getLatLng().longitude);
                }};

                if (requestCode == RESULT_START) {
                    mBinding.tripStart.setText(googlePlace.getName());
                    mPresenter.setStartPlace(place);
                } else if (requestCode == RESULT_END) {
                    mBinding.tripDestination.setText(googlePlace.getName());
                    mPresenter.setEndPlace(place);
                } else {
                    mAdapter.addBreakPoints(place);
                    mPresenter.addBreakPlace(place);
                }
            }

            if (requestCode == REQUEST_IMAGE && data != null) {
                try {
                    Bitmap bannerBitmap = MediaStore.Images.Media.getBitmap(
                            this.getContentResolver(), data.getData());
                    bannerBitmap = CompressBitmap.compress(bannerBitmap);
                    mBinding.tripImage.setImageBitmap(bannerBitmap);
                    mPresenter.setBanner(bannerBitmap);
                } catch (IOException e) {
                    Toast.makeText(this, R.string.error_image, Toast.LENGTH_SHORT).show();
                }
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
                finish();
                break;
            case R.id.menu_trip_save:
                save();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (mPresenter != null) {
            mPresenter.drawnMap();
        }
    }

    @Override
    public void setPresenter(@NonNull TripPresenterContract presenter) {
        mPresenter = presenter;

        if (mTripState != null) {
            mPresenter.setTripState(mTripState);
            mBinding.tripTitle.setText(mTripState.getTripName());
            mBinding.tripDistance.setText(mTripState.getTripDistance());

            if (mTripState.getPlaceStart() != null) {
                mBinding.tripStart.setText(mTripState.getPlaceStart().getPlaceName());
            }
            if (mTripState.getPlaceEnd() != null) {
                mBinding.tripDestination.setText(mTripState.getPlaceEnd().getPlaceName());
            }
            if (mTripState.getPlacesPoints() != null && !mTripState.getPlacesPoints().isEmpty()) {
                if (mTripState.getPlacesPoints().get(0) != null) {
                    mTripState.getPlacesPoints().add(0, null);
                }
                mAdapter.setBreakPoints(mTripState.getPlacesPoints());
            }

            if (mTripState.getTripDuration() != null) {
                String[] durationInfo = mTripState.getTripDuration().split(" ");
                if (durationInfo.length == 2) {
                    mBinding.tripDuration.setText(durationInfo[0]);
                    mBinding.tripDurationType.setSelection(
                            Arrays.asList(getResources().getStringArray(R.array.trip_duration_types))
                                    .indexOf(durationInfo[1]));
                }
            }

            try {
                mBinding.tripDate.setText(DateFormatterHelper.dateToString(
                        mTripState.getTripDate(), this));
            } catch (Exception ignored) { }
        }
        if (mBannerState != null) {
            mPresenter.setBanner(mBannerState);
            mBinding.tripImage.setImageBitmap(mBannerState);
        }

        mBinding.tripDate.addTextChangedListener(DateTimeEditText.mask(mBinding.tripDate));
    }

    @Override
    public void drawMap(MapRouterHelper mapRouterHelper) {
        if (mMap != null) {
            mBinding.tripProgress.setVisibility(View.VISIBLE);
            mBinding.tripStart.setEnabled(false);
            mBinding.tripDestination.setEnabled(false);
            mBinding.tripListBreakPoints.setEnabled(false);
            mMapDrawerHelper.drawnMap(mMap, mapRouterHelper);
        }
    }

    @Override
    public void onSaveBannerSuccess() {
        mPresenter.saveTrip();
    }

    @Override
    public void onSaveTripSuccess() {
        if (!(isFinishing() || isDestroyed())) {
            Toast.makeText(this, getString(R.string.success_create_trip), Toast.LENGTH_LONG)
                    .show();
            mProgressDialog.dismiss();
            finish();
        }
    }

    @Override
    public void onSaveTripFailure() {
        if (!(isFinishing() || isDestroyed())) {
            mBinding.tripProgress.setVisibility(View.GONE);
            mProgressDialog.dismiss();
            Toast.makeText(this, getString(R.string.error_create_trip), Toast.LENGTH_LONG)
                    .show();
        }
    }

    @Override
    public void onMapDrawnFinish(Double distance) {
        if (!isFinishing() && !isDestroyed() && mMap != null) {
            mBinding.tripProgress.setVisibility(View.GONE);
            mBinding.tripStart.setEnabled(true);
            mBinding.tripDestination.setEnabled(true);
            mBinding.tripListBreakPoints.setEnabled(true);
            mBinding.tripDistance.setText(distance != null ?
                    getString(R.string.trip_distance_format, distance) :
                    getString(R.string.trip_distance_hint));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.trip_start:
                openSearchPlaceWidget(RESULT_START);
                break;
            case R.id.trip_destination:
                openSearchPlaceWidget(RESULT_END);
                break;
            case R.id.trip_image:
                Intent libraryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                libraryIntent.setType(AVATAR_FILE_TYPE);

                Intent cameraIntent = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                cameraIntent.setType(AVATAR_FILE_TYPE);

                Intent chooserIntent = Intent.createChooser(libraryIntent,
                        getString(R.string.profile_avatar_chooser_title));
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{cameraIntent});

                startActivityForResult(chooserIntent, REQUEST_IMAGE);
                break;
        }
    }

    @Override
    public void onBreakPointItemCLick(Place place) {
        if (place == null && mBinding.tripListBreakPoints.isEnabled()) {
            openSearchPlaceWidget(RESULT_BREAK_POINT);
        }
    }

    @Override
    public void onBreakPointItemSwiped(int position) {
        mAdapter.removeBreakPoint(position);
        mPresenter.removeBreakPlace(position);
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

    private void setTripState() throws Exception {
        mPresenter.setTitle(mBinding.tripTitle.getText().toString());
        mPresenter.setDuration(mBinding.tripDuration.getText().toString()
                .concat(" ")
                .concat(mBinding.tripDurationType.getSelectedItem().toString()));
        if (!mBinding.tripDistance.getText().toString()
                .equals(getString(R.string.trip_distance_hint))) {
            mPresenter.setDistance(mBinding.tripDistance.getText().toString());
        } else {
            mPresenter.setDistance(null);
        }
        mPresenter.setDate(
                DateFormatterHelper.stringToDate(
                        mBinding.tripDate.getText().toString(), this));
    }

    private void save() {
        try {
            setTripState();
            mPresenter.save(this);
            mBinding.tripProgress.setVisibility(View.VISIBLE);
            mProgressDialog.show(getSupportFragmentManager(), FRAGMENT_PROGRESS_TAG);
        } catch (Exception e) {
            Toast.makeText(this, getString(R.string.error_create_trip), Toast.LENGTH_LONG)
                    .show();
        }
    }
}
