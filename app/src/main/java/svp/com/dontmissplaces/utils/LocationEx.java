package svp.com.dontmissplaces.utils;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Pasha on 4/2/2016.
 */
public final class LocationEx {
    public static double getSpeed(Location prev, Location current) {
        double speed;
        if (current.hasSpeed()) {//if there is min from location
            //get location min
            speed = current.getSpeed();
        }else{
            speed = Math.sqrt(
                    Math.pow(current.getLongitude() - prev.getLongitude(), 2)
                            + Math.pow(current.getLatitude() - prev.getLatitude(), 2)
            ) / (current.getTime() - prev.getTime());
        }
        return speed;
    }
    public static LatLng getLatLng(Location location){
        return new LatLng(location.getLatitude(),location.getLongitude());
    }
}
