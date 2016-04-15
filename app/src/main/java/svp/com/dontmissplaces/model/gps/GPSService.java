package svp.com.dontmissplaces.model.gps;

import android.app.Service;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.LocationListener;

/**
 * Created by Pasha on 4/15/2016.
 */
public class GPSService extends Service implements LocationListener {
    /**
     * <code>mAcceptableAccuracy</code> indicates the maximum acceptable accuracy
     * of a waypoint in meters.
     */
    private float mMaxAcceptableAccuracy = 20;
    private int mSatellites = 0;
    /**
     * Should the GPS Status monitor update the notification bar
     */
    private boolean mStatusMonitor;

    /**
     * Listens to GPS status changes
     */
    private GpsStatus.Listener mStatusListener = new GpsStatus.Listener() {
        public synchronized void onGpsStatusChanged(int event) {
            switch (event) {
                case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
                    if (mStatusMonitor) {
                        GpsStatus status = mLocationManager.getGpsStatus(null);
                        mSatellites = 0;
                        Iterable<GpsSatellite> list = status.getSatellites();
                        for (GpsSatellite satellite : list) {
                            if (satellite.usedInFix()) {
                                mSatellites++;
                            }
                        }
                        updateNotification();
                    }
                    break;
                case GpsStatus.GPS_EVENT_STOPPED:
                    break;
                case GpsStatus.GPS_EVENT_STARTED:
                    break;
                default:
                    break;
            }
        }
    };


    public GPSService(){

    }


}
