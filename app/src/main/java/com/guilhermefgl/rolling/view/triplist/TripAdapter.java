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
import com.guilhermefgl.rolling.databinding.ItemAddTripBinding;
import com.guilhermefgl.rolling.helper.PicassoHelper;
import com.guilhermefgl.rolling.model.Trip;

import java.util.List;

public class TripAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_ADD = 1001;
    private static final int VIEW_TYPE_ITEM = 1002;

    @Nullable
    private List<Trip> mTrips;
    @NonNull
    private TripAdapterItemClick mAdapterItemClick;


    TripAdapter(@Nullable List<Trip> trips, @NonNull TripAdapterItemClick adapterItemClick) {
        mTrips = trips;
        mAdapterItemClick = adapterItemClick;
    }

    @Override
    public int getItemViewType(int position) {
        return  mTrips != null && mTrips.get(position) != null ? VIEW_TYPE_ITEM : VIEW_TYPE_ADD;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            ItemTripBinding binding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.getContext()), R.layout.item_trip, parent, false);
            return new ItemViewHolder(binding);
        } else {
            ItemAddTripBinding binding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.getContext()), R.layout.item_add_trip, parent, false);
            return new AddViewHolder(binding);
        }
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == VIEW_TYPE_ITEM) {
            ((ItemViewHolder) holder).bind(mTrips.get(position), mAdapterItemClick);
        } else {
            ((AddViewHolder) holder).bind(mAdapterItemClick);
        }
    }

    @Override
    public int getItemCount() {
        return mTrips != null ? mTrips.size() : 0;
    }

    public void setTripList(List<Trip> trips) {
        mTrips = trips;
        notifyDataSetChanged();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        @NonNull
        private final ItemTripBinding mBinding;

        ItemViewHolder(@NonNull ItemTripBinding binding) {
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
                    adapterItemClick.itemCLick(trip, mBinding.itemTripImage);
                }
            });
        }
    }

    class AddViewHolder extends RecyclerView.ViewHolder {

        @NonNull
        private final ItemAddTripBinding mBinding;

        AddViewHolder(@NonNull ItemAddTripBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        public void bind(final TripAdapterItemClick adapterItemClick) {
            mBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    adapterItemClick.itemCLick(null, null);
                }
            });
        }
    }

    interface TripAdapterItemClick {
        void itemCLick(Trip trip, View transitionImageView);
    }
}
