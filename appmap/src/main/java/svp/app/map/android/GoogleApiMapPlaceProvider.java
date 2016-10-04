package svp.app.map.android;


import android.util.Log;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.*;
import com.google.api.client.http.javanet.*;

import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.Key;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.List;

import svp.app.map.HttpResponseReader;

public class GoogleApiMapPlaceProvider {
    public static class Place implements Serializable {

        @Key
        public String id;

        @Key
        public String name;

        @Key
        public String reference;

        @Key
        public String icon;

        @Key
        public String vicinity;

        @Key
        public Geometry geometry;

        @Key
        public String formatted_address;

        @Key
        public String formatted_phone_number;

        @Override
        public String toString() {
            return name + " - " + id + " - " + reference;
        }

        public static class Geometry implements Serializable
        {
            @Key
            public Location location;
        }

        public static class Location implements Serializable
        {
            @Key
            public double lat;

            @Key
            public double lng;
        }

    }
    public static class PlacesList implements Serializable {

        @Key
        public String status;

        @Key
        public List<Place> results;

    }
    public static class PlaceDetails implements Serializable {

        @Key
        public String status;

        @Key
        public Place result;

        @Override
        public String toString() {
            if (result!=null) {
                return result.toString();
            }
            return super.toString();
        }
    }

    /** Global instance of the HTTP transport. */
    private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();

    // Google API Key
    private static final String API_KEY = "AIzaSyD76hAcDE2CBYJE14l61rhzyG2xnAzOz6s";

    // Google Places serach url's
    private static final String PLACES_SEARCH_URL = "https://maps.googleapis.com/maps/api/place/search/json?";
    private static final String PLACES_TEXT_SEARCH_URL = "https://maps.googleapis.com/maps/api/place/search/json?";
    private static final String PLACES_DETAILS_URL = "https://maps.googleapis.com/maps/api/place/details/json?";

    private double _latitude;
    private double _longitude;
    private double _radius;

    /**
     * Searching places
     * @param latitude - latitude of place
     * @params longitude - longitude of place
     * @param radius - radius of searchable area
     * @param types - type of place to search
     * @return list of places
     * */
    public PlacesList search(double latitude, double longitude, double radius, String types)
            throws Exception {

        this._latitude = latitude;
        this._longitude = longitude;
        this._radius = radius;

        try {
            DecimalFormat format = new DecimalFormat("#.#######");
            HttpRequestFactory httpRequestFactory = createRequestFactory(HTTP_TRANSPORT);
            HttpRequest request = httpRequestFactory
                    .buildGetRequest(new GenericUrl(PLACES_SEARCH_URL));
            request.getUrl().put("key", API_KEY);
            request.getUrl().put("location", format.format(_latitude) + "," +format.format( _longitude));
            request.getUrl().put("radius", _radius); // in meters
            request.getUrl().put("sensor", "false");
            if(types != null){
                request.getUrl().put("types", types);
            }
//
            HttpResponse result = request.execute();

            HttpResponseReader reader = new HttpResponseReader(result);

            String text = reader.readToEnd();

            PlacesList list = result.parseAs(PlacesList.class);
            // Check log cat for places response status
            Log.d("Places Status", "" + list.status);
            return list;

        } catch (HttpResponseException e) {
            Log.e("Error:", e.getMessage());
            return null;
        }

    }

    /**
     * Searching single place full details
     * @param refrence - reference id of place
     *                 - which you will get in search api request
     * */
    public PlaceDetails getPlaceDetails(String reference) throws Exception {
        try {

            HttpRequestFactory httpRequestFactory = createRequestFactory(HTTP_TRANSPORT);
            HttpRequest request = httpRequestFactory
                    .buildGetRequest(new GenericUrl(PLACES_DETAILS_URL));
            request.getUrl().put("key", API_KEY);
            request.getUrl().put("reference", reference);
            request.getUrl().put("sensor", "false");

            PlaceDetails place = request.execute().parseAs(PlaceDetails.class);

            return place;

        } catch (HttpResponseException e) {
            Log.e("Error in Perform Details", e.getMessage());
            throw e;
        }
    }

    /**
     * Creating http request Factory
     * */
    public static HttpRequestFactory createRequestFactory(
            final HttpTransport transport) {
        return transport.createRequestFactory(new HttpRequestInitializer() {
            public void initialize(HttpRequest request) {

                HttpHeaders headers = new HttpHeaders();
                headers.setUserAgent("AndroidHive-Places-Test");
                request.setHeaders(headers);
                JsonObjectParser parser = new JsonObjectParser(new JacksonFactory());
                request.setParser(parser);
            }
        });
    }
}
