package svp.com.dontmissplaces;


import android.app.Application;

import com.svp.infrastructure.mvpvs.presenter.IPresenter;
import com.svp.infrastructure.mvpvs.PresenterContainer;
import com.svp.infrastructure.mvpvs.ViewStateContainer;
import com.svp.infrastructure.mvpvs.viewstate.IViewState;

import svp.com.dontmissplaces.presenters.MapsPresenter;

public class App extends Application {
    @Override
    public void onCreate() {
        PresenterContainer.Register(MapsActivity.class, new PresenterContainer.IPresenterCreator() {
            @Override
            public IPresenter create() {
                return new MapsPresenter();
            }
        });

        ViewStateContainer.Register(MapsActivity.class, new ViewStateContainer.IViewStateCreator<MapsActivity>() {
            @Override
            public IViewState create(MapsActivity view) {
                return new MapsActivity.ViewState(view);
            }
        });

        super.onCreate();
    }
}
