package com.svp.infrastructure.mvpvs.viewstate;

import android.app.Activity;
import android.support.design.widget.Snackbar;

import com.svp.infrastructure.mvpvs.view.IActivityView;

public interface IViewState {
    void saveState();
    void refresh(IActivityView view);
    Activity getActivity();
    Snackbar getSnackbar(CharSequence text);
}
