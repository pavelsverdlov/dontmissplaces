package com.svp.infrastructure.mvpvs.presenter;

import android.app.Activity;

import com.svp.infrastructure.mvpvs.view.IActivityView;
import com.svp.infrastructure.mvpvs.ViewStateContainer;
import com.svp.infrastructure.mvpvs.viewstate.IViewState;

import java.util.UUID;

public abstract class Presenter<V extends IActivityView, VS extends IViewState>
        implements IPresenter{
    public static final UUID id = UUID.randomUUID();
    @Override
    public UUID getId() {
        return id;
    }
    private ViewStateContainer vsContainer;
    protected VS state;

    public Presenter(){
        vsContainer = new ViewStateContainer();
    }

    public final void attachView(V view) {
        state = vsContainer.addView(view);
        onAttachedView(view);
    }
    public final void detachView(V view) {
        //vsContainer.removeView(view);
        onDetachedView(view);
    }

    protected void onAttachedView(V view){}
    protected void onDetachedView(V view){}


}
