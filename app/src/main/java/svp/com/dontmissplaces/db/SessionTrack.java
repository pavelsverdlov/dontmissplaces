package svp.com.dontmissplaces.db;

/**
 * Created by Pasha on 5/16/2016.
 */
public class SessionTrack extends dto {
    public final long trackId;

    public SessionTrack(long id, long trackId) {
        super(id);
        this.trackId = trackId;
    }
}
