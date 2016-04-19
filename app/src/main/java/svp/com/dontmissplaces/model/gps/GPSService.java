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

import svp.com.opengpstracker.logger.IGPSLoggerServiceRemote;

public class GPSService extends Service {
    private static final String TAG = "BOOMBOOMTESTGPS";
    private static final int LOCATION_INTERVAL = 1000;
    private static final float LOCATION_DISTANCE = 10f;

    LocationListener[] mLocationListeners = new LocationListener[] {
            new LocationListener(LocationManager.GPS_PROVIDER),
            new LocationListener(LocationManager.NETWORK_PROVIDER)
    };

    private LocationManager mLocationManager;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    private IBinder mBinder = new IGPSLoggerServiceRemote.Stub() {
        public int loggingState() throws RemoteException {
            return 0;
        }
        public long startLogging() throws RemoteException {
            //GPSService.this.startLogging();
            return 0;
        }
        public void pauseLogging() throws RemoteException {
            //GPSLoggerService.this.pauseLogging();
        }
        public long resumeLogging() throws RemoteException {
//            GPSLoggerService.this.resumeLogging();
            return 0;
        }

        public void stopLogging() throws RemoteException {
//            GPSLoggerService.this.stopLogging();
        }

        public Uri storeMediaUri(Uri mediaUri) throws RemoteException {
//            GPSLoggerService.this.storeMediaUri(mediaUri);
            return null;
        }

        public boolean isMediaPrepared() throws RemoteException {
//            return GPSLoggerService.this.isMediaPrepared();
            return true;
        }

        public void storeDerivedDataSource(String sourceName) throws RemoteException {
            //GPSLoggerService.this.storeDerivedDataSource(sourceName);
        }

        public Location getLastWaypoint() throws RemoteException {
            //return GPSLoggerService.this.getLastWaypoint();
            return new Location("");
        }

        public float getTrackedDistance() throws RemoteException {
           // return GPSLoggerService.this.getTrackedDistance();
            return 0;
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[1]);
        } catch (java.lang.SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "network provider does not exist, " + ex.getMessage());
        }
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[0]);
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
