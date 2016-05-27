package svp.com.dontmissplaces.ui.model;

import android.database.Cursor;
import android.view.View;
import android.widget.TextView;

import com.svp.infrastructure.common.ViewExtensions;

import svp.com.dontmissplaces.R;
import svp.com.dontmissplaces.db.Place;

public class PlaceView implements ICursorParcelable{
    private TextView title;

    @Override
    public void parse(Cursor cursor) {
        title.setText(Place.getTitle(cursor));
    }

    @Override
    public void initView(View view) {
        title = ViewExtensions.findViewById(view, R.id.history_tracks_item_text);
    }
}
