package svp.com.dontmissplaces.db;

import android.database.Cursor;

/**
 * Created by Pasha on 5/16/2016.
 */
public class SessionTrack extends dto {
    public final long trackId;
    public final long creationtime;

    public SessionTrack(long id, long trackId, long time) {
        super(id);
        this.trackId = trackId;
        creationtime = time;
    }

    public SessionTrack(Cursor cursor) {
        super(cursor.getLong(cursor.getColumnIndex(DatabaseStructure.Sessions._ID)));
        trackId = cursor.getLong(cursor.getColumnIndex(DatabaseStructure.Sessions.TRACK));
        creationtime = cursor.getLong(cursor.getColumnIndex(DatabaseStructure.Sessions.CREATION_TIME));
    }
}
