package com.guilhermefgl.rolling.view.breakpoint;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.guilhermefgl.rolling.R;
import com.guilhermefgl.rolling.databinding.ItemAddBreakPointBinding;
import com.guilhermefgl.rolling.databinding.ItemBreakPointBinding;
import com.guilhermefgl.rolling.model.Place;

import java.util.List;

public class BreakPointAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_ADD = 1001;
    private static final int VIEW_TYPE_ITEM = 1002;

    @Nullable
    private List<Place> mBreakPoints;
    @NonNull
    private BreakPointAdapterItemClick mAdapterItemClick;

    public BreakPointAdapter(@Nullable List<Place> placesPoints, @NonNull BreakPointAdapterItemClick adapterItemClick) {
        mBreakPoints = placesPoints;
        mAdapterItemClick = adapterItemClick;
    }

    @Override
    public int getItemViewType(int position) {
        return  mBreakPoints != null && mBreakPoints.get(position) != null ?
                VIEW_TYPE_ITEM : VIEW_TYPE_ADD;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            ItemBreakPointBinding binding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.getContext()), R.layout.item_break_point, parent, false);
            return new ItemViewHolder(binding);
        } else {
            ItemAddBreakPointBinding binding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.getContext()), R.layout.item_add_break_point, parent, false);
            return new AddViewHolder(binding);
        }
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == VIEW_TYPE_ITEM) {
            ((ItemViewHolder) holder).bind(mBreakPoints.get(position), mAdapterItemClick);
        } else {
            ((AddViewHolder) holder).bind(mAdapterItemClick);
        }
    }

    @Override
    public int getItemCount() {
        return mBreakPoints != null ? mBreakPoints.size() : 0;
    }

    public void setBreakPoints(List<Place> breakPoints) {
        this.mBreakPoints = breakPoints;
        notifyDataSetChanged();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        @NonNull
        private final ItemBreakPointBinding mBinding;

        ItemViewHolder(@NonNull ItemBreakPointBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        public void bind(final Place place, final BreakPointAdapterItemClick mAdapterItemClick) {
            mBinding.itemBreakPointText.setText(place.getPlaceName());
            mBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mAdapterItemClick.itemCLick(place);
                }
            });
        }
    }

    class AddViewHolder extends RecyclerView.ViewHolder {

        @NonNull
        private final ItemAddBreakPointBinding mBinding;

        AddViewHolder(@NonNull ItemAddBreakPointBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        public void bind(final BreakPointAdapterItemClick adapterItemClick) {
            mBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    adapterItemClick.itemCLick(null);
                }
            });
        }
    }

    public interface BreakPointAdapterItemClick {
        void itemCLick(Place place);
    }
}
