package svp.com.dontmissstation.presenters;

import android.content.Intent;
import android.util.Log;

import com.svp.infrastructure.mvpvs.commutate.ActivityOperationItem;

import svp.app.map.MapViewTypes;
import svp.app.map.android.gps.GPSService;
import svp.app.map.android.gps.GPSServiceProvider;
import svp.app.map.android.gps.IGPSProvider;
import svp.com.dontmissstation.MainActivity;
import svp.com.dontmissstation.db.Repository;
import svp.com.dontmissstation.ui.activities.ActivityOperationResult;
import svp.com.dontmissstation.ui.model.SubwayView;

public class MainPresenter extends CommutativePreferencePresenter<MainActivity,MainActivity.ViewState> {

    private static final String TAG = "";
    private final Repository repository;
    public IGPSProvider gps;

    public MainPresenter(Repository repository) {

        this.repository = repository;
    }


    @Override
    protected void incomingResultFrom(ActivityOperationItem from, Intent data) {
        if(ActivityOperationResult.ListSubways.is(from)){

        }
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

            SubwayBundleProvider bundle = new SubwayBundleProvider(intent);
            long id = bundle.getSubwayId();
            SubwayView subway = repository.getSubwayById(id);
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

    public void openAddNewSubway() {
        commutator.goTo(ActivityOperationResult.EditNewSubway);
    }

    public void openListSubwaysActivity() {
        commutator.goTo(ActivityOperationResult.ListSubways);
    }
}
