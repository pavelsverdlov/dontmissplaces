package svp.com.dontmissplaces.db;

import android.database.Cursor;
import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

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
    long session;
    /** The accuracy of the fix */
    private double accuracy;
    /** The latitude */
    private double altitude;
    /** the bearing of the fix */
    private double bearing;

    double getLatitude(){ return latitude; }
    double getLongitude(){ return longitude; }
    double getAccuracy(){ return accuracy; }
    double getBearing(){ return bearing; }
    long getTime(){ return time; }
    double getAltitude(){return altitude; }


    public Waypoint(long trackId, Location location){
        session = trackId;

        time = location.getTime();
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        accuracy = location.getAccuracy();
        altitude = location.getAltitude();
        bearing = location.getBearing();

        speed = location.getSpeed();
    }
    public Waypoint(long trackId, Cursor cursor){
        session = trackId;

        time = cursor.getLong(cursor.getColumnIndex(DatabaseStructure.Waypoints.TIME));
        latitude = cursor.getDouble(cursor.getColumnIndex(DatabaseStructure.Waypoints.LATITUDE));
        longitude = cursor.getDouble(cursor.getColumnIndex(DatabaseStructure.Waypoints.LONGITUDE));
        accuracy = cursor.getDouble(cursor.getColumnIndex(DatabaseStructure.Waypoints.ACCURACY));
        altitude = cursor.getDouble(cursor.getColumnIndex(DatabaseStructure.Waypoints.ALTITUDE));
        bearing = cursor.getDouble(cursor.getColumnIndex(DatabaseStructure.Waypoints.BEARING));

        speed = cursor.getDouble(cursor.getColumnIndex(DatabaseStructure.Waypoints.SPEED));
    }


    public LatLng getLatLng() {
        return new LatLng(latitude,longitude);
    }
}
