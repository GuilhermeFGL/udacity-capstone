package com.guilhermefgl.rolling.view.details;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.guilhermefgl.rolling.R;
import com.guilhermefgl.rolling.databinding.ItemPersonBinding;
import com.guilhermefgl.rolling.helper.PicassoHelper;
import com.guilhermefgl.rolling.model.User;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    @Nullable
    private final List<User> mUsers;

    UserAdapter(@Nullable List<User> users) {
        mUsers = users;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemPersonBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()), R.layout.item_person, parent, false);
        return new UserAdapter.ViewHolder(binding);
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(mUsers.get(position));
    }

    @Override
    public int getItemCount() {
        return mUsers != null ? mUsers.size() : 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @NonNull
        private final ItemPersonBinding mBinding;

        ViewHolder(@NonNull ItemPersonBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        void bind(User user) {
            mBinding.itemPersonName.setText(user.getUserName());
            PicassoHelper.loadImage(user.getUserAvatarUrl(), mBinding.itemPersonAvatar);
        }
    }
}
