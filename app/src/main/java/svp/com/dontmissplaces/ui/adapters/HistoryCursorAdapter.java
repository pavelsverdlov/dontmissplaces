package svp.com.dontmissplaces.ui.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import svp.com.dontmissplaces.R;
import svp.com.dontmissplaces.ui.model.ICursorParcelable;
import svp.com.dontmissplaces.ui.model.TrackView;

public class HistoryCursorAdapter extends BaseCursorAdapter<TrackView> {
    public interface OnItemClickListener{
        void onItemClick(View v, TrackView track);
    }

    private LayoutInflater mInflater;
    private OnItemClickListener clickListener;
    //private final ArrayList<>
    public HistoryCursorAdapter(Context context, Cursor c) {
        super(context, c);
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public ICursorParcelable createParcelableObject() {
        return new TrackView();
    }

    @Override
    public View getView(LayoutInflater inflater, ViewGroup parent) {
        return mInflater.inflate(R.layout.activity_history_tracks_item_template, parent, false);
    }

    @Override
    public void onItemClick(View view, TrackView item) {
        clickListener.onItemClick(view, item);
    }
    /*
        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            final View item = mInflater.inflate(R.layout.activity_history_tracks_item_template, parent, false);
            TrackView track = new TrackView();
            track.initView(item);
            item.setTag(track);

            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TrackView track = (TrackView) v.getTag();
                    clickListener.onItemClick(v, track);
                }
            });
            return item;
        }

        @Override
        public void bindView(View convertView, Context context, Cursor cursor) {
            TrackView track = (TrackView) convertView.getTag();
            track.parse(cursor);
        }
    */
    public void setOnItemClickListener(OnItemClickListener clickListener){
        this.clickListener = clickListener;
    }
}
