package svp.com.dontmissplaces.db;

import android.database.Cursor;

import java.io.Serializable;
import java.util.UUID;

public class Track extends dto {
    public static final String KEY = UUID.randomUUID().toString();
    public final String name;
    int creationtime;

    public Track(long trackId, String name, long currentTime) {
        super(trackId);
        this.name = name;
        this.creationtime = creationtime;
    }

    public Track(Cursor cursor){
        super(getId(cursor));
        name = getName(cursor);
    }

    public static String getName(Cursor cursor){
        return cursor.getString(cursor.getColumnIndex(DatabaseStructure.Tracks.NAME));
    }
    public static long getId(Cursor cursor){
        return cursor.getLong(cursor.getColumnIndex(DatabaseStructure.Tracks._ID));
    }
}
