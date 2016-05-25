package svp.com.dontmissplaces.ui.model;

import java.util.Vector;

import svp.com.dontmissplaces.db.SessionTrack;
import svp.com.dontmissplaces.db.Waypoint;

/**
 * Created by Pasha on 5/17/2016.
 */
public class SessionView {
    private final SessionTrack session;
    public final Vector<Waypoint> waypoints;

    public SessionView(SessionTrack session, Vector<Waypoint> waypoints) {
        this.session = session;
        this.waypoints = waypoints;
    }

    public long getId(){
        return session.id;
    }
}
