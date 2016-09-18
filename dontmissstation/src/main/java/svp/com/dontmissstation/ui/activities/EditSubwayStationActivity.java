package svp.com.dontmissstation.ui.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.svp.infrastructure.mvpvs.commutate.ActivityOperationItem;
import com.svp.infrastructure.mvpvs.commutate.ICommutativeElement;
import com.svp.infrastructure.mvpvs.view.AppCompatActivityView;

import java.util.Vector;

import butterknife.Bind;
import butterknife.ButterKnife;
import svp.app.map.model.Point2D;
import svp.com.dontmissstation.R;
import svp.com.dontmissstation.presenters.EditSubwayStationPresenter;
import svp.com.dontmissstation.ui.model.SubwayStationView;
import svp.com.dontmissstation.ui.model.SubwayLineView;

public class EditSubwayStationActivity extends AppCompatActivityView<EditSubwayStationPresenter> implements ICommutativeElement, View.OnClickListener {

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

    @Bind(R.id.activity_edit_subway_station_lines) Spinner linesSpinner;
    @Bind(R.id.activity_edit_subway_station_name_edittext) TextView nameView;
    @Bind(R.id.activity_edit_subway_station_latitude_edittext) TextView latitudeView;
    @Bind(R.id.activity_edit_subway_station_longitude_edittext) TextView longitudeView;
    @Bind(R.id.activity_edit_subway_station_pick_point_btn) Button pickPointBtn;

    private SubwayStationView station;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_station);

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
    public void onStart(){
        super.onStart();
        station = getPresenter().getStation();

        nameView.setText(station.getName());
        Point2D coor = station.getCoordinate();
        longitudeView.setText(String.valueOf(coor.longitude));
        latitudeView.setText(String.valueOf(coor.latitude));

        Vector<String> lines = new Vector<String>();
        for (SubwayLineView line : getPresenter().getAvailableLines()) {
            lines.add(line.getName());
        }
        linesSpinner.setAdapter(new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,lines));
    }
}