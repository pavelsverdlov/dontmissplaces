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
import svp.com.dontmissstation.ui.activities.ActivityCommutator;
import svp.com.dontmissstation.ui.activities.ActivityOperationResult;

public class App extends Application {
    private final Repository repository;

    public App(){
        Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler(this));
        repository = new Repository(this);
    }

    @Override
    public void onCreate() {
        this.deleteDatabase(Repository.dbname);

        ActivityCommutator.register(ActivityOperationResult.Main,MainActivity.class);

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

    }
}
