package svp.com.dontmissstation.ui.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.svp.infrastructure.mvpvs.commutate.ActivityOperationItem;
import com.svp.infrastructure.mvpvs.commutate.ICommutativeElement;
import com.svp.infrastructure.mvpvs.view.AppCompatActivityView;

import java.util.Vector;

import butterknife.Bind;
import butterknife.ButterKnife;
import svp.app.map.model.Point2D;
import svp.com.dontmissstation.R;
import svp.com.dontmissstation.presenters.EditSubwayStationPresenter;
import svp.com.dontmissstation.ui.activities.edit.subway.station.ConnectStationsAdapter;
import svp.com.dontmissstation.ui.model.LineUIView;
import svp.com.dontmissstation.ui.model.SubwayStationView;
import svp.com.dontmissstation.ui.model.SubwayLineView;

public class EditSubwayStationActivity extends AppCompatActivityView<EditSubwayStationPresenter>
        implements ICommutativeElement, View.OnClickListener, ConnectStationsAdapter.OnClickListener {

    @Override
    public ActivityOperationItem getOperation() {
        return ActivityOperationResult.EditSubwayStation;
    }

    @Override
    public Activity getActivity() {
        return this;
    }


    public static class ViewState extends com.svp.infrastructure.mvpvs.viewstate.ViewState<EditSubwayStationActivity> {
        public ViewState(EditSubwayStationActivity view) {
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

//    @Bind(R.id.activity_edit_subway_station_lines) Spinner linesSpinner;
    @Bind(R.id.activity_edit_subway_station_lines) LinearLayout linesLayout;
    @Bind(R.id.activity_edit_subway_station_name_edittext) TextView nameView;
    @Bind(R.id.activity_edit_subway_station_latitude_edittext) TextView latitudeView;
    @Bind(R.id.activity_edit_subway_station_longitude_edittext) TextView longitudeView;
    @Bind(R.id.activity_edit_subway_station_pick_point_btn) Button pickPointBtn;
    @Bind(R.id.activity_edit_subway_station_connect_stations) ListView connectStationsView;

    private SubwayStationView station;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_station);

        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_route_toolbar_id);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.activity_route_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        pickPointBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activity_edit_subway_station_pick_point_btn:
                getPresenter().openMapActivity();
                break;
        }
    }
    @Override
    public void onClick(ConnectStationsAdapter.ConnectStation station) {

    }

    @Override
    public void onStart(){
        super.onStart();
        station = getPresenter().getStation();

        nameView.setText(station.getName());
        Point2D coor = station.getCoordinate();
        longitudeView.setText(String.valueOf(coor.longitude));
        latitudeView.setText(String.valueOf(coor.latitude));
        linesLayout.removeAllViews();
        for (SubwayLineView line : station.getLines()) {
            LineUIView linev = new LineUIView(this, line);
            linev.addTo(linesLayout);
        }

        Vector<ConnectStationsAdapter.ConnectStation> sts = new Vector<>();
        for (SubwayStationView s : station.getPrevs()){
            sts.add(ConnectStationsAdapter.ConnectStation.getPrev(s));
        }
        for (SubwayStationView s : station.getNexts()){
            sts.add(ConnectStationsAdapter.ConnectStation.getNext(s));
        }
        ConnectStationsAdapter adapter = new ConnectStationsAdapter(this.getLayoutInflater(), sts);
        adapter.setOnClickListener(this);
        connectStationsView.setAdapter(adapter);

//        Vector<String> lines = new Vector<String>();
//        for (SubwayLineView line : getPresenter().getAvailableLines()) {
//            lines.add(line.getName());
//        }
//        linesSpinner.setAdapter(new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,lines));
    }
}
