package svp.com.dontmissplaces.ui.model;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

import svp.com.dontmissplaces.utils.LocationEx;

/**
 * Created by Pasha on 5/12/2016.
 */
public final class LocationView {
    private final Location location;

    public LocationView(Location location){
        this.location = location;
    }

    public LatLng getLatLng(){
        return LocationEx.getLatLng(location);
    }
}
