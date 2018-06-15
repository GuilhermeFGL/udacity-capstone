package com.guilhermefgl.rolling.view.triplist;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.guilhermefgl.rolling.R;
import com.guilhermefgl.rolling.databinding.ItemTripBinding;
import com.guilhermefgl.rolling.helper.PicassoHelper;
import com.guilhermefgl.rolling.model.Trip;

import java.util.List;

public class TripAdapter extends RecyclerView.Adapter<TripAdapter.ViewHolder> {

    @Nullable
    private List<Trip> mTrips;
    @NonNull
    private TripAdapterItemClick mAdapterItemClick;


    TripAdapter(@Nullable List<Trip> trips, @NonNull TripAdapterItemClick adapterItemClick) {
        mTrips = trips;
        mAdapterItemClick = adapterItemClick;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemTripBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()), R.layout.item_trip, parent, false);
        return new TripAdapter.ViewHolder(binding);
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(mTrips.get(position), mAdapterItemClick);
    }

    @Override
    public int getItemCount() {
        return mTrips != null ? mTrips.size() : 0;
    }

    public void setTripList(List<Trip> trips) {
        this.mTrips = trips;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @NonNull
        private final ItemTripBinding mBinding;

        ViewHolder(@NonNull ItemTripBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        public void bind(final Trip trip, final TripAdapterItemClick adapterItemClick) {
            PicassoHelper.loadImage(trip.getTripBannerUrl(), mBinding.itemTripImage);
            mBinding.itemTripTitle.setText(trip.getTripName());
            mBinding.itemTripDistance.setText(trip.getTripDistance());
            mBinding.itemTripTime.setText(trip.getTripDuration());
            mBinding.itemTripStart.setText(trip.getPlaceStart().getPlaceName());
            mBinding.itemTripEnd.setText(trip.getPlaceEnd().getPlaceName());
            mBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    adapterItemClick.itemCLick(trip);
                }
            });
        }
    }

    interface TripAdapterItemClick {
        void itemCLick(Trip trip);
    }
}
