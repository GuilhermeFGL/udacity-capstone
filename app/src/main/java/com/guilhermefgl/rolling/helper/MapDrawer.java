package com.guilhermefgl.rolling.helper;

import android.app.Activity;
import android.graphics.Color;
import android.support.annotation.NonNull;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.guilhermefgl.rolling.model.GoogleResponse;
import com.guilhermefgl.rolling.model.Place;
import com.guilhermefgl.rolling.service.MapClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapDrawer {

    private static final String MAP_UNIT = "metric";
    private static final String ROUTE_TYPE = "driving";
    private static final Integer ROUTE_WIDTH = 20;
    private static final Integer ROUTE_COLOR = Color.RED;

    private final Activity mActivity;
    private GoogleMap mMap;
    private Double mDistance;
    private MapDrawnCallBack mCallBack;

    public MapDrawer(Activity activity, MapDrawnCallBack callBack) {
        mActivity = activity;
        mCallBack = callBack;
        mDistance = 0d;
    }

    public void drawnMap(GoogleMap map, MapRouter mapRouter) {
        mMap = map;
        mMap.clear();

        LatLng originMarker = null;
        LatLng destinationMarker = null;
        ArrayList<LatLng> positions = new ArrayList<>();
        if (mapRouter.getStartPoint() != null) {
            MarkerOptions marker = createMarker(mapRouter.getStartPoint(), BitmapDescriptorFactory.HUE_AZURE);
            originMarker = marker.getPosition();
            mMap.addMarker(marker);
            positions.add(originMarker);
        }
        if (mapRouter.getBreakPlaces() != null && !mapRouter.getBreakPlaces().isEmpty()) {
            for (Place place : mapRouter.getBreakPlaces()) {
                MarkerOptions breakMarker = createMarker(place, BitmapDescriptorFactory.HUE_CYAN);
                mMap.addMarker(breakMarker);
                positions.add(breakMarker.getPosition());
            }
        }
        if (mapRouter.getEndPlace() != null) {
            MarkerOptions marker = createMarker(mapRouter.getEndPlace(), BitmapDescriptorFactory.HUE_MAGENTA);
            destinationMarker = marker.getPosition();
            mMap.addMarker(marker);
            positions.add(destinationMarker);
        }

        if (originMarker != null && destinationMarker != null) {
            mMap.moveCamera(CameraUpdateFactory.newLatLng(originMarker));
        } else if (originMarker != null) {
            mMap.moveCamera(CameraUpdateFactory.newLatLng(originMarker));
        } else if (destinationMarker != null) {
            mMap.moveCamera(CameraUpdateFactory.newLatLng(destinationMarker));
        }

        requestMapService(positions);
    }

    private void requestMapService(final ArrayList<LatLng> positions){
        if (positions.size() > 1) {
            LatLng positionA = positions.remove(0);
            LatLng positionB = positions.get(0);

            MapClient.getInstance()
                    .getDistanceDuration(
                            MAP_UNIT,
                            positionA.latitude + "," + positionA.longitude,
                            positionB.latitude + "," + positionB.longitude,
                            ROUTE_TYPE)
                    .enqueue(new Callback<GoogleResponse>() {
                        @Override
                        public void onResponse(@NonNull Call<GoogleResponse> call,
                                               @NonNull Response<GoogleResponse> response) {
                            if (mActivity.isFinishing() || mActivity.isDestroyed()) {
                                return;
                            }

                            GoogleResponse googleResponse = response.body();
                            if (googleResponse != null) {
                                for (int i = 0; i < googleResponse.getRoutes().size(); i++) {
                                    String distanceResource = googleResponse.getRoutes().get(i)
                                            .getLegs().get(i).getDistance().getText();
                                    List<LatLng> polygons = decodePoly(googleResponse.getRoutes().get(0)
                                            .getOverviewPolyline().getPoints());
                                    mDistance += Double.valueOf(distanceResource);
                                    mMap.addPolyline(
                                            new PolylineOptions()
                                                    .addAll(polygons)
                                                    .width(ROUTE_WIDTH)
                                                    .color(ROUTE_COLOR)
                                                    .geodesic(true)
                                    );
                                }
                            }

                            requestMapService(positions);
                        }

                        @Override
                        public void onFailure(@NonNull Call<GoogleResponse> call, @NonNull Throwable t) {
                            t.printStackTrace();
                        }
                    });
        } else {
            mCallBack.onMapDrawnFinish(mDistance);
        }
    }

    private MarkerOptions createMarker(@NonNull Place place, float iconHUE) {
        return new MarkerOptions()
                .position(new LatLng(place.getPlaceLatitude(), place.getPlaceLongitude()))
                .title(place.getPlaceName())
                .icon(BitmapDescriptorFactory.defaultMarker(iconHUE));
    }

    private List<LatLng> decodePoly(String encoded) {
        List<LatLng> poly = new ArrayList<>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int distanceLat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += distanceLat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int distanceLng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += distanceLng;

            LatLng p = new LatLng( (((double) lat / 1E5)),
                    (((double) lng / 1E5) ));
            poly.add(p);
        }

        return poly;
    }

    public interface MapDrawnCallBack {
        void onMapDrawnFinish(Double distance);
    }
}
