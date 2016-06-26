package svp.com.dontmissplaces.model.Map;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

import org.osmdroid.util.GeoPoint;

public final class Point2D {
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

    public LatLng getLatLng() {
       return new LatLng(latitude, longitude);
    }

    public GeoPoint getGeoPoint() {
        return new GeoPoint(latitude,longitude);
    }

    public static Point2D empty() {
        return new Point2D(0,0);
    }
}
