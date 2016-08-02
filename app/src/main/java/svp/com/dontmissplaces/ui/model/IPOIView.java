package svp.com.dontmissplaces.ui.model;

import org.osmdroid.util.GeoPoint;

import svp.com.dontmissplaces.db.Place;
import svp.app.map.model.Point2D;

public interface IPOIView  {
    String getName();
    String getType();
    String getAddress();
    String getLocationStringFormat();
    String getAccuracyDistance();

    PlaceExtraTags getExtraTags();

    Point2D getPoint();
    GeoPoint getGeoPoint();

    Place getPlace();
    void update(Place place);
}
