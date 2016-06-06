package com.svp.infrastructure.mvpvs.viewstate;

import android.app.Activity;

import com.svp.infrastructure.mvpvs.view.IActivityView;

public interface IViewState {
    void refresh(IActivityView view);
    Activity getActivity();
}
