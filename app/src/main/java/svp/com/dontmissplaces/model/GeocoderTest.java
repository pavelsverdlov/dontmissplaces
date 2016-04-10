package svp.com.dontmissplaces.model;

import android.app.Activity;
import android.location.Address;
import android.location.Geocoder;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by Pasha on 3/20/2016.
 */
public class GeocoderTest {
    public void onMapLongClick(Activity activity, LatLng latLng) {
        Geocoder geocoder = new Geocoder(activity, Locale.getDefault());

        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String city = addresses.get(0).getLocality();
        String state = addresses.get(0).getAdminArea();
        String zip = addresses.get(0).getPostalCode();
        String country = addresses.get(0).getCountryName();
    }
}
