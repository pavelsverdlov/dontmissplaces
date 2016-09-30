package svp.com.dontmissstation.ui.activities;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.svp.infrastructure.common.ViewExtensions;
import com.svp.infrastructure.common.view.BaseCursorAdapter;
import com.svp.infrastructure.common.view.ICursorParcelable;
import com.svp.infrastructure.mvpvs.commutate.ActivityOperationItem;
import com.svp.infrastructure.mvpvs.commutate.ICommutativeElement;
import com.svp.infrastructure.mvpvs.view.AppCompatActivityView;

import java.util.Collection;
import java.util.Vector;

import butterknife.Bind;
import butterknife.ButterKnife;
import svp.com.dontmissstation.R;
import svp.com.dontmissstation.presenters.StationListPresenter;
import svp.com.dontmissstation.ui.RecyclerViewEx;
import svp.com.dontmissstation.ui.adapters.RecyclerCursorAdapter;
import svp.com.dontmissstation.ui.model.SubwayLineView;
import svp.com.dontmissstation.ui.model.SubwayStationView;
import svp.com.dontmissstation.ui.model.SubwayView;

public class StationListActivity extends AppCompatActivityView<StationListPresenter>
        implements View.OnClickListener, ICommutativeElement {

    @Override
    public ActivityOperationItem getOperation() {
        return ActivityOperationResult.EditSubwayStation;
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    public static class ViewState extends com.svp.infrastructure.mvpvs.viewstate.ViewState<StationListActivity> {
        public ViewState(StationListActivity view) {
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

    public class SubwayStationCursorAdapter extends BaseCursorAdapter<SubwayStationCursorView> {
        private int index;
        public SubwayStationCursorAdapter(Context context, Cursor c) {
            super(context, c);
            index = -1;
        }

        @Override
        public ICursorParcelable createParcelableObject() {
            ++index;
            return new SubwayStationCursorView(StationListActivity.this.stations.get(index));
        }

        @Override
        public View getView(LayoutInflater inflater, ViewGroup parent) {
            return inflater.inflate(R.layout.subway_station_template, parent, false);
        }

        @Override
        public void onItemClick(View view, SubwayStationCursorView item) {
            //StationListActivity.this.getPresenter().subwaySelected(item.getSubway());
        }
        @Override
        public int getCount() {
            return StationListActivity.this.stations.size();
        }
    }
    public static class SubwayStationCursorView implements ICursorParcelable {
        private final SubwayStationView stationView;
        private TextView name;
        private TextView lineView;
        private int position;
        private View view;

        public SubwayStationCursorView(SubwayStationView subwayView) {
            this.stationView = subwayView;
        }

        public void parse(Cursor cursor) {
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    EditSubwayLineScrollingActivity.this
//                            .getPresenter().openEditStationActivity(stations.get(position));
//                }
//            });

            name.setText(stationView.getName());
            SubwayLineView line = stationView.getLines().get(0);
            lineView.setText(line.getName());
            lineView.setBackgroundColor(line.getColor());
        }

        @Override
        public void initView(View view) {
            this.view = view;
            name = ViewExtensions.findViewById(view,R.id.activity_add_new_subway_station_name_textview);
            lineView = ViewExtensions.findViewById(view,R.id.activity_add_new_subway_line_template_line_name);
        }

        public SubwayStationView getSubway() {
            return stationView;
        }
    }

    @Bind(R.id.activity_station_list_recyclerView) RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_station_toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.activity_statoin_fab_addnew);
        fab.setOnClickListener(this);

        RecyclerViewEx.setCustomSettings(recyclerView,this);
    }
    Vector<SubwayStationView> stations;

    @Override
    public void onStart() {
        super.onStart();
        stations = getPresenter().getStation();
        RecyclerCursorAdapter mAdapter = new RecyclerCursorAdapter(this,null,new SubwayStationCursorAdapter(this, null));
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onClick(View v) {

    }

}
