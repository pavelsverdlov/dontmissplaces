package svp.com.dontmissplaces.ui.model;

import android.database.Cursor;
import android.view.View;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import svp.com.dontmissplaces.R;

/**
 * Created by Pasha on 4/11/2016.
 */
public class TrackView {
    @Bind(R.id.history_tracks_item_text) TextView title;

    public void parse(Cursor cursor) {
        //title =
    }

    public void initView(View view) {
        ButterKnife.bind(view);
    }
}
