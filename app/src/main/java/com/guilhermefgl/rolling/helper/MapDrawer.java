package com.guilhermefgl.rolling.helper;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.guilhermefgl.rolling.model.Place;
import com.guilhermefgl.rolling.model.Trip;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapDrawer {

    private final Context mContext;

    public MapDrawer(Context context) {
        mContext = context;
    }

    public void drawnMap(GoogleMap map, Trip trip) {
        map.clear();

        LatLng originMarker = null;
        LatLng destinationMarker = null;
        if (trip.getPlaceStart() != null) {
            MarkerOptions marker = createMarker(trip.getPlaceStart(), BitmapDescriptorFactory.HUE_AZURE);
            originMarker = marker.getPosition();
            map.addMarker(marker);
        }
        if (trip.getPlaceEnd() != null) {
            MarkerOptions marker = createMarker(trip.getPlaceEnd(), BitmapDescriptorFactory.HUE_MAGENTA);
            destinationMarker = marker.getPosition();
            map.addMarker(marker);
        }
        if (trip.getPlacesPoints() != null && !trip.getPlacesPoints().isEmpty()) {
            for (Place place : trip.getPlacesPoints()) {
                map.addMarker(createMarker(place, BitmapDescriptorFactory.HUE_CYAN));
            }
        }

        if (originMarker != null && destinationMarker != null) {
            map.moveCamera(CameraUpdateFactory.newLatLng(originMarker));
        }else if (originMarker != null) {
            map.moveCamera(CameraUpdateFactory.newLatLng(originMarker));
        } else if (destinationMarker != null) {
            map.moveCamera(CameraUpdateFactory.newLatLng(destinationMarker));
        }
    }

    private MarkerOptions createMarker(@NonNull Place place, float iconHUE) {
        return new MarkerOptions()
                .position(new LatLng(place.getPlaceLatitude(), place.getPlaceLongitude()))
                .title(place.getPlaceName())
                .icon(BitmapDescriptorFactory.defaultMarker(iconHUE));
    }

















    private static class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... url) {
            String data = "";
            try {
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            new ParserTask().execute(result);
        }

        private String downloadUrl(String strUrl) throws IOException {
            String data = "";
            InputStream iStream = null;
            HttpURLConnection urlConnection = null;

            try {
                urlConnection = (HttpURLConnection) new URL(strUrl).openConnection();
                urlConnection.connect();

                iStream = urlConnection.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
                StringBuilder sb = new StringBuilder();

                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }

                data = sb.toString();
                br.close();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (iStream != null) {
                    iStream.close();
                }
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
            return data;
        }
    }

    private static class ParserTask extends AsyncTask<String, Integer, List<List<HashMap>>> {

        @Override
        protected List<List<HashMap>> doInBackground(String... jsonData) {
            JSONObject jObject;
            List<List<HashMap>> routes = null;
            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap>> result) {
            ArrayList points = null;
            PolylineOptions lineOptions = null;
            MarkerOptions markerOptions = new MarkerOptions();

            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList();
                lineOptions = new PolylineOptions();

                List<HashMap> path = result.get(i);

                for (int j = 0; j < path.size(); j++) {
                    HashMap point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                lineOptions.addAll(points);
                lineOptions.width(12);
                lineOptions.color(Color.RED);
                lineOptions.geodesic(true);

            }

            mMap.addPolyline(lineOptions);
        }
    }

    private String getDirectionsUrl(LatLng origin, LatLng dest) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";
        String mode = "mode=driving";

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor + "&" + mode;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;


        return url;
    }

}
