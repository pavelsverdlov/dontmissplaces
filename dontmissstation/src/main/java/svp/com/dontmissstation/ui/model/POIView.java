package svp.com.dontmissstation.ui.model;

import org.osmdroid.util.GeoPoint;

import svp.app.map.android.GoogleApiMapPlaceProvider;
import svp.app.map.model.IPOIView;
import svp.app.map.model.PlaceExtraTags;
import svp.app.map.model.Point2D;

public class POIView implements IPOIView {

    private final String name;
    private final String address;
    private Point2D point;

    public POIView(GoogleApiMapPlaceProvider.Place place) {
        name = place.name;
        point = new Point2D(place.geometry.location.lat,place.geometry.location.lng);
        address = place.formatted_address;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getType() {
        return null;
    }

    @Override
    public String getAddress() {
        return address;
    }

    @Override
    public String getLocationStringFormat() {
        return null;
    }

    @Override
    public String getAccuracyDistance() {
        return null;
    }

    @Override
    public PlaceExtraTags getExtraTags() {
        return null;
    }

    @Override
    public Point2D getPoint() {
        return point;
    }

    @Override
    public GeoPoint getGeoPoint() {
        return point.getGeoPoint();
    }
}
