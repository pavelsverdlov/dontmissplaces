package svp.com.dontmissplaces;


import android.app.Application;

import com.svp.infrastructure.mvpvs.presenter.IPresenter;
import com.svp.infrastructure.mvpvs.PresenterContainer;
import com.svp.infrastructure.mvpvs.ViewStateContainer;
import com.svp.infrastructure.mvpvs.viewstate.IViewState;

import svp.com.dontmissplaces.db.Repository;
import svp.com.dontmissplaces.presenters.MainMenuPresenter;
import svp.com.dontmissplaces.presenters.MapsPresenter;
import svp.com.dontmissplaces.ui.MapView;

public class App extends Application {
    @Override
    public void onCreate() {
        Thread.setDefaultUncaughtExceptionHandler(new svp.com.dontmissplaces.ui.UncaughtExceptionHandler());

        final Repository repository = new Repository(this);
        //map view
        PresenterContainer.Register(MapView.class, new PresenterContainer.IPresenterCreator() {
            @Override
            public IPresenter create() {
                return new MapsPresenter(repository);
            }
        });
        ViewStateContainer.Register(MapView.class, new ViewStateContainer.IViewStateCreator<MapView>() {
            @Override
            public IViewState create(MapView view) {
                return new MapView.ViewState(view);
            }
        });

        //Main menu
        PresenterContainer.Register(MainMenuActivity.class, new PresenterContainer.IPresenterCreator() {
            @Override
            public IPresenter create() {
                return new MainMenuPresenter(repository);
            }
        });
        ViewStateContainer.Register(MainMenuActivity.class, new ViewStateContainer.IViewStateCreator<MainMenuActivity>() {
            @Override
            public IViewState create(MainMenuActivity view) {
                return new MainMenuActivity.ViewState(view);
            }
        });

        super.onCreate();
    }
}
