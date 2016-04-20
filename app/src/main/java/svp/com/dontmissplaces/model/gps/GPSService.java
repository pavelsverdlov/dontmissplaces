package svp.com.dontmissplaces.model.gps;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.HashMap;

public class GPSService extends Service {
    private static final String TAG = "BOOMBOOMTESTGPS";
    private static final int LOCATION_INTERVAL = 1000;
    private static final float LOCATION_DISTANCE = 10f;

    private LocationManager mLocationManager;
    private final HashMap<String,LocationListener> listeners;
    private final LocationFilter filter;

    public GPSService(){
        filter = new LocationFilter();
        listeners = new HashMap<>();
        listeners.put(LocationManager.GPS_PROVIDER,new LocationListener(LocationManager.GPS_PROVIDER, filter));
        listeners.put(LocationManager.NETWORK_PROVIDER,new LocationListener(LocationManager.NETWORK_PROVIDER, filter));
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
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
        String provider = LocationManager.NETWORK_PROVIDER;
        try {
            mLocationManager.requestLocationUpdates(provider, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    listeners.get(provider));
        } catch (java.lang.SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "network provider does not exist, " + ex.getMessage());
        }

        provider = LocationManager.GPS_PROVIDER;
        try {
            mLocationManager.requestLocationUpdates(provider, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    listeners.get(provider));
        } catch (java.lang.SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "gps provider does not exist " + ex.getMessage());
        }
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // We want this service to continue running until it is explicitly
        // stopped, so return sticky.
        return START_STICKY;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
