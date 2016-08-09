package svp.com.dontmissplaces.model.gps;

import android.app.Activity;
import android.util.Log;

import com.svp.infrastructure.ActivityPermissions;

import svp.app.map.android.gps.GPSService;
import svp.app.map.android.gps.GPSServiceProvider;
import svp.app.map.android.gps.IGPSLocationListener;
import svp.app.map.android.gps.IGPSProvider;
import svp.com.dontmissplaces.presenters.MainMenuPresenter;

public final class GPSProvider {

    private static final String TAG = "GPSProvider";
    private static GPSServiceProvider<svp.app.map.android.gps.GPSService> gpsService;

    public static IGPSProvider create(Activity activity, ActivityPermissions permissions, IGPSLocationListener listener) throws Exception {
        if(gpsService == null){
            gpsService = new GPSServiceProvider(activity, GPSService.class);
            if(!permissions.checkPermissionFineLocation()) {
                throw new Exception("No permissions");
            }
            gpsService.startup(new Runnable() {
                @Override
                public void run() {
                    Log.d(TAG,"GPS service startup");
                }
            });
        }
        gpsService.addOnLocationChangeListener(listener);
        return gpsService;
    }
}
