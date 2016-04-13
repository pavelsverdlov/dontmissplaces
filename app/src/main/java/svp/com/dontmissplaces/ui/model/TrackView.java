package svp.com.dontmissplaces.ui.model;

import android.database.Cursor;
import android.view.View;
import android.widget.TextView;

import com.svp.infrastructure.common.ViewExtensions;

import svp.com.dontmissplaces.R;
import svp.com.dontmissplaces.db.Track;

public class TrackView {
    TextView title;

    public void parse(Cursor cursor) {
        title.setText(Track.getName(cursor));
    }

    public void initView(View view) {
        title = ViewExtensions.findViewById(view,R.id.history_tracks_item_text);
    }
}
