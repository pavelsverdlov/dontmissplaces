package svp.com.dontmissplaces.ui.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;

import butterknife.Bind;
import butterknife.ButterKnife;
import svp.com.dontmissplaces.R;
import svp.com.dontmissplaces.presenters.HistoryTracksPresenter;
import svp.com.dontmissplaces.ui.ActivityCommutator;
import svp.com.dontmissplaces.ui.BaseBundleProvider;
import svp.com.dontmissplaces.ui.adapters.HistoryCursorAdapter;
import svp.com.dontmissplaces.ui.model.TrackView;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.svp.infrastructure.mvpvs.commutate.ActivityOperationItem;
import com.svp.infrastructure.mvpvs.commutate.ICommutativeElement;
import com.svp.infrastructure.mvpvs.view.AppCompatActivityView;

import java.util.HashSet;

public class HistoryTracksActivity extends AppCompatActivityView<HistoryTracksPresenter> implements ICommutativeElement {

    @Override
    public ActivityOperationItem getOperation() {
        return ActivityCommutator.ActivityOperationResult.HistoryTracks;
    }
    @Override
    public Activity getActivity() {
        return this;
    }

    public static class ViewState extends com.svp.infrastructure.mvpvs.viewstate.ViewState<HistoryTracksActivity> {

        public ViewState(HistoryTracksActivity view) {
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


    @Bind(R.id.history_tracks_view) ListView tracksView;

    private HistoryCursorAdapter cursorAdapter;

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
    }

    @Override
    public void onStart(){
        super.onStart();
        cursorAdapter = new HistoryCursorAdapter(this, getPresenter().getCursorTracks());
        cursorAdapter.setOnItemClickListener(new HistoryCursorAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view,TrackView track) {
                view.setSelected(true);
                getPresenter().openTrack(track);
//                Snackbar.make(view, "selected " + track.getHeader().toString(), Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });
        tracksView.setAdapter(cursorAdapter);
    }

}
