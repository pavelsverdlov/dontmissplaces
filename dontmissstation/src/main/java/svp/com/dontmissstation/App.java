package svp.com.dontmissstation;

import android.app.Application;

import com.svp.infrastructure.mvpvs.PresenterContainer;
import com.svp.infrastructure.mvpvs.Registrator;
import com.svp.infrastructure.mvpvs.ViewStateContainer;
import com.svp.infrastructure.mvpvs.presenter.IPresenter;
import com.svp.infrastructure.mvpvs.viewstate.IViewState;

import svp.com.dontmissstation.db.Repository;
import svp.com.dontmissstation.presenters.*;
import svp.com.dontmissstation.ui.UncaughtExceptionHandler;
import svp.com.dontmissstation.ui.activities.*;

public class App extends Application {
    private final Repository repository;

    public App(){
        Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler(this));
        repository = new Repository(this);
    }

    @Override
    public void onCreate() {
        this.deleteDatabase(Repository.dbname);

        ActivityCommutator.register(ActivityOperationResult.Main, MainActivity.class);
        ActivityCommutator.register(ActivityOperationResult.EditNewSubway, EditSubwayScrollingActivity.class);
        ActivityCommutator.register(ActivityOperationResult.EditSubwayLine, EditSubwayLineScrollingActivity.class);
        ActivityCommutator.register(ActivityOperationResult.EditSubwayStation, EditSubwayStationActivity.class);
        ActivityCommutator.register(ActivityOperationResult.ListSubways, ListSubwaysActivity.class);
        ActivityCommutator.register(ActivityOperationResult.PickOnMap, PickOnMapActivity.class);
        ActivityCommutator.register(ActivityOperationResult.RouteSelection, RouteScrollingActivity.class);
        ActivityCommutator.register(ActivityOperationResult.StationList, StationListActivity.class);
        ActivityCommutator.register(ActivityOperationResult.SearchNewRoute, SearchNewRouteActivity.class);

        Registrator.register(MainActivity.class,
                new PresenterContainer.IPresenterCreator() {
                    @Override
                    public IPresenter create() {
                        return new MainPresenter(repository);
                    }
                },
                new ViewStateContainer.IViewStateCreator<MainActivity>() {
                    @Override
                    public IViewState create(MainActivity view) {
                        return new MainActivity.ViewState(view);
                    }
                });

        Registrator.register(EditSubwayScrollingActivity.class,
                new PresenterContainer.IPresenterCreator() {
                    @Override
                    public IPresenter create() {
                        return new EditSubwayPresenter(repository);
                    }
                },
                new ViewStateContainer.IViewStateCreator<EditSubwayScrollingActivity>() {
                    @Override
                    public IViewState create(EditSubwayScrollingActivity view) {
                        return new EditSubwayScrollingActivity.ViewState(view);
                    }
                });

        Registrator.register(EditSubwayLineScrollingActivity.class,
                new PresenterContainer.IPresenterCreator() {
                    @Override
                    public IPresenter create() {
                        return new EditSubwayLinePresenter(repository);
                    }
                },
                new ViewStateContainer.IViewStateCreator<EditSubwayLineScrollingActivity>() {
                    @Override
                    public IViewState create(EditSubwayLineScrollingActivity view) {
                        return new EditSubwayLineScrollingActivity.ViewState(view);
                    }
                });

        Registrator.register(EditSubwayStationActivity.class,
                new PresenterContainer.IPresenterCreator() {
                    @Override
                    public IPresenter create() {
                        return new EditSubwayStationPresenter(repository);
                    }
                },
                new ViewStateContainer.IViewStateCreator<EditSubwayStationActivity>() {
                    @Override
                    public IViewState create(EditSubwayStationActivity view) {
                        return new EditSubwayStationActivity.ViewState(view);
                    }
                });

        Registrator.register(ListSubwaysActivity.class,
                new PresenterContainer.IPresenterCreator() {
                    @Override
                    public IPresenter create() {
                        return new ListSubwaysPresenter(repository);
                    }
                },
                new ViewStateContainer.IViewStateCreator<ListSubwaysActivity>() {
                    @Override
                    public IViewState create(ListSubwaysActivity view) {
                        return new ListSubwaysActivity.ViewState(view);
                    }
                });

        Registrator.register(PickOnMapActivity.class,
                new PresenterContainer.IPresenterCreator() {
                    @Override
                    public IPresenter create() {
                        return new PickOnMapPresenter(repository);
                    }
                },
                new ViewStateContainer.IViewStateCreator<PickOnMapActivity>() {
                    @Override
                    public IViewState create(PickOnMapActivity view) {
                        return new PickOnMapActivity.ViewState(view);
                    }
                });

        Registrator.register(RouteScrollingActivity.class,
                new PresenterContainer.IPresenterCreator() {
                    @Override
                    public IPresenter create() {
                        return new RouteScrollingPresenter(repository);
                    }
                },
                new ViewStateContainer.IViewStateCreator<RouteScrollingActivity>() {
                    @Override
                    public IViewState create(RouteScrollingActivity view) {
                        return new RouteScrollingActivity.ViewState(view);
                    }
                });
        Registrator.register(StationListActivity.class,
                new PresenterContainer.IPresenterCreator() {
                    @Override
                    public IPresenter create() {
                        return new StationListPresenter(repository);
                    }
                },
                new ViewStateContainer.IViewStateCreator<StationListActivity>() {
                    @Override
                    public IViewState create(StationListActivity view) {
                        return new StationListActivity.ViewState(view);
                    }
                });
        Registrator.register(SearchNewRouteActivity.class,
                new PresenterContainer.IPresenterCreator() {
                    @Override
                    public IPresenter create() {
                        return new SearchNewRoutePresenter(repository);
                    }
                },
                new ViewStateContainer.IViewStateCreator<SearchNewRouteActivity>() {
                    @Override
                    public IViewState create(SearchNewRouteActivity view) {
                        return new SearchNewRouteActivity.ViewState(view);
                    }
                });

    }
}

