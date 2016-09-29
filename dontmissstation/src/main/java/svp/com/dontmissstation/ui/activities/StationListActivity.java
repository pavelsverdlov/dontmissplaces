package svp.com.dontmissstation.ui.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.svp.infrastructure.mvpvs.commutate.ActivityOperationItem;
import com.svp.infrastructure.mvpvs.commutate.ICommutativeElement;
import com.svp.infrastructure.mvpvs.view.AppCompatActivityView;

import java.util.Collection;

import svp.com.dontmissstation.R;
import svp.com.dontmissstation.presenters.StationListPresenter;
import svp.com.dontmissstation.ui.model.SubwayStationView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_station_toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.activity_statoin_fab_addnew);
        fab.setOnClickListener(this);
    }

    @Override
    public void onStart() {
        Collection<SubwayStationView> stations = getPresenter().getStation();

    }

    @Override
    public void onClick(View v) {

    }

}
