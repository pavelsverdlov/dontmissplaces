package svp.app.map.model;

import org.osmdroid.util.GeoPoint;

public interface IPOIView  {
    String getName();
    String getType();
    String getAddress();
    String getLocationStringFormat();
    String getAccuracyDistance();

    PlaceExtraTags getExtraTags();

    Point2D getPoint();
    GeoPoint getGeoPoint();
}
