package svp.com.dontmissstation.presenters;

import android.content.Intent;
import android.util.Log;

import com.svp.infrastructure.mvpvs.commutate.ActivityOperationItem;
import com.svp.infrastructure.mvpvs.commutate.CommutativePresenter;

import svp.app.map.MapViewTypes;
import svp.app.map.android.gps.GPSService;
import svp.app.map.android.gps.GPSServiceProvider;
import svp.app.map.android.gps.IGPSProvider;
import svp.com.dontmissstation.MainActivity;
import svp.com.dontmissstation.db.Repository;

public class MainPresenter extends CommutativePreferencePresenter<MainActivity,MainActivity.ViewState> {

    private static final String TAG = "";
    public IGPSProvider gps;

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
        super.onAttachedView(view,intent);
        try {
            GPSServiceProvider gpsp = new GPSServiceProvider(view, GPSService.class);
            if(!state.getPermissions().checkPermissionFineLocation()) {
                throw new Exception("No permissions");
            }
            gpsp.startup(new Runnable() {
                @Override
                public void run() {
                    Log.d(TAG,"GPS service startup");
                }
            });
            gps = gpsp;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Settings
     * */
    public MapViewTypes getMapViewType() {
        return userSettings.getMapProvider();
    }
}
