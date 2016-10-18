package svp.com.dontmissstation.ui.model;

import org.osmdroid.util.GeoPoint;

import svp.app.map.model.IPOIView;
import svp.app.map.model.PlaceExtraTags;
import svp.app.map.model.Point2D;

public class POIStationView implements IPOIView {

    private final SubwayStationView station;

    public POIStationView(SubwayStationView station) {
        this.station = station;
    }

    @Override
    public String getName() {
        return station.getName();
    }

    @Override
    public String getType() {
        return "Subway station";
    }

    @Override
    public String getAddress() {
        return null;
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
        return station.getCoordinate();
    }

    @Override
    public GeoPoint getGeoPoint() {
        return station.getCoordinate().getGeoPoint();
    }
}
