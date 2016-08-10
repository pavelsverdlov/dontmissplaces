package svp.com.dontmissplaces;


import android.app.Application;
import android.os.Bundle;

import com.svp.infrastructure.mvpvs.Registrator;
import com.svp.infrastructure.mvpvs.bundle.BundleContainer;
import com.svp.infrastructure.mvpvs.bundle.IBundleProvider;
import com.svp.infrastructure.mvpvs.presenter.IPresenter;
import com.svp.infrastructure.mvpvs.PresenterContainer;
import com.svp.infrastructure.mvpvs.ViewStateContainer;
import com.svp.infrastructure.mvpvs.viewstate.IViewState;

import svp.com.dontmissplaces.db.Repository;
import svp.com.dontmissplaces.presenters.*;
import svp.com.dontmissplaces.ui.ActivityCommutator;
import svp.com.dontmissplaces.ui.BaseBundleProvider;
import svp.com.dontmissplaces.ui.map.GoogleMapView;

import svp.com.dontmissplaces.ui.activities.*;
import svp.com.dontmissplaces.ui.map.DNMPOsmdroidMapView;

public class App extends Application {
    private final Repository repository;

    public App(){
        Thread.setDefaultUncaughtExceptionHandler(new svp.com.dontmissplaces.ui.UncaughtExceptionHandler(this));
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

        Registrator.register(DNMPOsmdroidMapView.class,
                new PresenterContainer.IPresenterCreator() {
                    @Override
                    public IPresenter create() {
                        return new MapsPresenter(repository);
                    }
                },
                new ViewStateContainer.IViewStateCreator<DNMPOsmdroidMapView>() {
                    @Override
                    public IViewState create(DNMPOsmdroidMapView view) {
                        return new DNMPOsmdroidMapView.ViewState(view);
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

        Registrator.register(HistoryTracksActivity.class,
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

        Registrator.register(SaveTrackActivity.class,
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

        Registrator.register(SettingsActivity.class,
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

        Registrator.register(SearchPlacesActivity.class,
                new PresenterContainer.IPresenterCreator() {
                    @Override
                    public IPresenter create() {
                        return new SearchPlacesPresenter(repository);
                    }
                },
                new ViewStateContainer.IViewStateCreator<SearchPlacesActivity>() {
                    @Override
                    public IViewState create(SearchPlacesActivity view) {
                        return new SearchPlacesActivity.ViewState(view);
                    }
                });

        registerBundleProviders();
        ActivityCommutator.register();

        super.onCreate();
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
