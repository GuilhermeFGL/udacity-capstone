package com.guilhermefgl.rolling.service.taks;

import android.content.Context;
import android.os.AsyncTask;

import com.guilhermefgl.rolling.model.Trip;

public class SelectCurrentWidgetTripTask extends AsyncTask<Void, Void, Trip> {

    private Trip mTrip = new Trip();

    private final SelectTripCallBack mCallBack;

    public SelectCurrentWidgetTripTask(Context context, SelectTripCallBack callBack) {
        mCallBack = callBack;
    }

    @Override
    protected Trip doInBackground(Void... voids) {
        return mTrip;
    }

    @Override
    protected void onPostExecute(Trip trip) {
        super.onPostExecute(trip);
        if (mCallBack != null) {
            mCallBack.onSelect(trip);
        }
    }

    public interface SelectTripCallBack {
        void onSelect(Trip trip);
    }
}
