package com.guilhermefgl.rolling.view.widget;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.guilhermefgl.rolling.R;
import com.guilhermefgl.rolling.helper.FirebaseHelper;
import com.guilhermefgl.rolling.model.Place;
import com.guilhermefgl.rolling.model.Trip;

import java.util.ArrayList;
import java.util.List;

public class ListWidgetFactoryService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new TripRemoteViewsFactory(this.getApplicationContext());
    }

    class TripRemoteViewsFactory implements RemoteViewsFactory {

        private final Context mContext;
        private List<Place> mPlaces;

        TripRemoteViewsFactory(Context applicationContext) {
            mContext = applicationContext;
        }

        @Override
        public void onCreate() { }

        @Override
        public void onDataSetChanged() {
            final DatabaseReference currentDataBase = FirebaseHelper.getCurrentDatabaseInstance();
            final DatabaseReference tripDataBase = FirebaseHelper.getTripDatabaseInstance();
            final FirebaseAuth auth = FirebaseHelper.getAuthInstance();

            if (auth.getCurrentUser() != null) {
                currentDataBase.child(auth.getCurrentUser().getUid())
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    String tripId = (String) dataSnapshot.getValue();
                                    if (tripId != null) {
                                        tripDataBase.child(tripId).addListenerForSingleValueEvent(
                                                new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(
                                                            @NonNull DataSnapshot dataSnapshot) {
                                                        Trip trip = dataSnapshot.getValue(Trip.class);
                                                        if (trip != null) {
                                                            mPlaces = new ArrayList<>();
                                                            mPlaces.add(trip.getPlaceStart());
                                                            if (trip.getPlacesPoints() != null &&
                                                                    !trip.getPlacesPoints().isEmpty()) {
                                                                mPlaces.addAll(trip.getPlacesPoints());
                                                            }
                                                            mPlaces.add(trip.getPlaceEnd());
                                                        } else {
                                                            mPlaces = null;
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(
                                                            @NonNull DatabaseError databaseError) {
                                                        mPlaces = null;
                                                    }
                                                });
                                    } else {
                                        mPlaces = null;
                                    }
                                } else {
                                    mPlaces = null;
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                mPlaces = null;
                            }
                        });
            } else {
                mPlaces = null;
            }
        }

        @Override
        public void onDestroy() {
            mPlaces = null;
        }

        @Override
        public int getCount() {
            return mPlaces != null ? mPlaces.size() : 0;
        }

        @Override
        public RemoteViews getViewAt(int position) {
            Place place = mPlaces.get(position);
            RemoteViews view = new RemoteViews(mContext.getPackageName(), R.layout.item_widget_trip);
            view.setTextViewText(R.id.item_widget_break_point, place.getPlaceName());
            return view;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }

}
