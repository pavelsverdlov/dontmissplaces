package com.svp.infrastructure.mvpvs.viewstate;

import android.app.Activity;

import com.svp.infrastructure.mvpvs.view.IActivityView;
import com.svp.infrastructure.mvpvs.view.IViewAction;

public abstract class ActivityViewState<V extends Activity & IActivityView> implements IViewState {
    protected V view;
    protected ActivityViewState(V view){
        this.view = view;
    }
    public void addAction(IViewAction<V> action){
        view.executeAction(action);
    }

    public void refresh(IActivityView view){
        this.view = (V)view;
        restore();
    }

    protected abstract void restore();
}