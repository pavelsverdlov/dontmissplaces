package svp.com.dontmissplaces.model.gps;

import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.location.Location;
import android.location.LocationManager;
import android.os.*;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.*;

public class MyTracksLocationManager {

    private final String TAG = "MyTracksLocationManager";

    private class GoogleSettingsObserver extends ContentObserver {

        public GoogleSettingsObserver(Handler handler) {
            super(handler);
            Log.d(TAG,"GoogleSettingsObserver initialized");
        }

        @Override
        public void onChange(boolean selfChange) {
            isAllowed = GoogleLocationUtils.isAllowed(context);
        }
    }
    private final GoogleApiClient.ConnectionCallbacks connectionCallbacks = new GoogleApiClient.ConnectionCallbacks() {
        @Override
        public void onConnected(Bundle bunlde) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    if (requestLastLocation != null && locationClient.isConnected()) {
                        Location location =LocationServices.FusedLocationApi.getLastLocation(locationClient);
                        requestLastLocation.onLocationChanged(location);//locationClient.getLastLocation());
                        requestLastLocation = null;
                    }
                    if (requestLocationUpdates != null && locationClient.isConnected()) {
                        LocationRequest locationRequest = new LocationRequest().setPriority(
                                LocationRequest.PRIORITY_HIGH_ACCURACY).setInterval(requestLocationUpdatesTime)
                                .setFastestInterval(requestLocationUpdatesTime)
                                .setSmallestDisplacement(requestLocationUpdatesDistance);
//                        locationClient.requestLocationUpdates(
//                                locationRequest, requestLocationUpdates, handler.getLooper());
                        LocationServices.FusedLocationApi.requestLocationUpdates(
                                locationClient, locationRequest,requestLocationUpdates, handler.getLooper());
                    }
                }
            });
        }

        @Override
        public void onConnectionSuspended(int i) {

        }
    };
    private final GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener = new GoogleApiClient.OnConnectionFailedListener() {
        @Override
        public void onConnectionFailed(ConnectionResult connectionResult) {}
    };

    private final Context context;
    private final Handler handler;
//    private final LocationClient locationClient;
    private GoogleApiClient locationClient;
    private final LocationManager locationManager;
    private final ContentResolver contentResolver;
    private final GoogleSettingsObserver observer;

    private boolean isAllowed;
    private LocationListener requestLastLocation;
    private LocationListener requestLocationUpdates;
    private float requestLocationUpdatesDistance;
    private long requestLocationUpdatesTime;

    public MyTracksLocationManager(Context context, Looper looper, boolean enableLocaitonClient) {
        this.context = context;
        this.handler = new Handler(looper);

        if (enableLocaitonClient) {
//            locationClient = new LocationClient(context, connectionCallbacks, onConnectionFailedListener);
            locationClient = new GoogleApiClient.Builder(context)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(connectionCallbacks)
                    .addOnConnectionFailedListener(onConnectionFailedListener)
                    .build();
            locationClient.connect();
        } else {
            locationClient = null;
        }
        Log.d(TAG,"location client enabled=" + enableLocaitonClient);

        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        contentResolver = context.getContentResolver();
        observer = new GoogleSettingsObserver(handler);

        isAllowed = GoogleLocationUtils.isAllowed(context);

        contentResolver.registerContentObserver(
                GoogleLocationUtils.USE_LOCATION_FOR_SERVICES_URI, false, observer);
        Log.d(TAG,"initialized");
    }
    public void close() {
        if (locationClient != null) {
            locationClient.disconnect();
        }
        contentResolver.unregisterContentObserver(observer);
    }
    public boolean isAllowed() {
        return isAllowed;
    }
    public boolean isGpsProviderEnabled() {
        if (!isAllowed()) {
            return false;
        }
        String provider = LocationManager.GPS_PROVIDER;
        if (locationManager.getProvider(provider) == null) {
            return false;
        }
        return locationManager.isProviderEnabled(provider);
    }
    public void requestLastLocation(final LocationListener locationListener) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (!isAllowed()) {
                    requestLastLocation = null;
                    locationListener.onLocationChanged(null);
                } else {
                    requestLastLocation = locationListener;
                    connectionCallbacks.onConnected(null);
                }
            }
        });
    }
    public void requestLocationUpdates(
            final long minTime, final float minDistance, final LocationListener locationListener) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                requestLocationUpdatesTime = minTime;
                requestLocationUpdatesDistance = minDistance;
                requestLocationUpdates = locationListener;
                connectionCallbacks.onConnected(null);
            }
        });
    }
    public void removeLocationUpdates(final LocationListener locationListener) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                requestLocationUpdates = null;
                if (locationClient != null && locationClient.isConnected()) {
//                    locationClient.removeLocationUpdates(locationListener);
                    LocationServices.FusedLocationApi.removeLocationUpdates(locationClient,locationListener);
                }
            }
        });
    }

}
