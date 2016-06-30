package svp.com.dontmissplaces.ui.activities;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.svp.infrastructure.common.ViewExtensions;
import com.svp.infrastructure.common.view.BaseCursorAdapter;
import com.svp.infrastructure.common.view.ICursorParcelable;

import svp.com.dontmissplaces.R;
import svp.com.dontmissplaces.db.Place;
import svp.com.dontmissplaces.ui.model.PlaceView;

public class SavedPlacesActivity extends AppCompatActivity {
    public static class SavedPlacesCursorAdapter extends BaseCursorAdapter<SavePlaceView> {
        public SavedPlacesCursorAdapter(Context context, Cursor c) {
            super(context, c);
        }

        @Override
        public ICursorParcelable createParcelableObject() {
            return new SavePlaceView();
        }

        @Override
        public View getView(LayoutInflater inflater, ViewGroup parent) {
            return inflater.inflate(R.layout.activity_history_tracks_item_template, parent, false);
        }

        @Override
        public void onItemClick(View view, SavePlaceView item) {

        }
    }
    public static class SavePlaceView extends PlaceView implements ICursorParcelable {
        @Override
        public void parse(Cursor cursor) {
            title.setText(Place.getTitle(cursor));
        }

        @Override
        public void initView(View view) {
            title = ViewExtensions.findViewById(view, R.id.history_tracks_item_text);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_places);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.select_place_save_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
