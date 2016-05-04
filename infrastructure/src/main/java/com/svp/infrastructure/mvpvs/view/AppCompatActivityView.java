package com.svp.infrastructure.mvpvs.view;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;

import com.svp.infrastructure.mvpvs.PresenterContainer;
import com.svp.infrastructure.mvpvs.presenter.Presenter;

import java.util.UUID;

public class AppCompatActivityView<P extends Presenter> extends AppCompatActivity implements IActivityView {

   // public static final UUID id = UUID.randomUUID();

    PresenterContainer prContainer;

    public AppCompatActivityView(){
        prContainer = new PresenterContainer();
    }

    protected final P getPresenter(){
        return prContainer.get(this.getClass());
    }

    @Override
    public final void showError(String stringErrorWrapper) {

    }

    @Override
    public final <V extends IActivityView> void executeAction(IViewAction<V> action) {
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
