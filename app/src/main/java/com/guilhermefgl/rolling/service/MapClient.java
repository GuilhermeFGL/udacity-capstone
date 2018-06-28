package com.guilhermefgl.rolling.service;

import com.guilhermefgl.rolling.model.GoogleResponse;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class MapClient {

    private static final String BASE_URL = "https://maps.googleapis.com/maps/";

    private MapClient() { }

    public static MapsService getInstance() {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(MapsService.class);
    }

    public interface MapsService {

        @GET("api/directions/json?key=AIzaSyC22GfkHu9FdgT9SwdCWMwKX1a4aohGifM")
        Call<GoogleResponse> getDistanceDuration(@Query("units") String units,
                                                 @Query("origin") String origin,
                                                 @Query("destination") String destination,
                                                 @Query("mode") String mode);
    }
}
