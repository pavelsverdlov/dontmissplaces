package svp.com.dontmissplaces.ui.activities;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;

import butterknife.Bind;
import butterknife.ButterKnife;
import svp.com.dontmissplaces.R;
import svp.com.dontmissplaces.presenters.HistoryTracksPresenter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.TextView;

import com.svp.infrastructure.mvpvs.view.AppCompatActivityView;

import java.util.HashSet;

public class HistoryTracksActivity extends AppCompatActivityView<HistoryTracksPresenter> {

    public static class ViewState extends com.svp.infrastructure.mvpvs.viewstate.ViewState<HistoryTracksActivity> {

        public ViewState(HistoryTracksActivity view) {
            super(view);
        }

        @Override
        protected void restore() {

        }

        @Override
        protected Activity getActivity() {
            return view;
        }
    }


    @Bind(R.id.history_tracks_recycler_view) RecyclerView tracksView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_tracks);

        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.history_tracks_toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.history_tracks_fab);
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
        tracksView.setHasFixedSize(true);

        // use a linear layout manager
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        tracksView.setLayoutManager(mLayoutManager);

        RecyclerView.Adapter mAdapter = new HistoryListAdapter(new String[] {"fd" });
        tracksView.setAdapter(mAdapter);

        tracksView.setClickable(true);

        HistoryTracksPresenter.HistoryCursorAdapter cursorAdapter = new HistoryTracksPresenter.HistoryCursorAdapter(this, getPresenter().getCursorTracks());

    }

    public class HistoryListAdapter extends RecyclerView.Adapter<HistoryListAdapter.ViewHolder> {
        private String[] mDataset;
        private HashSet<Integer> selected;
        // Provide a reference to the views for each data item
        // Complex data items may need more than one view per item, and
        // you provide access to all the views for a data item in a view holder
        public class ViewHolder extends RecyclerView.ViewHolder {
            // each data item is just a string in this case
            public TextView mTextView;
            public View card;
            public int position;

            public ViewHolder(View v) {
                super(v);
                this.card = v;
                this.card.setClickable(true);
                mTextView = (TextView)v.findViewById(R.id.history_tracks_item_text);
                this.card.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(selected.contains(position)){
                            selected.remove(position);
                        }else{
                            selected.add(position);
                            v.setSelected(true);
                        }
                    }
                });
            }
        }

        // Provide a suitable constructor (depends on the kind of dataset)
        public HistoryListAdapter(String[] myDataset) {
            mDataset = myDataset;
            selected = new HashSet<>();
        }

        // Create new views (invoked by the layout manager)
        @Override
        public HistoryListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {
            // create a new view
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.activity_history_tracks_item_template, parent, false);
            // set the view's size, margins, paddings and layout parameters

            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            // - get element from your dataset at this position
            // - replace the contents of the view with that element
            holder.mTextView.setText(mDataset[position]);
            holder.card.setSelected(selected.contains(position));
            holder.position = position;
        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return mDataset.length;
        }
    }
}
