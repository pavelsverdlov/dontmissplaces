package svp.com.dontmissstation.presenters;

import android.content.Intent;

import com.svp.infrastructure.mvpvs.commutate.ActivityOperationItem;
import com.svp.infrastructure.mvpvs.commutate.CommutativePresenter;

import svp.app.map.MapViewTypes;
import svp.com.dontmissstation.MainActivity;
import svp.com.dontmissstation.db.Repository;

public class MainPresenter extends CommutativePreferencePresenter<MainActivity,MainActivity.ViewState> {

    public MainPresenter(Repository repository) {

    }

    @Override
    protected ActivityOperationItem getOperation(int code) {
        return null;
    }

    @Override
    protected void incomingResultFrom(ActivityOperationItem from, Intent data) {

    }

    @Override
    protected void onAttachedView(MainActivity view, Intent intent) {

    }

    /**
     * Settings
     * */
    public MapViewTypes getMapViewType() {
        return userSettings.getMapProvider();
    }
}
