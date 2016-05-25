package svp.com.dontmissplaces.db;

import android.provider.BaseColumns;

public class DatabaseStructure {
    public static final class Tracks extends TracksColumns implements android.provider.BaseColumns {
        /** The name of this table */
        public static final String TABLE = "tracks";
        public static final String CREATE_STATEMENT =
                "CREATE TABLE " + Tracks.TABLE + "(" + " " + Tracks._ID           + " " + Tracks._ID_TYPE +
                        "," + " " + Tracks.NAME          + " " + Tracks.NAME_TYPE +
                        "," + " " + Tracks.CREATION_TIME + " " + Tracks.CREATION_TIME_TYPE +
                        ");";
        public static final String SELECT_ALL = "SELECT * FROM " + Tracks.TABLE + ";";
        public static final String DELETE_ALL = "DELETE FROM " + Tracks.TABLE + ";";


    }

    public static final class Sessions extends SessionTrackColumns {
        public static final String TABLE = "session_tracks";
        static final String CREATE_STATEMENT = "CREATE TABLE " + TABLE  +
                "( " + _ID   + " " + _ID_TYPE +
                ", " + TRACK + " " + TRACK_TYPE +
                ", " + CREATION_TIME + " " + CREATION_TIME_TYPE +
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
                ");";
        public static final String DELETE_BY = "DELETE FROM " + Tracks.TABLE + " WHERE;";
    }

    public static class TracksColumns implements BaseColumns{
        public static final String NAME          = "name";
        public static final String CREATION_TIME = "creationtime";
        static final String CREATION_TIME_TYPE   = "INTEGER NOT NULL";
        static final String NAME_TYPE            = "TEXT";
        static final String _ID_TYPE             = "INTEGER PRIMARY KEY AUTOINCREMENT";
    }

    public static class WaypointsColumns implements android.provider.BaseColumns {
        /** The latitude */
        public static final String LATITUDE = "latitude";
        /** The longitude */
        public static final String LONGITUDE = "longitude";
        /** The recorded time */
        public static final String TIME = "time";
        /** The min in meters per second */
        public static final String SPEED = "min";
        /** The segment _id to which this session belongs */
        public static final String SESSION = "session";
        /** The accuracy of the fix */
        public static final String ACCURACY = "accuracy";
        /** The altitude */
        public static final String ALTITUDE = "altitude";
        /** the bearing of the fix */
        public static final String BEARING = "bearing";

        static final String LATITUDE_TYPE  = "REAL NOT NULL";
        static final String LONGITUDE_TYPE = "REAL NOT NULL";
        static final String TIME_TYPE      = "INTEGER NOT NULL";
        static final String SPEED_TYPE     = "REAL NOT NULL";
        static final String SESSION_TYPE   = "INTEGER NOT NULL";
        static final String ACCURACY_TYPE  = "REAL";
        static final String ALTITUDE_TYPE  = "REAL";
        static final String BEARING_TYPE   = "REAL";
        static final String _ID_TYPE       = "INTEGER PRIMARY KEY AUTOINCREMENT";
    }

    public static class SessionTrackColumns implements Base {
        /** The segment _id to which this session belongs */
        public static final String TRACK = "session";
        static final String TRACK_TYPE   = "INTEGER NOT NULL";

        public static final String CREATION_TIME = "creationtime";
        static final String CREATION_TIME_TYPE   = "INTEGER NOT NULL";
    }

    public interface Base extends BaseColumns {
        public static final String _ID_TYPE       = "INTEGER PRIMARY KEY AUTOINCREMENT";
    }

}
