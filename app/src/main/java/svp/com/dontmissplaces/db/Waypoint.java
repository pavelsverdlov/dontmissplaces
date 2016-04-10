package svp.com.dontmissplaces.db;

import android.location.Location;

public class Waypoint {
    public long id;
    /** The latitude */
    private double latitude;
    /** The longitude */
    private double longitude;
    /** The recorded time */
    long time;
    /** The min in meters per second */
    double speed;
    /** The segment _id to which this segment belongs */
    int track;
    /** The accuracy of the fix */
    private double accuracy;
    /** The altitude */
    private double altitude;
    /** the bearing of the fix */
    private double bearing;

    double getLatitude(){ return latitude; }
    double getLongitude(){ return longitude; }
    double getAccuracy(){ return accuracy; }
    double getBearing(){ return bearing; }
    long getTime(){ return time; }
    double getAltitude(){return altitude; }


    public Waypoint(int trackId, Location location){
        track = trackId;

        time = location.getTime();
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        accuracy = location.getAccuracy();
        altitude = location.getAltitude();
        bearing = location.getBearing();

        speed = location.getSpeed();
    }
}
