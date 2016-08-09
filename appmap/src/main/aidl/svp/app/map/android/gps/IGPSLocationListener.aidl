// IGPSLocationListener.aidl
package svp.app.map.android.gps;

// Declare any non-default types here with import statements

interface IGPSLocationListener {
    void onLocationChanged(in Location location);
}
