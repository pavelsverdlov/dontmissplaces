package svp.com.dontmissplaces.model;

import android.app.Activity;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

import svp.com.dontmissplaces.db.Place;

public class PlaceProvider {
    private final Activity activity;
    private final Geocoder geocoder;

    public PlaceProvider(Activity activity) {
        this.activity = activity;
        Locale locale = Locale.getDefault();
        geocoder = new Geocoder(activity, locale);
    }

    public Place getPlace(LatLng latLng) {

        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (addresses != null) {
            return parceAddress(addresses.get(0));
        }
        return null;
    }
    public Vector<Place> getPlace(String text) {
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocationName(text,10);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Vector<Place> places = new Vector<>();
        if (addresses != null) {
            for (Address a : addresses){
                places.add(parceAddress(a));
            }
        }
        return places;
    }

    private Place parceAddress(Address address) {
        Place place = new Place(address.getLongitude(),address.getLatitude());
        place.city = address.getLocality();
        String state = address.getAdminArea();
        place.country = address.getCountryName();
        String postalCode = address.getPostalCode();
        place.title = address.getFeatureName();
        place.address = address.getAddressLine(0);
        StringBuilder strReturnedAddress = new StringBuilder("");

        for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
            strReturnedAddress.append(address.getAddressLine(i)).append(" ");
        }
        place.description = strReturnedAddress.toString();
        return place;
    }
}
