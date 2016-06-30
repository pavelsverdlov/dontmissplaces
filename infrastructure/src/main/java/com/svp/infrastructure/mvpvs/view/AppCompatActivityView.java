package com.svp.infrastructure.mvpvs.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.svp.infrastructure.mvpvs.bundle.BundleContainer;
import com.svp.infrastructure.mvpvs.bundle.BundleProvider;
import com.svp.infrastructure.mvpvs.bundle.IBundleProvider;
import com.svp.infrastructure.mvpvs.PresenterContainer;
import com.svp.infrastructure.mvpvs.presenter.Presenter;

public class AppCompatActivityView<P extends Presenter> extends AppCompatActivity implements IActivityView {

   // public static final UUID id = UUID.randomUUID();

    private final PresenterContainer prContainer;
   // private final BundleContainer bundleContainer;

    public AppCompatActivityView(){
        prContainer = new PresenterContainer();
      //  bundleContainer = new BundleContainer();
    }

    protected final P getPresenter(){
        return prContainer.get(this.getClass());
    }

    @Override
    public final void showError(String stringErrorWrapper) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        //bundleProvider = bundleContainer.get(this.getClass(),BundleProvider.getBundle(savedInstanceState,this));
        super.onCreate(savedInstanceState);
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

//    protected final <B extends IBundleProvider> B getBundle(){
//        return (B)bundleProvider;
//    }
}
