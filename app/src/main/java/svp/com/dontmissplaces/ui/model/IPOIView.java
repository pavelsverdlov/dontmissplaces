package svp.com.dontmissplaces.ui.model;

import com.svp.infrastructure.common.view.ICursorParcelable;

import org.osmdroid.util.GeoPoint;

import svp.com.dontmissplaces.model.Map.Point2D;

public interface IPOIView  {
    String getName();
    String getType();
    GeoPoint getGeoPoint();
    String getAddress();
    PlaceExtraTags getExtraTags();

    String getLocationStringFormat();
    String getAccuracyDistance();

    Point2D getPoint();
}
