package svp.com.dontmissstation.ui.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.svp.infrastructure.mvpvs.commutate.ActivityOperationItem;
import com.svp.infrastructure.mvpvs.commutate.ICommutativeElement;
import com.svp.infrastructure.mvpvs.view.AppCompatActivityView;

import java.util.Vector;

import butterknife.Bind;
import butterknife.ButterKnife;
import svp.com.dontmissstation.R;
import svp.com.dontmissstation.presenters.EditSubwayStationPresenter;
import svp.com.dontmissstation.ui.model.StationView;

public class EditSubwayStationActivity extends AppCompatActivityView<EditSubwayStationPresenter> implements ICommutativeElement {



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

    private StationView station;

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

    }

    @Override
    public void onStart(){
        super.onStart();
        station = getPresenter().getStation();
        Vector<String> lines = new Vector<String>();
        lines.add("U1");
        lines.add("U2");
        lines.add("U3");
        // Specify the layout to use when the list of choices appears
//        linesSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        linesSpinner.setAdapter(new ArrayAdapter<String>(this,0,lines));
    }
}
