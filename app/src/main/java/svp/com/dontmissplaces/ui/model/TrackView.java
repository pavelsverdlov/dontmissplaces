package svp.com.dontmissplaces.ui.model;

import android.database.Cursor;
import android.view.View;
import android.widget.TextView;

import com.svp.infrastructure.common.ViewExtensions;

import java.util.Collection;
import java.util.Vector;

import svp.com.dontmissplaces.R;
import svp.com.dontmissplaces.db.SessionTrack;
import svp.com.dontmissplaces.db.Track;

public class TrackView {
    TextView title;

    public long id;
    private CharSequence name;
    public final Collection<SessionView> sessions;

    public TrackView(Track recordingTrack, Vector<SessionView> sessions) {
        id = recordingTrack.id;
        name = recordingTrack.name;
        this.sessions = sessions;
    }
    public TrackView(){
        sessions = new Vector<>();
    }

    public void parse(Cursor cursor) {
        name = Track.getName(cursor);
        title.setText(name);
        id = Track.getId(cursor);
    }

    public void initView(View view) {
        title = ViewExtensions.findViewById(view,R.id.history_tracks_item_text);
    }

    public CharSequence getHeader() {
        return name;
    }
}
