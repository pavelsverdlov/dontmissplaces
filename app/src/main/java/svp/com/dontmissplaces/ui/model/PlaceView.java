package svp.com.dontmissplaces.ui.model;

import android.graphics.Point;

import org.osmdroid.util.GeoPoint;

import svp.com.dontmissplaces.db.Place;
import svp.com.dontmissplaces.model.Map.Point2D;
import svp.com.dontmissplaces.model.Map.google.GPlaceAddressDetails;
import svp.com.dontmissplaces.model.nominatim.PlaceAddressDetails;

public class PlaceView implements IPOIView {

    protected Place place;
    private final Point2D originalPoint;
    private IPlaceAddressDetails addressDetails;
    private PlaceExtraTags extraTags;

    public PlaceView(Place place, Point2D originalPoint) {
        this.originalPoint = originalPoint;
        update(place);
    }

    public String getName(){
        return addressDetails.getName();
    }

    public String getAccuracyDistance(){
        //for search nearest POI to point we should check found point this original
        long dist = originalPoint.distanceTo(getPoint());
        if(!originalPoint.isEmpty() && dist > 5){
            return  "Near " + dist + " meters to ";
        }
        return "";
    }


    public String getType() {
        return place.nominatimType;
    }
    public GeoPoint getGeoPoint(){
        return new GeoPoint(place.latitude, place.longitude);
    }

    @Override
    public Place getPlace() { return place; }
    @Override
    public void update(Place place) {
        _update(place);
    }


    public String getAddress(){
        return addressDetails.getFullAddress();
    }
    public String getLocationStringFormat(){
        return place.latitude + " " + place.longitude;
    }

    @Override
    public Point2D getPoint() {
        return new Point2D(place.latitude, place.longitude);
    }

    public PlaceExtraTags getExtraTags(){ return extraTags; }

    public PlaceView() {
        originalPoint = Point2D.empty();
    }


    private void _update(Place place){
        this.place = place;

        if(place.nominatimId > 0) {
            addressDetails = new PlaceAddressDetails(place.title);
        }else{
            addressDetails = new GPlaceAddressDetails(place);
        }
        extraTags = new PlaceExtraTags(place.extratags);
    }

}
