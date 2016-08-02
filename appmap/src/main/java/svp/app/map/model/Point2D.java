package svp.app.map.model;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

import org.osmdroid.util.GeoPoint;

public final class Point2D {
    private boolean isEmpty = false;
    public final double latitude;
    public final double longitude;

    public Point2D(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Point2D(GeoPoint p) {
        latitude = p.getLatitude();
        longitude = p.getLongitude();
    }

    public Point2D(Location location) {
        longitude = location.getLongitude();
        latitude = location.getLatitude();
    }

    public Point2D(LatLng latLng) {
        latitude = latLng.latitude;
        longitude = latLng.longitude;
    }
    /**
     * return distance in meters
     * */
    public long distanceTo(Point2D other){
        return getGeoPoint().distanceTo(other.getGeoPoint());
    }

    public LatLng getLatLng() {
       return new LatLng(latitude, longitude);
    }

    public GeoPoint getGeoPoint() {
        return new GeoPoint(latitude,longitude);
    }

    public boolean isEmpty(){
        return isEmpty;
    }

    public static Point2D empty() {
        Point2D p = new Point2D(0, 0);
        p.isEmpty = true;
        return p;
    }

    @Override
    public boolean equals(Object ob) {
        if (ob == null) return false;
        if (ob.getClass() != getClass()) return false;

        Point2D other = (Point2D) ob;

        if (!(latitude == other.latitude)) return false;
        if (!(longitude == other.longitude)) return false;
        return true;
    }

    @Override
    public int hashCode() {
        return (int)(latitude * 1E6) ^ (int) (longitude * 1E6);
    }
}
