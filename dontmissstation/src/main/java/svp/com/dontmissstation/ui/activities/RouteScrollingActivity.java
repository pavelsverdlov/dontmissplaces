package svp.com.dontmissstation.ui.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.svp.infrastructure.common.ViewExtensions;
import com.svp.infrastructure.mvpvs.commutate.ActivityOperationItem;
import com.svp.infrastructure.mvpvs.commutate.ICommutativeElement;
import com.svp.infrastructure.mvpvs.view.AppCompatActivityView;

import java.util.Collection;
import java.util.Vector;

import butterknife.Bind;
import butterknife.ButterKnife;
import svp.com.dontmissstation.R;
import svp.com.dontmissstation.presenters.RouteScrollingPresenter;
import svp.com.dontmissstation.ui.model.SubwayStationView;

public class RouteScrollingActivity extends AppCompatActivityView<RouteScrollingPresenter> implements ICommutativeElement {

    @Override
    public ActivityOperationItem getOperation() {
        return ActivityOperationResult.RouteSelection;
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    public static class ViewState extends com.svp.infrastructure.mvpvs.viewstate.ViewState<RouteScrollingActivity> {

        public ViewState(RouteScrollingActivity view) {
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
        setContentView(R.layout.activity_route_scrolling);//activity_route_toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_route_toolbar_id);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.activity_route_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Collection<SubwayStationView> stations = getPresenter().getStation();

        Vector<String> worlds = new Vector<>();
        for (SubwayStationView s : stations){
            worlds.add(s.getName());
        }
        initAutoCompleteTextView(ViewExtensions.<AutoCompleteTextView>findViewById(this, R.id.activity_rouse_station_from_text),worlds);
        initAutoCompleteTextView(ViewExtensions.<AutoCompleteTextView>findViewById(this, R.id.activity_rouse_station_to_text),worlds);
    }

    private void initAutoCompleteTextView(AutoCompleteTextView acTextView, Vector<String> worlds){
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.select_dialog_singlechoice, worlds);
        acTextView.setAdapter(adapter);
        acTextView.setThreshold(1);
    }
}
