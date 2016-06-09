package svp.com.dontmissplaces.model.Map;

import android.location.Location;
import android.os.AsyncTask;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;

import javax.net.ssl.HttpsURLConnection;

import svp.com.dontmissplaces.R;

/**
 * https://developers.google.com/places/place-id#----api-
 */
public class GoogleMapsPlaceService {
    public interface IGetPlaceCallback {

    }
    private final String getPlaceIdUrl = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=-33.8670522,151.1957362&radius=500&types=food&name=cruise&key=AddYourOwnKeyHere";
    private final String getPlaceInfoUrl = "https://maps.googleapis.com/maps/api/place/details/json?placeid=ChIJrTLr-GyuEmsRBfy61i59si0&key=AddYourOwnKeyHere";

    public GoogleMapsPlaceService(){
        //Places.GeoDataApi.getPlaceById
    }

    public void getPlace(Projection mProjection, LatLng latLng, String key, IGetPlaceCallback callback) {
        StringBuilder searchURL = new StringBuilder();
        searchURL.append("https://maps.googleapis.com/maps/api/place/nearbysearch/");
        searchURL.append("json?");
        searchURL.append("location=" + latLng.latitude + "," + latLng.longitude);
        searchURL.append("&radius=" + calculateProjectionRadiusInMeters(mProjection));
        searchURL.append("&key=" + "AIzaSyB93G2gyFjk1cLBRD7GG2p3LVFbL3QN5yg");

        new PlaceSearchAPITask().execute(searchURL.toString());
    }

    private double calculateProjectionRadiusInMeters(Projection projection) {

        LatLng farLeft = projection.getVisibleRegion().farLeft;
        LatLng nearRight = projection.getVisibleRegion().nearRight;

        Location farLeftLocation = new Location("Point A");
        farLeftLocation.setLatitude(farLeft.latitude);
        farLeftLocation.setLongitude(farLeft.longitude);

        Location nearRightLocation = new Location("Point B");
        nearRightLocation.setLatitude(nearRight.latitude);
        nearRightLocation.setLongitude(nearRight.longitude);

        return farLeftLocation.distanceTo(nearRightLocation) / 2 ;
    }

    private class PlaceSearchAPITask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... uri) {
            String responseString = null;
            try {
                URL url = new URL(uri[0]);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                if(conn.getResponseCode() == HttpsURLConnection.HTTP_OK){
                    // Do normal input or output stream reading
                    InputStream is = conn.getInputStream();
                    responseString = readIt(is, 10000);
                }
                else {
                  //  response = "FAILED"; // See documentation for more info on response handling
                }
          //  } catch (ClientProtocolException e) {
                //TODO Handle problems..
            } catch (IOException e) {
                //TODO Handle problems..
            }
            Gson gson = new Gson();
            //place student = gson.fromJson(responseString, place.class);

            return responseString;
        }
        public String readIt(InputStream stream, int len) throws IOException, UnsupportedEncodingException {
            Reader reader = null;
            reader = new InputStreamReader(stream, "UTF-8");
            char[] buffer = new char[len];
            reader.read(buffer);
            return new String(buffer);
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            //Do anything with response..
        }



    }
}
