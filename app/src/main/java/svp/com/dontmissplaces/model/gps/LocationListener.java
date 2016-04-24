package svp.com.dontmissplaces.model.gps;

import android.location.Location;
import android.os.Bundle;
import android.util.Log;

public class LocationListener implements android.location.LocationListener {
    private static final String TAG = "LocationListener";
    private final LocationFilter filter;
    private final Location lastLocation;


    public LocationListener(String provider, LocationFilter filter) {
        lastLocation = new Location(provider);
        this.filter = filter;
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "onLocationChanged: " + location);
        filter.addLocation(location);
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d(TAG, "onProviderDisabled: " + provider);
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d(TAG, "onProviderEnabled: " + provider);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d(TAG, "onStatusChanged: " + provider);
    }
}