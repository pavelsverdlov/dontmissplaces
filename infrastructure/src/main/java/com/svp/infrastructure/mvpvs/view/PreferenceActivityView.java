package com.svp.infrastructure.mvpvs.view;

import android.preference.PreferenceActivity;

import com.svp.infrastructure.mvpvs.PresenterContainer;
import com.svp.infrastructure.mvpvs.presenter.Presenter;

public class PreferenceActivityView<P extends Presenter>  extends PreferenceActivity implements IActivityView  {
    private final PresenterContainer prContainer;

    public PreferenceActivityView(){
        prContainer = new PresenterContainer();
    }

    protected P getPresenter(){
        return prContainer.get(this.getClass());
    }

    @Override
    public void showError(String stringErrorWrapper) {

    }

    @Override
    public <V extends IActivityView> void executeAction(IViewAction<V> action) {
        action.execute((V) this);
    }

    @Override
    protected void onStart(){
        getPresenter().attachView(this);
        super.onStart();
    }
    @Override
    protected void onStop(){
        getPresenter().detachView(this);
        super.onStop();
    }
}
