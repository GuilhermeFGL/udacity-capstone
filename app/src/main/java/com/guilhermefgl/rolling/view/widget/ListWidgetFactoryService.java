package com.guilhermefgl.rolling.view.widget;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.guilhermefgl.rolling.R;
import com.guilhermefgl.rolling.mock.TripMock;
import com.guilhermefgl.rolling.model.Place;
import com.guilhermefgl.rolling.model.Trip;

import java.util.List;

public class ListWidgetFactoryService extends RemoteViewsService {

    // TODO remove mock data
    private Trip mTrip = TripMock.getMyCurrentTrip();


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
            mPlaces = null;
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
