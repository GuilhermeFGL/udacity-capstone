package com.guilhermefgl.rolling.view.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.RemoteViews;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.guilhermefgl.rolling.R;
import com.guilhermefgl.rolling.helper.FirebaseHelper;
import com.guilhermefgl.rolling.model.Trip;
import com.guilhermefgl.rolling.view.details.DetailsActivity;
import com.guilhermefgl.rolling.view.splash.SplashActivity;

public class TripWidgetProvider extends AppWidgetProvider {

    private static final int INTENT_REQUEST_CODE = 0;

    public static void update(Context context) {
        Intent intent = new Intent(context, TripWidgetProvider.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        int[] ids = AppWidgetManager.getInstance(context).getAppWidgetIds(
                new ComponentName(context, TripWidgetProvider.class));
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        context.sendBroadcast(intent);
    }

    @Override
    public void onUpdate(final Context context,
                         final AppWidgetManager appWidgetManager,
                         final int[] appWidgetIds) {

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
                                                    createUpdateAppWidget(context, appWidgetManager,
                                                            appWidgetIds, trip);
                                                }

                                                @Override
                                                public void onCancelled(
                                                        @NonNull DatabaseError databaseError) {
                                                    createUpdateAppWidget(context, appWidgetManager,
                                                            appWidgetIds, null);
                                                }
                                            });
                                } else {
                                    createUpdateAppWidget(context, appWidgetManager,
                                            appWidgetIds, null);
                                }
                            } else {
                                createUpdateAppWidget(context, appWidgetManager,
                                        appWidgetIds, null);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            createUpdateAppWidget(context, appWidgetManager,
                                    appWidgetIds, null);
                        }
                    });
        } else {
            createUpdateAppWidget(context, appWidgetManager, appWidgetIds, null);
        }
    }

    private void createUpdateAppWidget(final Context context,
                                       final AppWidgetManager appWidgetManager,
                                       final int[] appWidgetIds,
                                       Trip trip) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, trip);
        }
    }

    private void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                 int appWidgetId, Trip trip) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_trip);
        views.setEmptyView(R.id.widget_trip_list, R.id.widget_empty_layout);
        views.setRemoteAdapter(R.id.widget_trip_list,
                new Intent(context, ListWidgetFactoryService.class));

        if (trip != null) {
            views.setTextViewText(R.id.widget_title, trip.getTripName());

            Bundle extras = new Bundle();
            extras.putParcelable(DetailsActivity.BUNDLE_TRIP, trip);
            Intent appIntent = new Intent(context, DetailsActivity.class);
            appIntent.putExtras(extras);
            appIntent.setData(Uri.parse(appIntent.toUri(Intent.URI_INTENT_SCHEME)));
            PendingIntent appPendingIntent = PendingIntent
                    .getActivity(context, INTENT_REQUEST_CODE, appIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            views.setOnClickPendingIntent(R.id.widget_trip_layout, appPendingIntent);
        } else {
            Intent appIntent = new Intent(context, SplashActivity.class);
            PendingIntent appPendingIntent = PendingIntent
                    .getActivity(context, INTENT_REQUEST_CODE, appIntent, PendingIntent.FLAG_CANCEL_CURRENT);
            views.setOnClickPendingIntent(R.id.widget_empty_layout, appPendingIntent);
        }

        appWidgetManager.updateAppWidget(appWidgetId, views);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.widget_trip_list);
    }
}
