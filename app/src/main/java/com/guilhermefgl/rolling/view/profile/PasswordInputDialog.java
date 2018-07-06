package com.guilhermefgl.rolling.view.profile;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.guilhermefgl.rolling.R;
import com.guilhermefgl.rolling.databinding.DialogInputPasswordBinding;

public class PasswordInputDialog extends DialogFragment {

    private DialogInputPasswordBinding mBinding;

    public PasswordInputDialog() { }

    public static PasswordInputDialog newInstance() {
        return new PasswordInputDialog();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(
                inflater, R.layout.dialog_input_password, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mBinding.dialogPasswordActionOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment targetFragment = getTargetFragment();
                if (targetFragment instanceof PasswordDialogCallback) {
                    ((PasswordDialogCallback) targetFragment)
                            .onSubmitPassword(mBinding.dialogPasswordInput.getText().toString());
                }
                dismiss();
            }
        });
        mBinding.dialogPasswordActionCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    interface PasswordDialogCallback {
        void onSubmitPassword(String oldPassword);
    }
}
