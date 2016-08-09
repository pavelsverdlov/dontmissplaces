// IGPSService.aidl
package svp.app.map.android.gps;

import svp.app.map.android.gps.IGPSLocationListener;

interface IGPSService {
    Location getLastLocation();
    void addLocationListener(in IGPSLocationListener listener);
}
