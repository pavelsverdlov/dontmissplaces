package svp.com.dontmissplaces.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import java.util.Date;
import java.util.Vector;

import svp.com.dontmissplaces.db.DatabaseStructure.*;

public class Repository extends SQLiteOpenHelper {
    public static final String dbname = "dmpdb";
    private static final int dbversion = 1;
    private static String TAG = Repository.class.getName();

    public final TrackRepository Track;
    public final PlaceRepository Place;

    public Repository(Context context) {
        super(context, dbname, null, dbversion);
        Track = new TrackRepository();
        Place = new PlaceRepository();
    }

    public void vacuum() {
        new Thread() {
            @Override
            public void run() {
                SQLiteDatabase sqldb = getWritableDatabase();
                sqldb.execSQL("VACUUM");
            }
        }.start();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //db.execSQL("DROP TABLE " + Tracks.TABLE + "," + Sessions.TABLE + "," + Waypoints.TABLE);
        db.execSQL(Tracks.CREATE_STATEMENT);
        db.execSQL(Sessions.CREATE_STATEMENT);
        db.execSQL(Waypoints.CREATE_STATEMENT);

        db.execSQL(Places.CREATE_STATEMENT);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    @Override
    public SQLiteDatabase getWritableDatabase() {
        SQLiteDatabase sqldb = super.getWritableDatabase();
        sqldb.execSQL("PRAGMA foreign_keys = ON;");
        return sqldb;
    }

    public class TrackRepository{
        public Track getOrInsertTrack(Track track) {
            Track got = getTrack(track.id);
            if (got == null) {
                got = insertTrack(track.name);
            }
            return got;
        }
        public Track insertTrack(String name) {
            long currentTime = new Date().getTime();

            ContentValues args = new ContentValues();
            args.put(Tracks.NAME, name);
            args.put(Tracks.CREATION_TIME, currentTime);

            SQLiteDatabase sqldb = getWritableDatabase();
            long trackId = sqldb.insert(Tracks.TABLE, null, args);

            return new Track(trackId, name, currentTime);
        }
        public Track getTrack(long id) {
            SQLiteDatabase sqldb = getReadableDatabase();
            try (Cursor cursor = sqldb.query(Tracks.TABLE, null, Tracks._ID + "= ?", new String[]{String.valueOf(id)},
                    null, null, null, null)) {
                if (cursor.moveToFirst()) {
                    return new Track(cursor);
                }
            }
            return null;
        }
        public Cursor getCursorTracks() {
            SQLiteDatabase sqldb = getReadableDatabase();
            return sqldb.rawQuery(Tracks.SELECT_ALL, null);
        }
        int updateTrack(long trackId, String name) {
            int updates;
            String whereclause = Tracks._ID + " = " + trackId;
            ContentValues args = new ContentValues();
            args.put(Tracks.NAME, name);

            // Execute the query.
            SQLiteDatabase mDb = getWritableDatabase();
            updates = mDb.update(Tracks.TABLE, args, whereclause, null);

            return updates;
        }

        public int deleteTrack(Track track) {
            SQLiteDatabase sqldb = getWritableDatabase();
            int affected = 0;
            try {
                sqldb.beginTransaction();

                affected += sqldb.delete(Tracks.TABLE, Tracks._ID + "= ?", new String[]{String.valueOf(track.id)});

                sqldb.setTransactionSuccessful();
            } finally {
                if (sqldb.inTransaction()) {
                    sqldb.endTransaction();
                }
            }
            return affected;
        }
        public void clearTracks() {
            SQLiteDatabase sqldb = getWritableDatabase();
            sqldb.execSQL(Tracks.DELETE_ALL);
        }
        public void insertWaypoint(Waypoint waypoint) {
            if (waypoint.session < 0) {
                throw new IllegalArgumentException("Track may not the less then 0.");
            }
            SQLiteDatabase sqldb = getWritableDatabase();

            ContentValues args = new ContentValues();
            args.put(Waypoints.SESSION, waypoint.session);
            args.put(Waypoints.TIME, waypoint.getTime());
            args.put(Waypoints.LATITUDE, waypoint.getLatitude());
            args.put(Waypoints.LONGITUDE, waypoint.getLongitude());
            args.put(Waypoints.SPEED, waypoint.speed);
            args.put(Waypoints.ACCURACY, waypoint.getAccuracy());
            args.put(Waypoints.ALTITUDE, waypoint.getAltitude());
            args.put(Waypoints.BEARING, waypoint.getBearing());

            waypoint.id = sqldb.insert(Waypoints.TABLE, null, args);
        }
        public Vector<Waypoint> getWaypoints(SessionTrack session) {
            Vector<Waypoint> waypoints = new Vector<>();

            SQLiteDatabase sqldb = getReadableDatabase();

            try (Cursor cursor = sqldb.query(Waypoints.TABLE, null, Waypoints.SESSION + "= ?", new String[]{String.valueOf(session.id)},
                    null, null, null, null)) {
                if (cursor.moveToFirst()) {
                    do {
                        waypoints.add(new Waypoint(session.id, cursor));
                    } while (cursor.moveToNext());
                }
            }
            return waypoints;
        }

        public SessionTrack insertSession(Track track) {
            long currentTime = new Date().getTime();

            ContentValues args = new ContentValues();
            args.put(Sessions.TRACK, track.id);
            args.put(Sessions.CREATION_TIME, currentTime);

            SQLiteDatabase sqldb = getWritableDatabase();
            long sId = sqldb.insert(Sessions.TABLE, null, args);

            return new SessionTrack(sId, track.id,currentTime);
        }
        public Vector<SessionTrack> getSessions(Track track) {
            Vector<SessionTrack> sessions = new Vector<>();
            try(Cursor cursor = getCursorById(Sessions.TABLE,track.id)){
                if (cursor == null){
                    return sessions;
                }
                do{
                    sessions.add(new SessionTrack(cursor));
                }while (cursor.moveToFirst());
            }
            return sessions;
        }
    }

    public class PlaceRepository{
        public Place insert(Place place){
            place.creationTime = new Date().getTime();

            ContentValues args = new ContentValues();
            args.put(Places.ADDRESS, place.address);
            args.put(Places.DESCRIPTION, place.description);
            args.put(Places.GOOGLE_PLACE_ID, place.googlePlaceId);
            args.put(Places.LATITUDE, place.latitude);
            args.put(Places.LONGITUDE, place.longitude);
            args.put(Places.TITLE, place.title);
            args.put(Places.PLACETYPE, place.placeType);
            args.put(Places.CREATION_TIME, place.creationTime);

            SQLiteDatabase sqldb = getWritableDatabase();
            place.id = sqldb.insert(Places.TABLE, null, args);

            return place;
        }
        public int delete(Place place){
            SQLiteDatabase sqldb = getWritableDatabase();
            return sqldb.delete(Places.TABLE, Places._ID + "= ?", new String[]{String.valueOf(place.id)});
        }
        public Vector<Place> getAll() {
            Vector<Place> places = new Vector<>();
            SQLiteDatabase sqldb = getReadableDatabase();
            try(Cursor cursor = sqldb.rawQuery(Tracks.SELECT_ALL, null)){
                while (cursor.moveToNext()){
                    places.add(new Place(cursor));
                }
            }
            return places;
        }
    }



    private Cursor getCursorById(String table,long id) {
        SQLiteDatabase sqldb = getReadableDatabase();
        Cursor cursor = sqldb.query(
                table, null,
                BaseColumns._ID + "= ?", new String[]{String.valueOf(id)},
                null, null, null, null);
        if (cursor.moveToFirst()) {
            return cursor;
        }
        return null;
    }
}

