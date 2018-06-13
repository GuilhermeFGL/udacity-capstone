package com.guilhermefgl.rolling.view.breakpoint;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.guilhermefgl.rolling.R;
import com.guilhermefgl.rolling.databinding.ItemBreakPointBinding;
import com.guilhermefgl.rolling.model.Place;

import java.util.List;

public class BreakPointAdapter extends RecyclerView.Adapter<BreakPointAdapter.ViewHolder> {

    @Nullable
    private List<Place> mBreakPoints;

    public BreakPointAdapter() { }

    public BreakPointAdapter(@NonNull List<Place> placesPoints) {
        mBreakPoints = placesPoints;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemBreakPointBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()), R.layout.item_break_point, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(mBreakPoints.get(position));
    }

    @Override
    public int getItemCount() {
        return mBreakPoints != null ? mBreakPoints.size() : 0;
    }

    public void setBreakPoints(List<Place> breakPoints) {
        this.mBreakPoints = breakPoints;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @NonNull
        private final ItemBreakPointBinding mBinding;

        ViewHolder(@NonNull ItemBreakPointBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        public void bind(Place place) {
            mBinding.itemBreakPointText.setText(place.getPlaceName());
        }
    }
}
