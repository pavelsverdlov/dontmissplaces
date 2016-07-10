package svp.com.dontmissplaces.db;

import android.database.Cursor;

import org.json.JSONException;
import org.json.JSONObject;

public class Place extends dto {
    public final long osmNodeId;
    public final String osmType;
    public final double osmRank;

    public final int nominatimId;
    public final String nominatimType;
    public final String nominatimCategory;
    public final double nominatimImportance;

    public String googlePlaceId;

    public String address;
    public String country;
    public String city;

    public String description;
    public String title;
    //json obj
    //"extratags":{"website":"http:\/\/mure-restaurant.com\/","opening_hours":"Mo-Fr 08:30-17:00; Sa 10:00-17:00"}
    //"extratags":{"cuisine":"chinese"}
    public String extratags;

    public double latitude;
    public double longitude;

    public int placeType;
    public long creationTime;
    public String iconUrl;


    public Place(long id) {
        super(id);
        osmNodeId = -1;
        osmType =null;
        osmRank = -1;

        nominatimId = -1;
        nominatimType = nominatimCategory = null;
        nominatimImportance = -1;

        latitude = longitude = -1;
    }
    public Place(double longitude, double latitude) {
        this(-1);
        this.latitude = latitude;
        this.longitude = longitude;
    }
    public Place(JSONObject jPlace) throws JSONException {
        super(-1);
        osmNodeId = jPlace.getLong("osm_id");
        osmType= jPlace.getString("osm_type");
        osmRank = jPlace.getDouble("place_rank");

        nominatimId = jPlace.getInt("place_id");
        nominatimType = jPlace.getString("type");
        nominatimCategory = jPlace.getString("category");
        nominatimImportance= jPlace.getDouble("importance");

        //"boundingbox":["48.8687437","48.8695361","2.3407995","2.3419343"],
        latitude = jPlace.getDouble("lat");
        longitude = jPlace.getDouble("lon");
        title = jPlace.getString("display_name");
        extratags = jPlace.optString("extratags");
        //"icon":"http:\/\/nominatim.openstreetmap.org\/images\/mapicons\/tourist_monument.p.20.png"}

        iconUrl = jPlace.optString("icon");

        //creationTime = new Date().getTime();
    }

    public Place(Cursor cursor) {
        super(cursor.getLong(cursor.getColumnIndex(DatabaseStructure.Places._ID)));
        osmNodeId = cursor.getInt(cursor.getColumnIndex(DatabaseStructure.Places.OSM_NODE_ID));
        osmType = cursor.getString(cursor.getColumnIndex(DatabaseStructure.Places.OSM_TYPE));
        osmRank = cursor.getDouble(cursor.getColumnIndex(DatabaseStructure.Places.OSM_PLACE_RANK));

        nominatimId= cursor.getInt(cursor.getColumnIndex(DatabaseStructure.Places.NOMINATIM_PLACE_ID));
        nominatimType= cursor.getString(cursor.getColumnIndex(DatabaseStructure.Places.NOMINATIM_TYPE));
        nominatimCategory = cursor.getString(cursor.getColumnIndex(DatabaseStructure.Places.NOMINATIM_CATEGORY));
        nominatimImportance = cursor.getDouble(cursor.getColumnIndex(DatabaseStructure.Places.NOMINATIM_IMPORTANCE));

        title = getTitle(cursor);
        extratags = cursor.getString(cursor.getColumnIndex(DatabaseStructure.Places.EXTRATAGS));

        latitude = cursor.getDouble(cursor.getColumnIndex(DatabaseStructure.Places.LATITUDE));
        longitude = cursor.getDouble(cursor.getColumnIndex(DatabaseStructure.Places.LONGITUDE));

        /*
        country = cursor.getString(cursor.getColumnIndex(DatabaseStructure.Places.COUNTRY));
        city = cursor.getString(cursor.getColumnIndex(DatabaseStructure.Places.CITY));
        address = cursor.getString(cursor.getColumnIndex(DatabaseStructure.Places.ADDRESS));
        description = cursor.getString(cursor.getColumnIndex(DatabaseStructure.Places.DESCRIPTION));
        googlePlaceId = cursor.getString(cursor.getColumnIndex(DatabaseStructure.Places.GOOGLE_PLACE_ID));

        placeType = cursor.getInt(cursor.getColumnIndex(DatabaseStructure.Places.PLACETYPE));
        */
        creationTime = cursor.getLong(cursor.getColumnIndex(DatabaseStructure.Places.CREATION_TIME));
    }



    public static String getTitle(Cursor cursor) {
        return cursor.getString(cursor.getColumnIndex(DatabaseStructure.Places.TITLE));
    }


}
