package svp.com.dontmissplaces.db;

import android.database.Cursor;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

public class Place extends dto {
    public long osmNodeId;
    public String osmType;

    public int nominatimId;
    public String nominatimType;
    public String nominatimClass;
    public double nominatimImportance;

    public String googlePlaceId;

    public String address;
    public String country;
    public String city;

    public String description;
    public String title;

    public double latitude;
    public double longitude;

    public int placeType;
    public long creationTime;


    public Place(long id) {
        super(id);
    }
    public Place(JSONObject jPlace) throws JSONException {
        super(-1);
        osmNodeId = jPlace.getLong("osm_id");
        osmType= jPlace.getString("osm_type");

        nominatimId = jPlace.getInt("place_id");
        nominatimType = jPlace.getString("type");
        nominatimClass = jPlace.getString("class");
        nominatimImportance= jPlace.getDouble("importance");

        //"boundingbox":["48.8687437","48.8695361","2.3407995","2.3419343"],
        latitude = jPlace.getDouble("lat");
        longitude = jPlace.getDouble("lon");
        title = jPlace.getString("display_name");
        //"icon":"http:\/\/nominatim.openstreetmap.org\/images\/mapicons\/tourist_monument.p.20.png"}

        creationTime = new Date().getTime();
    }

    public Place(Cursor cursor) {
        super(cursor.getLong(cursor.getColumnIndex(DatabaseStructure.Places._ID)));
        country = cursor.getString(cursor.getColumnIndex(DatabaseStructure.Places.COUNTRY));
        city = cursor.getString(cursor.getColumnIndex(DatabaseStructure.Places.CITY));
        address = cursor.getString(cursor.getColumnIndex(DatabaseStructure.Places.ADDRESS));
        description = cursor.getString(cursor.getColumnIndex(DatabaseStructure.Places.DESCRIPTION));
        googlePlaceId = cursor.getString(cursor.getColumnIndex(DatabaseStructure.Places.GOOGLE_PLACE_ID));
        osmNodeId = cursor.getInt(cursor.getColumnIndex(DatabaseStructure.Places.OSM_NODE_ID));
        latitude = cursor.getLong(cursor.getColumnIndex(DatabaseStructure.Places.LATITUDE));
        longitude = cursor.getLong(cursor.getColumnIndex(DatabaseStructure.Places.LONGITUDE));
        title = getTitle(cursor);
        placeType = cursor.getInt(cursor.getColumnIndex(DatabaseStructure.Places.PLACETYPE));
        creationTime = cursor.getLong(cursor.getColumnIndex(DatabaseStructure.Places.CREATION_TIME));
    }

    public static String getTitle(Cursor cursor) {
        return cursor.getString(cursor.getColumnIndex(DatabaseStructure.Places.TITLE));
    }
}
