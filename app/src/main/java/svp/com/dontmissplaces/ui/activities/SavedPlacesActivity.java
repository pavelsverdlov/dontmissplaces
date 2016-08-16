package svp.com.dontmissplaces.ui.activities;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.svp.infrastructure.common.ViewExtensions;
import com.svp.infrastructure.common.view.BaseCursorAdapter;
import com.svp.infrastructure.common.view.ICursorParcelable;
import com.svp.infrastructure.mvpvs.commutate.ActivityOperationItem;
import com.svp.infrastructure.mvpvs.commutate.ICommutativeElement;
import com.svp.infrastructure.mvpvs.view.AppCompatActivityView;

import butterknife.Bind;
import butterknife.ButterKnife;
import svp.com.dontmissplaces.R;
import svp.com.dontmissplaces.db.Place;
import svp.com.dontmissplaces.model.Map.google.GPlaceAddressDetails;
import svp.com.dontmissplaces.model.nominatim.PlaceAddressDetails;
import svp.com.dontmissplaces.presenters.SavedPlacesPresenter;
import svp.com.dontmissplaces.ui.ActivityCommutator;
import svp.com.dontmissplaces.ui.model.PlaceView;

public class SavedPlacesActivity extends AppCompatActivityView<SavedPlacesPresenter> implements ICommutativeElement {
    @Override
    public ActivityOperationItem getOperation() {
        return ActivityCommutator.ActivityOperationResult.SavedPlaces;
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    public class RecyclerCursorAdapter extends RecyclerView.Adapter<RecyclerCursorAdapter.ViewHolder> {
        private final Context context;
        private final SavedPlacesCursorAdapter cursorAdapter;

        // Provide a reference to the views for each data item
        // Complex data items may need more than one view per item, and
        // you provide access to all the views for a data item in a view holder
        public class ViewHolder extends RecyclerView.ViewHolder {
            // each data item is just a string in this case
            public ViewHolder(View v) {
                super(v);
            }
        }

        // Provide a suitable constructor (depends on the kind of dataset)
        public RecyclerCursorAdapter(Context context, Cursor c) {
            this.context = context;
            cursorAdapter = new SavedPlacesCursorAdapter(context, c);
        }
        // Create new views (invoked by the layout manager)
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = cursorAdapter.newView(context, cursorAdapter.getCursor(), parent);
            return new ViewHolder(v);
            /*
            // create a new view
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.my_text_view, parent, false);
            // set the view's size, margins, paddings and layout parameters
            ViewHolder vh = new ViewHolder(v);
            return vh;
            */
        }
        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            cursorAdapter.getCursor().moveToPosition(position); //EDITED: added this line as suggested in the comments below, thanks :)
            cursorAdapter.bindView(holder.itemView, context, cursorAdapter.getCursor());
//            cursorAdapter.getItem(position);
//            holder.mTextView.setText(mDataset[position]);

        }
        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return cursorAdapter.getCount();
        }
    }
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
            return inflater.inflate(R.layout.activity_saved_places_card_item_template, parent, false);
        }

        @Override
        public void onItemClick(View view, SavePlaceView item) {

        }
    }
    public static class SavePlaceView extends PlaceView implements ICursorParcelable {
        private TextView title;
        private TextView address;
        @Override
        public void parse(Cursor cursor) {
            this.update(new Place(cursor));

            title.setText(getName());
            address.setText(getAddress());
        }

        @Override
        public void initView(View view) {
            title = ViewExtensions.findViewById(view, R.id.saved_places_item_place_name);
            address = ViewExtensions.findViewById(view, R.id.saved_places_item_place_address);
        }
    }

    public static class ViewState extends com.svp.infrastructure.mvpvs.viewstate.ViewState<SavedPlacesActivity> {

        public ViewState(SavedPlacesActivity view) {
            super(view);
        }


        @Override
        protected void restore() {

        }

        @Override
        public void saveState() {

        }

        @Override
        public Activity getActivity() {
            return view;
        }
    }

    @Bind(R.id.saved_places_recycler_view) RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_places);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.select_place_save_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);
        // use a linear layout manager
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);

    }

    @Override
    public void onStart() {
        super.onStart();
        RecyclerCursorAdapter mAdapter = new RecyclerCursorAdapter(this, getPresenter().getSavedPlace());
        recyclerView.setAdapter(mAdapter);
    }


}
