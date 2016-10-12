package svp.com.dontmissstation.ui.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;

import com.svp.infrastructure.mvpvs.commutate.ActivityOperationItem;
import com.svp.infrastructure.mvpvs.commutate.ICommutativeElement;
import com.svp.infrastructure.mvpvs.view.AppCompatActivityView;

import java.util.Collection;
import java.util.Vector;

import butterknife.Bind;
import butterknife.ButterKnife;
import svp.com.dontmissstation.R;
import svp.com.dontmissstation.presenters.SearchNewRoutePresenter;
import svp.com.dontmissstation.ui.model.SubwayStationView;

public class SearchNewRouteActivity extends AppCompatActivityView<SearchNewRoutePresenter>
        implements ICommutativeElement, View.OnClickListener {

    @Override
    public ActivityOperationItem getOperation() {
        return ActivityOperationResult.SearchNewRoute;
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    public static class ViewState extends com.svp.infrastructure.mvpvs.viewstate.ViewState<SearchNewRouteActivity> {

        public ViewState(SearchNewRouteActivity view) {
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

    @Bind(R.id.activity_search_new_route_your_location) AutoCompleteTextView yourLocationTextView;
    @Bind(R.id.activity_search_new_route_choose_destination) AutoCompleteTextView chooseDestinationTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_new_route);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.activity_search_new_route_get_route_fab);
        fab.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activity_search_new_route_get_route_fab:
                String from = yourLocationTextView.getText().toString();
                String to = yourLocationTextView.getText().toString();

                getPresenter().searchNewRoute(from, to);
                break;
        }
    }

    @Override
    public void onStart(){
        super.onStart();

        Collection<SubwayStationView> stations = getPresenter().getSubwayStations();
        Vector<String> names = new Vector<>();

        for (SubwayStationView s : stations){
            names.add(s.getName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, names);
        yourLocationTextView.setAdapter(adapter);
        chooseDestinationTextView.setAdapter(adapter);
    }
}
