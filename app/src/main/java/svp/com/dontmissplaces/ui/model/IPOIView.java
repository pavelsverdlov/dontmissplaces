package svp.com.dontmissplaces.ui.model;

import com.svp.infrastructure.common.view.ICursorParcelable;

import org.osmdroid.util.GeoPoint;

import svp.com.dontmissplaces.db.Place;
import svp.com.dontmissplaces.model.Map.Point2D;

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
