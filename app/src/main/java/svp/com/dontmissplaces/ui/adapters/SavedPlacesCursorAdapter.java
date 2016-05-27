package svp.com.dontmissplaces.ui.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import svp.com.dontmissplaces.R;
import svp.com.dontmissplaces.ui.model.ICursorParcelable;
import svp.com.dontmissplaces.ui.model.PlaceView;

public class SavedPlacesCursorAdapter extends BaseCursorAdapter {
    public SavedPlacesCursorAdapter(Context context, Cursor c) {
        super(context, c);
    }

    @Override
    public ICursorParcelable createParcelableObject() {
        return new PlaceView();
    }

    @Override
    public View getView(LayoutInflater inflater, ViewGroup parent) {
        return inflater.inflate(R.layout.activity_history_tracks_item_template, parent, false);
    }

    @Override
    public void onItemClick(View view, ICursorParcelable item) {

    }
}
