package svp.com.dontmissplaces.db;

import android.database.Cursor;

public class Place extends dto {
    public String address;
    public String description;
    public String googlePlaceId;
    public double latitude;
    public double longitude;
    public String title;
    public int placeType;
    public long creationTime;
    public String country;
    public String city;

    public Place(long id) {
        super(id);
    }

    public Place(Cursor cursor) {
        super(cursor.getLong(cursor.getColumnIndex(DatabaseStructure.Places._ID)));
        country = cursor.getString(cursor.getColumnIndex(DatabaseStructure.Places.COUNTRY));
        city = cursor.getString(cursor.getColumnIndex(DatabaseStructure.Places.CITY));
        address = cursor.getString(cursor.getColumnIndex(DatabaseStructure.Places.ADDRESS));
        description = cursor.getString(cursor.getColumnIndex(DatabaseStructure.Places.DESCRIPTION));
        googlePlaceId = cursor.getString(cursor.getColumnIndex(DatabaseStructure.Places.GOOGLE_PLACE_ID));
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
