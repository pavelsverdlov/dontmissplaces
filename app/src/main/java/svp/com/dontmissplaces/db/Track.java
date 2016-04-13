package svp.com.dontmissplaces.db;

import android.database.Cursor;

public class Track {
    public final long id;
    String name;
    int creationtime;

    public Track(long trackId, String name, long currentTime) {
        id = trackId;
        this.name = name;
        this.creationtime = creationtime;
    }

    public static String getName(Cursor cursor){
        return cursor.getString(cursor.getColumnIndex(DatabaseStructure.TracksColumns.NAME));
    }
}
