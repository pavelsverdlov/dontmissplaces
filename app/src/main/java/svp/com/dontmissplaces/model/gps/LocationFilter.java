package svp.com.dontmissplaces.model.gps;

import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Vector;

import svp.app.map.LocationUtils;
import svp.app.map.policy.LocationListenerPolicy;

public class LocationFilter {
    private static final int LOCATION_INTERVAL = 50000;
    private static final float LOCATION_DISTANCE = 10f;
    /**
     * Anything faster than that (in meters per second) will be considered moving.
     */
    public static final double MAX_NO_MOVEMENT_SPEED = 0.224;
    /**
     * <code>MAX_REASONABLE_SPEED</code> is about 324 kilometer per hour or 201
     * mile per hour.
     */
    private static final int MAX_REASONABLE_SPEED = 90;
    /**
     * <code>MAX_REASONABLE_ALTITUDECHANGE</code> between the last few waypoints
     * and a new one the difference should be less then 200 meter.
     */
    private static final int MAX_REASONABLE_ALTITUDECHANGE = 200;

    public static final String TAG = "LocationFilter";
    private final LocationListenerPolicy locationListenerPolicy;
    private float mMaxAcceptableAccuracy = 20;
    private final Vector<Location> mWeakLocations;
    private final Queue<Double> mAltitudes;
    boolean mSpeedSanityCheck;
    private Location previousLocation;
    /**
     * Location is ignored if its accurancy is less than (50m recommended) (10(excellent GPS) - 2000m(poor GPS))
     * */
    private final int recordingGpsAccuracy = 50;
    /**
     * a new segment is creted if the distance between two locations is greater than (200m recommended) (50-5000m)
     * */
    private final int maxRecordingDistance = 200;
    public static final double PAUSE_LATITUDE = 100.0;
    /**
     * 10m between recorded locations (1-100m)
     * */
    private final int recordingDistanceInterval = 10;
    private long currentRecordingInterval;
    public boolean isIdle;

    public LocationFilter(LocationListenerPolicy locationListenerPolicy){
        this.locationListenerPolicy = locationListenerPolicy;
        mAltitudes = new LinkedList<Double>();
        mWeakLocations = new Vector<>();
        mSpeedSanityCheck = false;
        currentRecordingInterval = locationListenerPolicy.getDesiredPollingInterval();
    }

    public Location getPrevLocation(){
        return previousLocation;
    }
    public void addLocation2(Location proposedLocation) {
        previousLocation = proposedLocation;
    }


    public void addLocation1(Location proposedLocation) {
        if(previousLocation == null){
            previousLocation = proposedLocation;
            return;
        }

        try {
            // Do no include log wrong 0.0 lat 0.0 long, skip to next value in while-loop
            if (proposedLocation != null && (proposedLocation.getLatitude() == 0.0d || proposedLocation.getLongitude() == 0.0d)) {
                Log.w(TAG, "A wrong location was received, 0.0 latitude and 0.0 longitude... ");
                proposedLocation = null;
            }

            // Do not log a waypoint which is more inaccurate then is configured to be acceptable
            if (proposedLocation != null && proposedLocation.getAccuracy() > mMaxAcceptableAccuracy) {
                Log.w(TAG, String.format("A weak location was received, lots of inaccuracy... (%f is more then max %f)", proposedLocation.getAccuracy(),
                        mMaxAcceptableAccuracy));
                proposedLocation = addBadLocation(proposedLocation);
            }

            // Do not log a waypoint which might be on any side of the previous waypoint
            if (proposedLocation != null && proposedLocation.getAccuracy() > previousLocation.distanceTo(proposedLocation)) {
                Log.w(TAG, String.format("A weak location was received, not quite clear from the previous waypoint... (%f more then max %f)",
                        proposedLocation.getAccuracy(), previousLocation.distanceTo(proposedLocation)));
                proposedLocation = addBadLocation(proposedLocation);
            }

            // Speed checks, check if the proposed location could be reached from the previous one in sane speed
            // Common to jump on network logging and sometimes jumps on Samsung Galaxy S type of devices
            if (mSpeedSanityCheck && proposedLocation != null && previousLocation != null) {
                // To avoid near instant teleportation on network location or glitches cause continent hopping
                float meters = proposedLocation.distanceTo(previousLocation);
                long seconds = (proposedLocation.getTime() - previousLocation.getTime()) / 1000L;
                float speed = meters / seconds;
                if (speed > MAX_REASONABLE_SPEED) {
                    Log.w(TAG, "A strange location was received, a really high speed of " + speed + " m/s, prob wrong...");
                    proposedLocation = addBadLocation(proposedLocation);
                    // Might be a messed up Samsung Galaxy S GPS, reset the logging
                /*
                if (speed > 2 * MAX_REASONABLE_SPEED && mPrecision != Constants.LOGGING_GLOBAL) {
                    Log.w(TAG, "A strange location was received on GPS, reset the GPS listeners");
                    stopListening();
                    mLocationManager.removeGpsStatusListener(mStatusListener);
                    mLocationManager = (ServiceLocationManager) this.getSystemService(Context.LOCATION_SERVICE);
                    sendRequestStatusUpdateMessage();
                    sendRequestLocationUpdatesMessage();
                }
                */
                }
            }

            // Remove speed if not sane
            if (mSpeedSanityCheck && proposedLocation != null && proposedLocation.getSpeed() > MAX_REASONABLE_SPEED) {
                Log.w(TAG, "A strange speed, a really high speed, prob wrong...");
                proposedLocation.removeSpeed();
            }

            // Remove latitude if not sane
            if (mSpeedSanityCheck && proposedLocation != null && proposedLocation.hasAltitude()) {
                if (!addSaneAltitude(proposedLocation.getAltitude())) {
                    Log.w(TAG, "A strange latitude, a really big difference, prob wrong...");
                    proposedLocation.removeAltitude();
                }
            }
            // Older bad locations will not be needed
            if (proposedLocation != null) {
                mWeakLocations.clear();
            }
            if(proposedLocation != null) {
                previousLocation.set(proposedLocation);
            }
        }catch (Exception ex){
            Log.e(TAG,"addLocation",ex);
        }
    }
    private Location addBadLocation(Location location) {
        mWeakLocations.add(location);
        if (mWeakLocations.size() < 3) {
            location = null;
        } else {
            Location best = mWeakLocations.lastElement();
            for (Location whimp : mWeakLocations) {
                if (whimp.hasAccuracy() && best.hasAccuracy() && whimp.getAccuracy() < best.getAccuracy()) {
                    best = whimp;
                } else {
                    if (whimp.hasAccuracy() && !best.hasAccuracy()) {
                        best = whimp;
                    }
                }
            }
            synchronized (mWeakLocations) {
                mWeakLocations.clear();
            }
            location = best;
        }
        return location;
    }
    /**
     * Builds a bit of knowledge about altitudes to expect and return if the
     * added value is deemed sane.
     *
     * @param altitude
     * @return whether the latitude is considered sane
     */
    private boolean addSaneAltitude(double altitude) {
        boolean sane = true;
        double avg = 0;
        int elements = 0;
        // Even insane latitude shifts increases alter perception
        synchronized (mAltitudes) {
            mAltitudes.add(altitude);
            if (mAltitudes.size() > 3) {
                mAltitudes.poll();
            }
            for (Double alt : mAltitudes) {
                avg += alt;
                elements++;
            }
            avg = avg / elements;
            sane = Math.abs(altitude - avg) < MAX_REASONABLE_ALTITUDECHANGE;
        }
        return sane;
    }

    public void addLocation(Location location) {
        try {
            if (!LocationUtils.isValidLocation(location)) {
                Log.w(TAG, "Ignore onLocationChangedAsync. location is invalid.");
                return;
            }

            if (!location.hasAccuracy() || location.getAccuracy() >= recordingGpsAccuracy) {
                Log.d(TAG, "Ignore onLocationChangedAsync. Poor accuracy.");
                return;
            }

            // Fix for phones that do not set the time field
            if (location.getTime() == 0L) {
                location.setTime(System.currentTimeMillis());
            }

            Location lastValidTrackPoint = previousLocation;//getLastValidTrackPointInCurrentSegment(track.getId());
            long idleTime = 0L;
            if (lastValidTrackPoint != null && location.getTime() > lastValidTrackPoint.getTime()) {
                idleTime = location.getTime() - lastValidTrackPoint.getTime();
            }
            locationListenerPolicy.updateIdleTime(idleTime);
            if (currentRecordingInterval != locationListenerPolicy.getDesiredPollingInterval()) {
                //TODO: registerLocationListener();
                Log.d(TAG, "need register LocationListener");
            }

//            SensorDataSet sensorDataSet = getSensorDataSet();
//            if (sensorDataSet != null) {
//                location = new MyTracksLocation(location, sensorDataSet);
//            }

            // Always insert the first segment location
//            if (!currentSegmentHasLocation) {
//                insertLocation(track, location, null);
//                currentSegmentHasLocation = true;
//                lastLocation = location;
//                return;
//            }

            if (!LocationUtils.isValidLocation(lastValidTrackPoint)) {
                Log.d(TAG, "Should not happen. The current segment should have a location. Just insert the current location.");
                previousLocation = location;
                return;
            }

            double distanceToLastTrackLocation = location.distanceTo(lastValidTrackPoint);
            if (distanceToLastTrackLocation > maxRecordingDistance) {
//                insertLocation(track, lastLocation, lastValidTrackPoint);

                Location pause = new Location(LocationManager.GPS_PROVIDER);
                pause.setLongitude(0);
                pause.setLatitude(PAUSE_LATITUDE);
                pause.setTime(previousLocation.getTime());
//                insertLocation(track, pause, null);
//
//                insertLocation(track, location, null);
                isIdle = false;
            } else if (/*sensorDataSet != null || */distanceToLastTrackLocation >= recordingDistanceInterval) {
//                insertLocation(track, lastLocation, lastValidTrackPoint);
//                insertLocation(track, location, null);
                isIdle = false;
            } else if (!isIdle && location.hasSpeed() && location.getSpeed() < MAX_NO_MOVEMENT_SPEED) {
//                insertLocation(track, lastLocation, lastValidTrackPoint);
//                insertLocation(track, location, null);
                isIdle = true;
            } else if (isIdle && location.hasSpeed() && location.getSpeed() >= MAX_NO_MOVEMENT_SPEED) {
//                insertLocation(track, lastLocation, lastValidTrackPoint);
//                insertLocation(track, location, null);
                isIdle = false;
            } else {
                Log.d(TAG, "Not recording location, idle");
            }
            previousLocation = location;
        } catch (Error e) {
            Log.e(TAG, "Error in onLocationChangedAsync", e);
            throw e;
        } catch (RuntimeException e) {
            Log.e(TAG, "RuntimeException in onLocationChangedAsync", e);
            throw e;
        }
    }

    public void clear() {
        previousLocation = null;
    }
}
