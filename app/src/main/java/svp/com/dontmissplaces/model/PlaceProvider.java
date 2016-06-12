package svp.com.dontmissplaces.model;

import android.app.Activity;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import svp.com.dontmissplaces.db.Place;

/**
 * Created by Pasha on 5/31/2016.
 */
public class PlaceProvider {
    private final Activity activity;

    public PlaceProvider(Activity activity) {
        this.activity = activity;
    }

    public Place getPlace(LatLng latLng) {
        Geocoder geocoder = new Geocoder(activity, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Place place = new Place(latLng.longitude,latLng.latitude);

        if (addresses != null) {
            Address returnedAddress = addresses.get(0);


            place.city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            place.country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            place.title = addresses.get(0).getFeatureName();
            place.address = addresses.get(0).getAddressLine(0);
            StringBuilder strReturnedAddress = new StringBuilder("");

            for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
            }
            place.description = strReturnedAddress.toString();
        }
        return place;
    }
}
