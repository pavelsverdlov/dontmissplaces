package com.svp.infrastructure.mvpvs.view;

import android.app.Activity;
import android.support.v4.app.FragmentActivity;

import com.svp.infrastructure.mvpvs.presenter.Presenter;
import com.svp.infrastructure.mvpvs.PresenterContainer;

/**
 * Created by Pasha on 3/19/2016.
 */
public class FragmentActivityView<P extends Presenter> extends FragmentActivity implements IActivityView {
    PresenterContainer prContainer;

    public FragmentActivityView(){
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
