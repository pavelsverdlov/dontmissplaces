package svp.com.dontmissplaces.db;

import android.provider.BaseColumns;

public class DatabaseStructure {
    public static final class Tracks extends TracksColumns implements android.provider.BaseColumns {
        /** The name of this table */
        public static final String TABLE = "tracks";
        public static final String CREATE_STATEMENT =
                "CREATE TABLE " + Tracks.TABLE + "(" +
                        " " + Tracks._ID           + " " + Tracks._ID_TYPE +
                        "," + " " + Tracks.NAME          + " " + Tracks.NAME_TYPE +
                        "," + " " + Tracks.CREATION_TIME + " " + Tracks.CREATION_TIME_TYPE +
                        ");";
        public static final String SELECT_ALL = "SELECT * FROM " + Tracks.TABLE + ";";
        public static final String DELETE_ALL = "DELETE FROM " + Tracks.TABLE + ";";
    }

    public static final class Sessions implements SessionTrackColumns {
        public static final String TABLE = "session_tracks";
        static final String CREATE_STATEMENT = "CREATE TABLE " + TABLE  +
                "( " + _ID   + " " + _ID_TYPE +
                ", " + TRACK + " " + TRACK_TYPE +
                ", " + CREATION_TIME + " " + CREATION_TIME_TYPE +
                ", FOREIGN KEY (" + TRACK + ") references " + Tracks.TABLE + "(" + Tracks._ID + ") ON DELETE CASCADE" +
                ");";
    }

    public static final class Waypoints extends WaypointsColumns {
        /** The name of this table, waypoints */
        public static final String TABLE = "waypoints";
        static final String CREATE_STATEMENT = "CREATE TABLE " + Waypoints.TABLE +
                "(" + " " + BaseColumns._ID + " " + WaypointsColumns._ID_TYPE +
                "," + " " + WaypointsColumns.LATITUDE  + " " + WaypointsColumns.LATITUDE_TYPE +
                "," + " " + WaypointsColumns.LONGITUDE + " " + WaypointsColumns.LONGITUDE_TYPE +
                "," + " " + WaypointsColumns.TIME      + " " + WaypointsColumns.TIME_TYPE +
                "," + " " + WaypointsColumns.SPEED     + " " + WaypointsColumns.SPEED_TYPE +
                "," + " " + WaypointsColumns.SESSION + " " + WaypointsColumns.SESSION_TYPE +
                "," + " " + WaypointsColumns.ACCURACY  + " " + WaypointsColumns.ACCURACY_TYPE +
                "," + " " + WaypointsColumns.ALTITUDE  + " " + WaypointsColumns.ALTITUDE_TYPE +
                "," + " " + WaypointsColumns.BEARING   + " " + WaypointsColumns.BEARING_TYPE +
                ", FOREIGN  KEY (" + SESSION + ") references " + Sessions.TABLE + "(" + Sessions._ID + ") ON DELETE CASCADE" +
                ");";
        public static final String DELETE_BY = "DELETE FROM " + Tracks.TABLE + " WHERE;";
    }

    private static class TracksColumns implements BaseColumns{
        public static final String NAME          = "name";
        public static final String CREATION_TIME = "creationtime";
        static final String CREATION_TIME_TYPE   = "INTEGER NOT NULL";
        static final String NAME_TYPE            = "TEXT";
        static final String _ID_TYPE             = "INTEGER PRIMARY KEY AUTOINCREMENT";
    }

    private static class WaypointsColumns implements android.provider.BaseColumns, LocationColumns {
        /** The latitude */
       // public static final String LATITUDE = "latitude";
        /** The longitude */
       // public static final String LONGITUDE = "longitude";
        /** The recorded time */
        public static final String TIME = "time";
        /** The min in meters per second */
        public static final String SPEED = "min";
        /** The segment _id to which this session belongs */
        public static final String SESSION = "session";
        /** The accuracy of the fix */
        public static final String ACCURACY = "accuracy";
        /** The latitude */
        public static final String ALTITUDE = "altitude";
        /** the bearing of the fix */
        public static final String BEARING = "bearing";

      //  static final String LATITUDE_TYPE  = "REAL NOT NULL";
     //   static final String LONGITUDE_TYPE = "REAL NOT NULL";
        static final String TIME_TYPE      = "INTEGER NOT NULL";
        static final String SPEED_TYPE     = "REAL NOT NULL";
        static final String SESSION_TYPE   = "INTEGER NOT NULL";
        static final String ACCURACY_TYPE  = "REAL";
        static final String ALTITUDE_TYPE  = "REAL";
        static final String BEARING_TYPE   = "REAL";
        static final String _ID_TYPE       = "INTEGER PRIMARY KEY AUTOINCREMENT";
    }

    private interface SessionTrackColumns extends Base {
        /** The segment _id to which this session belongs */
        String TRACK = "session";
        String TRACK_TYPE   = "INTEGER NOT NULL";

        String CREATION_TIME = "creationtime";
        String CREATION_TIME_TYPE   = "INTEGER NOT NULL";
    }


    /**
     * Save my places
     */
    public static final class Places implements PlaceColumns{
        public static final String TABLE = "Places";
        public static final String CREATE_STATEMENT =
                "CREATE TABLE " + TABLE + "(" +
                        " " + _ID               + " " + _ID_TYPE +
                        "," + GOOGLE_PLACE_ID   + " " + GOOGLE_PLACE_ID_TYPE +
                        "," + OSM_NODE_ID       + " " + OSM_NODE_ID_TYPE +
                        "," + DESCRIPTION       + " " + DESCRIPTION_TYPE +
                        "," + PLACETYPE         + " " + PLACETYPE_TYPE +
                        "," + COUNTRY           + " " + COUNTRY_TYPE +
                        "," + CITY              + " " + CITY_TYPE +
                        "," + ADDRESS           + " " + ADDRESS_TYPE +
                        "," + TITLE             + " " + TITLE_TYPE +
                        "," + CREATION_TIME     + " " + CREATION_TIME_TYPE +
                        "," + LATITUDE          + " " + LATITUDE_TYPE +
                        "," + LONGITUDE         + " " + LONGITUDE_TYPE +
                        ");";
        public static final String SELECT_ALL = "SELECT * FROM " + TABLE + ";";
        public static final String DELETE_ALL = "DELETE FROM " + TABLE + ";";
    }
    public interface PlaceColumns extends Base, LocationColumns{
        /**
         * example - "place_id" : "ChIJrTLr-GyuEmsRBfy61i59si0",
         * */
        String GOOGLE_PLACE_ID = "googleplaceid";
        String GOOGLE_PLACE_ID_TYPE   = "TEXT";

        String OSM_NODE_ID = "osmnodeid";
        String OSM_NODE_ID_TYPE   = "INTEGER NOT NULL";

        String TITLE = "title";
        String TITLE_TYPE   = "TEXT";

        String DESCRIPTION = "description";
        String DESCRIPTION_TYPE   = "TEXT";

        String PLACETYPE = "placetype";
        String PLACETYPE_TYPE   = "INTEGER NOT NULL";

        String CITY = "city";
        String CITY_TYPE   = "TEXT";

        String COUNTRY = "country";
        String COUNTRY_TYPE   = "TEXT";

        String ADDRESS = "address";
        String ADDRESS_TYPE   = "TEXT";

        String CREATION_TIME = "creationtime";
        String CREATION_TIME_TYPE   = "INTEGER NOT NULL";
    }

    public interface Base extends BaseColumns {
        String _ID_TYPE       = "INTEGER PRIMARY KEY AUTOINCREMENT";
    }

    public interface LocationColumns{
        String LATITUDE = "latitude";
        String LATITUDE_TYPE  = "REAL NOT NULL";

        String LONGITUDE = "longitude";
        String LONGITUDE_TYPE = "REAL NOT NULL";
    }
}
