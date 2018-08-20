package com.guilhermefgl.rolling.service;

import com.guilhermefgl.rolling.BuildConfig;
import com.guilhermefgl.rolling.model.GoogleResponse;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class MapClient {

    private static final String BASE_URL = "https://maps.googleapis.com/maps/";

    private static MapsService mInstance;

    private MapClient() { }

    public static MapsService getInstance() {
        if (mInstance != null) {
            return mInstance;
        }

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(BuildConfig.DEBUG ?
                HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);
        mInstance = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(MapsService.class);
        return mInstance;
    }

    public interface MapsService {

        @GET("api/directions/json?key=AIzaSyC22GfkHu9FdgT9SwdCWMwKX1a4aohGifM")
        Call<GoogleResponse> getDistanceDuration(@Query("units") String units,
                                                 @Query("origin") String origin,
                                                 @Query("destination") String destination,
                                                 @Query("mode") String mode);
    }
}
