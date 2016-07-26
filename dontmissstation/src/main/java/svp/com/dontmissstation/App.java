package svp.com.dontmissstation;

import android.app.Application;

import svp.com.dontmissstation.db.Repository;

/**
 * Created by Pasha on 7/26/2016.
 */
public class App extends Application {
    private final Repository repository;

    public App(){
//        Thread.setDefaultUncaughtExceptionHandler(new svp.com.dontmissplaces.ui.UncaughtExceptionHandler(this));
        repository = new Repository(this);
    }

    @Override
    public void onCreate() {
        this.deleteDatabase(Repository.dbname);


    }
}
