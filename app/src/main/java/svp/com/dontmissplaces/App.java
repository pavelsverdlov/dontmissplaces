package svp.com.dontmissplaces;


import android.app.Application;
import android.os.Bundle;

import com.svp.infrastructure.mvpvs.bundle.BundleContainer;
import com.svp.infrastructure.mvpvs.bundle.IBundleProvider;
import com.svp.infrastructure.mvpvs.presenter.IPresenter;
import com.svp.infrastructure.mvpvs.PresenterContainer;
import com.svp.infrastructure.mvpvs.ViewStateContainer;
import com.svp.infrastructure.mvpvs.view.IActivityView;
import com.svp.infrastructure.mvpvs.viewstate.IViewState;

import svp.com.dontmissplaces.db.Repository;
import svp.com.dontmissplaces.presenters.*;
import svp.com.dontmissplaces.ui.BaseBundleProvider;
import svp.com.dontmissplaces.ui.map.GoogleMapView;

import svp.com.dontmissplaces.ui.activities.*;
import svp.com.dontmissplaces.ui.map.OsmdroidMapView;

public class App extends Application {
    private final Repository repository;

    public App(){
        Thread.setDefaultUncaughtExceptionHandler(new svp.com.dontmissplaces.ui.UncaughtExceptionHandler());
        repository = new Repository(this);
    }

    @Override
    public void onCreate() {
        this.deleteDatabase(Repository.dbname);
        //map view
        PresenterContainer.register(GoogleMapView.class, new PresenterContainer.IPresenterCreator() {
            @Override
            public IPresenter create() {
                return new MapsPresenter(repository);
            }
        });
        ViewStateContainer.Register(GoogleMapView.class, new ViewStateContainer.IViewStateCreator<GoogleMapView>() {
            @Override
            public IViewState create(GoogleMapView view) {
                return new GoogleMapView.ViewState(view);
            }
        });

        register(OsmdroidMapView.class,
                new PresenterContainer.IPresenterCreator() {
                    @Override
                    public IPresenter create() {
                        return new MapsPresenter(repository);
                    }
                },
                new ViewStateContainer.IViewStateCreator<OsmdroidMapView>() {
                    @Override
                    public IViewState create(OsmdroidMapView view) {
                        return new OsmdroidMapView.ViewState(view);
                    }
                });

        //Main menu
        PresenterContainer.register(MainMenuActivity.class, new PresenterContainer.IPresenterCreator() {
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
        PresenterContainer.register(HistoryTracksActivity.class, new PresenterContainer.IPresenterCreator() {
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

        register(HistoryTracksActivity.class,
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

        register(SaveTrackActivity.class,
                new PresenterContainer.IPresenterCreator() {
                    @Override
                    public IPresenter create() {
                        return new SaveTrackPresenter(repository);
                    }
                },
                new ViewStateContainer.IViewStateCreator<SaveTrackActivity>() {
                    @Override
                    public IViewState create(SaveTrackActivity view) {
                        return new SaveTrackActivity.ViewState(view);
                    }
                });

        register(SettingsActivity.class,
                new PresenterContainer.IPresenterCreator() {
                    @Override
                    public IPresenter create() {
                        return new SettingsPresenter();
                    }
                },
                new ViewStateContainer.IViewStateCreator<SettingsActivity>() {
                    @Override
                    public IViewState create(SettingsActivity view) {
                        return new SettingsActivity.ViewState(view);
                    }
                });

        registerBundleProviders();

        super.onCreate();
    }

    private static <T extends IActivityView> void register(Class<?> _class,
                                                           PresenterContainer.IPresenterCreator pcreator,
                                                           ViewStateContainer.IViewStateCreator<T> stateCreator){
        PresenterContainer.register(_class, pcreator);
        ViewStateContainer.Register(_class, stateCreator);
    }

    //refactor because this is too complicated
    private void registerBundleProviders(){
        BundleContainer.register(SaveTrackActivity.class, new BundleContainer.IBundleCreator() {
            @Override
            public IBundleProvider create(Bundle bundle) {
                return new BaseBundleProvider(bundle);
            }
        });
        BundleContainer.register(HistoryTracksActivity.class, new BundleContainer.IBundleCreator() {
            @Override
            public IBundleProvider create(Bundle bundle) {
                return new BaseBundleProvider(bundle);
            }
        });
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        repository.close();
    }
    @Override
    public void onTerminate(){
        this.deleteDatabase(Repository.dbname);
        super.onTerminate();
        repository.close();
    }
}
