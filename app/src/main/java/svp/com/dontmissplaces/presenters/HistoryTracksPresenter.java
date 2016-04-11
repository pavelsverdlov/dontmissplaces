package svp.com.dontmissplaces.presenters;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.svp.infrastructure.mvpvs.presenter.Presenter;

import svp.com.dontmissplaces.R;
import svp.com.dontmissplaces.db.Repository;
import svp.com.dontmissplaces.db.Track;
import svp.com.dontmissplaces.ui.activities.HistoryTracksActivity;
import svp.com.dontmissplaces.ui.model.TrackView;

/**
 * Created by Pasha on 4/11/2016.
 */
public class HistoryTracksPresenter extends Presenter<HistoryTracksActivity,HistoryTracksActivity.ViewState> {

    private final Repository repository;

    public HistoryTracksPresenter(Repository repository) {
        this.repository = repository;
    }

    public Cursor getCursorTracks(){
        return null;//repository.getTracks();
    }

    public static class HistoryCursorAdapter extends CursorAdapter {
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
//            holder.text = (TextView) item.findViewById(R.id.text);
            item.setTag(track);
            return item;
        }

        @Override
        public void bindView(View convertView, Context context, Cursor cursor) {
            TrackView track = (TrackView) convertView.getTag();
            track.parse(cursor);
//            holder.text.setText(cursor.getString(0));
        }
    }
}
