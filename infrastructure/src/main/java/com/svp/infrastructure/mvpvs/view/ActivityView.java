package com.svp.infrastructure.mvpvs.view;

import android.app.Activity;

import com.svp.infrastructure.mvpvs.presenter.Presenter;

import java.util.UUID;

/**
 * Created by Pasha on 3/19/2016.
 */
public class ActivityView<P extends Presenter> extends Activity implements IActivityView {
    protected P presenter;
    public static final UUID id = UUID.randomUUID();

    public void setPresenter(P presenter){
        this.presenter = presenter;
    }

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public void showError(String stringErrorWrapper) {

    }

    @Override
    public <V extends Activity & IActivityView> void executeAction(IViewAction<V> action) {
        action.execute((V) this);
    }

    @Override
    protected void onStart(){
        presenter.attachView(this);
        super.onStart();
    }
    @Override
    protected void onStop(){
        presenter.detachView(this);
        super.onStop();
    }
}
