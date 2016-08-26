package svp.com.dontmissstation.ui.activities;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.svp.infrastructure.mvpvs.commutate.ActivityOperationItem;
import com.svp.infrastructure.mvpvs.commutate.ICommutativeElement;
import com.svp.infrastructure.mvpvs.view.AppCompatActivityView;

import java.util.Collection;
import java.util.Vector;

import butterknife.Bind;
import butterknife.ButterKnife;
import svp.com.dontmissstation.R;
import svp.com.dontmissstation.presenters.AddSubwayLinePresenter;
import svp.com.dontmissstation.ui.RecyclerViewEx;
import svp.com.dontmissstation.ui.model.*;

public class AddSubwayLineActivity extends AppCompatActivityView<AddSubwayLinePresenter> implements ICommutativeElement {

    @Override
    public ActivityOperationItem getOperation() {
        return ActivityOperationResult.AddSubwayLine;
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    public static class ViewState extends com.svp.infrastructure.mvpvs.viewstate.ViewState<AddSubwayLineActivity> {

        public ViewState(AddSubwayLineActivity view) {
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

//        public ActivityPermissions getPermissions() {
//            return view.permissions;
//        }
    }

    public class StationsRecyclerAdapter extends RecyclerView.Adapter<StationsRecyclerAdapter.ViewHolder> {
        private final Context context;
        private final Vector<StationView> lines;

        public class ViewHolder extends RecyclerView.ViewHolder {
            public ViewHolder(View v) {
                super(v);
                ((TextView)v.findViewById(R.id.activity_add_new_subway_line_template_startStation))
                        .setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        });
            }
        }
        public StationsRecyclerAdapter(Context context, Collection<StationView> collection) {
            this.context = context;
            lines = new Vector<>(collection);
        }
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.activity_add_new_subway_line_template, parent, false);
            ViewHolder vh = new ViewHolder(v);
            return vh;
        }
        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
//            holder.mTextView.setText(mDataset[position]);
        }
        @Override
        public int getItemCount() {
            return lines.size();
        }
    }

    @Bind(R.id.add_new_subway_actv_stations) RecyclerView subwayStationsRecyclerView;

    private StationsRecyclerAdapter stationsAdapter;
    private SubwayLineView lineView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_subway_line);

        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        RecyclerViewEx.setCustomSettings(subwayStationsRecyclerView,this);
    }

    public void onStart(){
        super.onStart();
        lineView = getPresenter().getLine();
        stationsAdapter = new StationsRecyclerAdapter(this,lineView.getStations());
        subwayStationsRecyclerView.setAdapter(stationsAdapter);
    }


}
