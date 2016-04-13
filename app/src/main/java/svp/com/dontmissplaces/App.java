package svp.com.dontmissplaces;


import android.app.Application;

import com.svp.infrastructure.mvpvs.presenter.IPresenter;
import com.svp.infrastructure.mvpvs.PresenterContainer;
import com.svp.infrastructure.mvpvs.ViewStateContainer;
import com.svp.infrastructure.mvpvs.view.IActivityView;
import com.svp.infrastructure.mvpvs.viewstate.IViewState;

import svp.com.dontmissplaces.db.Repository;
import svp.com.dontmissplaces.presenters.HistoryTracksPresenter;
import svp.com.dontmissplaces.presenters.MainMenuPresenter;
import svp.com.dontmissplaces.presenters.MapsPresenter;
import svp.com.dontmissplaces.ui.MapView;
import svp.com.dontmissplaces.ui.activities.HistoryTracksActivity;

public class App extends Application {
    private final Repository repository;

    public App(){
        Thread.setDefaultUncaughtExceptionHandler(new svp.com.dontmissplaces.ui.UncaughtExceptionHandler());
        repository = new Repository(this);
    }

    @Override
    public void onCreate() {
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

        //History Tracks
        PresenterContainer.Register(HistoryTracksActivity.class, new PresenterContainer.IPresenterCreator() {
            @Override
            public IPresenter create() {
                return new HistoryTracksPresenter(repository);
            }
        });
        ViewStateContainer.Register(HistoryTracksActivity.class, new ViewStateContainer.IViewStateCreator<HistoryTracksActivity>() {
            @Override
            public IViewState create(HistoryTracksActivity view) {
                return new HistoryTracksActivity.ViewState(view);
            }
        });

        Register(HistoryTracksActivity.class,
                new PresenterContainer.IPresenterCreator() {
                    @Override
                    public IPresenter create() {
                        return new HistoryTracksPresenter(repository);
                    }
                },
                new ViewStateContainer.IViewStateCreator<HistoryTracksActivity>() {
                    @Override
                    public IViewState create(HistoryTracksActivity view) {
                        return new HistoryTracksActivity.ViewState(view);
                    }
                });
        super.onCreate();
    }

    private static <T extends IActivityView> void Register(Class<?> _class,
                                                           PresenterContainer.IPresenterCreator pcreator,
                                                           ViewStateContainer.IViewStateCreator<T> stateCreator){
        PresenterContainer.Register(_class, pcreator);
        ViewStateContainer.Register(_class, stateCreator);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        repository.close();
    }
    @Override
    public void onTerminate(){
        super.onTerminate();
        repository.close();
    }
}
