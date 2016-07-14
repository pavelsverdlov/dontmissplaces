package svp.com.dontmissplaces.model.gps;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import java.util.HashMap;

public class GPSService extends Service {
    private static final String TAG = "GPSService";
    private static final int LOCATION_INTERVAL = 50000;
    private static final float LOCATION_DISTANCE = 10f;

    private LocationManager locationManager;
    private final HashMap<String, LocationListener> listeners;
    private final LocationFilter filter;

    private boolean isNetworkProvider;
    private boolean isGPSProvider;

    public GPSService() {
        filter = new LocationFilter();
        listeners = new HashMap<>();
        listeners.put(LocationManager.GPS_PROVIDER, new LocationListener(LocationManager.GPS_PROVIDER, filter));
        listeners.put(LocationManager.NETWORK_PROVIDER, new LocationListener(LocationManager.NETWORK_PROVIDER, filter));
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    private IBinder mBinder = new IGPSService.Stub() {
        @Override
        public Location getLastLocation() throws RemoteException {
            return filter.getPrevLocation();
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        if (locationManager == null) {
            locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
            locationManager.addGpsStatusListener(new GpsStatus.Listener() {
                @Override
                public void onGpsStatusChanged(int event) {
                    Log.d(TAG,"onGpsStatusChanged: " + event);
                }
            });
        }
        isNetworkProvider = requestLocationUpdates(LocationManager.NETWORK_PROVIDER);
        isGPSProvider = requestLocationUpdates(LocationManager.GPS_PROVIDER);

        Log.d(TAG, "onCreate: GPSProvider = " + isGPSProvider);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // We want this service to continue running until it is explicitly
        // stopped, so return sticky.
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy: ");
        super.onDestroy();
        locationManager.removeUpdates(listeners.get(LocationManager.GPS_PROVIDER));
        locationManager.removeUpdates(listeners.get(LocationManager.NETWORK_PROVIDER));
    }

    private boolean requestLocationUpdates(String provider){
        try {
            if(!locationManager.isProviderEnabled(provider)){
                Log.d(TAG, "requestLocationUpdates: " + provider + " disabled.");
                return false;
            }

            Location last = locationManager.getLastKnownLocation(provider);
            Log.d(TAG, "LastKnownLocation: " + (last == null ? "NULL" : last.toString()));
            filter.addLocation(last);

            locationManager.requestLocationUpdates(provider, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    listeners.get(provider));

            Log.d(TAG, provider + " true");

            return true;
        } catch (java.lang.SecurityException ex) {
            Log.i(TAG, "requestLocationUpdates: fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "requestLocationUpdates: gps provider does not exist " + ex.getMessage());
        }
        return false;
    }
    private boolean isNetworkConnected() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connMgr.getActiveNetworkInfo();

        return (info != null && info.isConnected());
    }
}
