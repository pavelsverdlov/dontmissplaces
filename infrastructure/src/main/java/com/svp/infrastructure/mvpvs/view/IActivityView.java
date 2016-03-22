package com.svp.infrastructure.mvpvs.view;

import android.app.Activity;

import java.util.UUID;

public interface IActivityView {
    UUID getId();
    void showError(String stringErrorWrapper);
    <V extends Activity & IActivityView> void executeAction(IViewAction<V> action);
}
