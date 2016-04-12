package svp.com.dontmissplaces.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.Date;

import svp.com.dontmissplaces.db.DatabaseStructure.Tracks;
import svp.com.dontmissplaces.db.DatabaseStructure.Waypoints;
import svp.com.dontmissplaces.db.DatabaseStructure.WaypointsColumns;
import svp.com.dontmissplaces.db.DatabaseStructure.TracksColumns;

/**
 * Created by Pasha on 4/2/2016.
 */
public class Repository extends SQLiteOpenHelper {
    private static final String dbname ="";
    private static final int dbversion =1;
    private static String TAG = Repository.class.getName();
    public Repository(Context context){
        super(context,dbname,null,dbversion);
    }

    public void vacuum() {
        new Thread() {
            @Override
            public void run(){
                SQLiteDatabase sqldb = getWritableDatabase();
                sqldb.execSQL("VACUUM");
            }
        }.start();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Tracks.CREATE_STATEMENT);
        db.execSQL(Waypoints.CREATE_STATEMENT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    void insertWaypoint(Waypoint waypoint) {
        if (waypoint.track < 0){
            throw new IllegalArgumentException("Track may not the less then 0.");
        }
        SQLiteDatabase sqldb = getWritableDatabase();

        ContentValues args = new ContentValues();
        args.put(WaypointsColumns.TRACK, waypoint.track);
        args.put(WaypointsColumns.TIME, waypoint.getTime());
        args.put(WaypointsColumns.LATITUDE, waypoint.getLatitude());
        args.put(WaypointsColumns.LONGITUDE, waypoint.getLongitude());
        args.put(WaypointsColumns.SPEED, waypoint.speed);
        args.put(WaypointsColumns.ACCURACY, waypoint.getAccuracy());
        args.put(WaypointsColumns.ALTITUDE, waypoint.getAltitude());
        args.put(WaypointsColumns.BEARING, waypoint.getBearing());

        waypoint.id = sqldb.insert(Waypoints.TABLE, null, args);
    }
    int deleteTrack(long trackId)
    {
        /*
        SQLiteDatabase sqldb = getWritableDatabase();
        int affected = 0;
        Cursor cursor = null;
        long segmentId = -1;
        long metadataId = -1;

        try
        {
            sqldb.beginTransaction();
            // Iterate on each segement to delete each
            cursor = sqldb.query(Segments.TABLE, new String[] { Segments._ID }, Segments.TRACK + "= ?", new String[] { String.valueOf(trackId) }, null, null,
                    null, null);
            if (cursor.moveToFirst())
            {
                do
                {
                    segmentId = cursor.getLong(0);
                    affected += deleteSegment(sqldb, trackId, segmentId);
                }
                while (cursor.moveToNext());
            }
            else
            {
                Log.e(TAG, "Did not find the last active segment");
            }
            // Delete the track
            affected += sqldb.delete(Tracks.TABLE, Tracks._ID + "= ?", new String[] { String.valueOf(trackId) });
            // Delete remaining meta-data
            affected += sqldb.delete(MetaData.TABLE, MetaData.TRACK + "= ?", new String[] { String.valueOf(trackId) });

            cursor = sqldb.query(MetaData.TABLE, new String[] { MetaData._ID }, MetaData.TRACK + "= ?", new String[] { String.valueOf(trackId) }, null, null,
                    null, null);
            if (cursor.moveToFirst())
            {
                do
                {
                    metadataId = cursor.getLong(0);
                    affected += deleteMetaData(metadataId);
                }
                while (cursor.moveToNext());
            }

            sqldb.setTransactionSuccessful();
        }
        finally
        {
            if (cursor != null)
            {
                cursor.close();
            }
            if (sqldb.inTransaction())
            {
                sqldb.endTransaction();
            }
        }

        return affected;
        */
        return 1;
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

    Track inserTrack(String name){
        long currentTime = new Date().getTime();
        ContentValues args = new ContentValues();
        args.put(TracksColumns.NAME, name);
        args.put(TracksColumns.CREATION_TIME, currentTime);

        SQLiteDatabase sqldb = getWritableDatabase();
        long trackId = sqldb.insert(Tracks.TABLE, null, args);

        return new Track(trackId, name, currentTime);
    }

    public Cursor getCursorTracks() {
        SQLiteDatabase sqldb = getWritableDatabase();
        return sqldb.rawQuery(Tracks.SELECT_ALL, null);
    }
}
