package com.guilhermefgl.rolling.view.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;

import com.guilhermefgl.rolling.R;
import com.guilhermefgl.rolling.model.Trip;
import com.guilhermefgl.rolling.service.taks.SelectCurrentWidgetTripTask;
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
        new SelectCurrentWidgetTripTask(context, new SelectCurrentWidgetTripTask.SelectTripCallBack() {
            @Override
            public void onSelect(Trip trip) {
                for (int appWidgetId : appWidgetIds) {
                    updateAppWidget(context, appWidgetManager, appWidgetId, trip);
                }
            }
        }).execute();
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
