package com.guilhermefgl.rolling.view;

import android.support.v4.app.Fragment;

public abstract class BaseFragment extends Fragment {

    protected boolean isActive() {
        return isAdded() && isActive();
    }

}
