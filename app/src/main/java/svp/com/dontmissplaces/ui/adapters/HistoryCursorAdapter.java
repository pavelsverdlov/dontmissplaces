package svp.com.dontmissplaces.ui.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import svp.com.dontmissplaces.R;
import svp.com.dontmissplaces.ui.model.TrackView;

public class HistoryCursorAdapter extends CursorAdapter {
    private LayoutInflater mInflater;
    public HistoryCursorAdapter(Context context, Cursor c) {
        super(context, c);
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View item = mInflater.inflate(R.layout.activity_history_tracks_item_template, parent, false);
        TrackView track = new TrackView();
        track.initView(item);
        item.setTag(track);
        return item;
    }

    @Override
    public void bindView(View convertView, Context context, Cursor cursor) {
        TrackView track = (TrackView) convertView.getTag();
        track.parse(cursor);
    }
}
