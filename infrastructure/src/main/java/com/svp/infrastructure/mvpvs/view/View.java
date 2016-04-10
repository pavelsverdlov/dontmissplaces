package com.svp.infrastructure.mvpvs.view;

import android.app.Activity;
import android.support.v4.app.FragmentActivity;

import com.svp.infrastructure.mvpvs.PresenterContainer;
import com.svp.infrastructure.mvpvs.presenter.Presenter;

/**
 * Created by Pasha on 4/9/2016.
 */
public class View<P extends Presenter> implements IActivityView {
    PresenterContainer prContainer;

    public View(){
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

    public void onStart(){
        getPresenter().attachView(this);
    }

    public void onStop(){
        getPresenter().detachView(this);
    }


}
