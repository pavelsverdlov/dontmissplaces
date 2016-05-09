package svp.com.dontmissplaces.model.gps;

import android.location.Location;
import android.util.Log;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Vector;

public class LocationFilter {
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
    private float mMaxAcceptableAccuracy = 20;
    private final Vector<Location> mWeakLocations;
    private final Queue<Double> mAltitudes;
    boolean mSpeedSanityCheck;
    private Location previousLocation;

    public LocationFilter(){
        mAltitudes = new LinkedList<Double>();
        mWeakLocations = new Vector<>();
        mSpeedSanityCheck = false;
    }

    public Location getPrevLocation(){
        return previousLocation;
    }
    public void addLocation(Location proposedLocation) {
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
                    mLocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
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

            // Remove altitude if not sane
            if (mSpeedSanityCheck && proposedLocation != null && proposedLocation.hasAltitude()) {
                if (!addSaneAltitude(proposedLocation.getAltitude())) {
                    Log.w(TAG, "A strange altitude, a really big difference, prob wrong...");
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
     * @return whether the altitude is considered sane
     */
    private boolean addSaneAltitude(double altitude) {
        boolean sane = true;
        double avg = 0;
        int elements = 0;
        // Even insane altitude shifts increases alter perception
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

}
