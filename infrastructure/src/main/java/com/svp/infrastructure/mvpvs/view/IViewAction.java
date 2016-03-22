package com.svp.infrastructure.mvpvs.view;


import android.app.Activity;

public interface IViewAction<V extends Activity & IActivityView> {
    void execute(V view);
}
