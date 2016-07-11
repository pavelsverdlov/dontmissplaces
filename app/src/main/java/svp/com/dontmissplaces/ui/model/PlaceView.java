package svp.com.dontmissplaces.ui.model;

import android.database.Cursor;
import android.view.View;
import android.widget.TextView;

import com.svp.infrastructure.common.PermissionUtils;
import com.svp.infrastructure.common.ViewExtensions;
import com.svp.infrastructure.common.view.ICursorParcelable;

import org.osmdroid.util.GeoPoint;

import svp.com.dontmissplaces.R;
import svp.com.dontmissplaces.db.Place;
import svp.com.dontmissplaces.model.Map.Point2D;

public class PlaceView implements IPOIView {

    protected Place place;
    private final Point2D originalPoint;
    private PlaceAddressDetails addressDetails;
    private PlaceExtraTags extraTags;

    public PlaceView(Place place, Point2D originalPoint) {
        this.place = place;
        this.originalPoint = originalPoint;
        addressDetails = new PlaceAddressDetails(place.title);
        extraTags = new PlaceExtraTags(place.extratags);
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
        return null;
    }


    public String getType() {
        return place.nominatimType;
    }
    public GeoPoint getGeoPoint(){
        return new GeoPoint(place.latitude, place.longitude);
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



}
