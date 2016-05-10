package svp.com.dontmissplaces.db;

import android.database.Cursor;

import java.io.Serializable;
import java.util.UUID;

public class Track implements Serializable {
    public static final String KEY = UUID.randomUUID().toString();
    public final long id;
    public final String name;
    int creationtime;

    public Track(long trackId, String name, long currentTime) {
        id = trackId;
        this.name = name;
        this.creationtime = creationtime;
    }

    public Track(Cursor cursor){
        id = getId(cursor);
        name = getName(cursor);
    }

    public static String getName(Cursor cursor){
        return cursor.getString(cursor.getColumnIndex(DatabaseStructure.TracksColumns.NAME));
    }
    public static long getId(Cursor cursor){
        return cursor.getLong(cursor.getColumnIndex(DatabaseStructure.TracksColumns._ID));
    }
}
