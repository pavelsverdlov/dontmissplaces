package svp.com.dontmissplaces.ui.model;

import org.osmdroid.util.GeoPoint;

public interface IPOIView {
    String getName();
    String getType();
    GeoPoint getGeoPoint();
}
