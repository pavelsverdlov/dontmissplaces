package svp.com.dontmissplaces;


import android.app.Application;

import com.svp.infrastructure.mvpvs.presenter.IPresenter;
import com.svp.infrastructure.mvpvs.PresenterContainer;
import com.svp.infrastructure.mvpvs.ViewStateContainer;
import com.svp.infrastructure.mvpvs.viewstate.IViewState;

import svp.com.dontmissplaces.db.Repository;
import svp.com.dontmissplaces.presenters.MapsPresenter;

public class App extends Application {
    @Override
    public void onCreate() {
        final Repository repository = new Repository(this);

        PresenterContainer.Register(MapsActivity.class, new PresenterContainer.IPresenterCreator() {
            @Override
            public IPresenter create() {
                return new MapsPresenter(repository);
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
