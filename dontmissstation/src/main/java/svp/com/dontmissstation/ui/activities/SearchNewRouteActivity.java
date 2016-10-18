package svp.com.dontmissstation.ui.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.svp.infrastructure.PreferenceSettings;
import com.svp.infrastructure.common.StringEx;
import com.svp.infrastructure.common.ViewExtensions;
import com.svp.infrastructure.mvpvs.commutate.ActivityOperationItem;
import com.svp.infrastructure.mvpvs.commutate.ICommutativeElement;
import com.svp.infrastructure.mvpvs.view.AppCompatActivityView;

import java.util.Collection;
import java.util.Locale;
import java.util.Vector;

import butterknife.Bind;
import butterknife.ButterKnife;
import svp.com.dontmissstation.R;
import svp.com.dontmissstation.presenters.SearchNewRoutePresenter;
import svp.com.dontmissstation.ui.activities.edit.subway.station.ConnectStationsAdapter;
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
        private final Vector<SearchNewRoutePresenter.RouteView> routes;
        public ViewState(SearchNewRouteActivity view) {
            super(view);
            routes = new Vector<>();
        }

        @Override
        protected void restore() {
            this.routes.clear();
        }

        @Override
        public void saveState() {

        }

        @Override
        public Activity getActivity() {
            return view;
        }

        public void addRoutes(Vector<SearchNewRoutePresenter.RouteView> routes) {
            this.routes.addAll(routes);
            view.routesListView.setAdapter(new RoutesAdapter(view.getLayoutInflater(), this.routes));
            view.routesListView.postInvalidate();
        }
    }

    @Bind(R.id.activity_search_new_route_your_location) AutoCompleteTextView yourLocationTextView;
    @Bind(R.id.activity_search_new_route_choose_destination) AutoCompleteTextView chooseDestinationTextView;
    @Bind(R.id.activity_search_new_route_list_routes) ListView routesListView;

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
                String to = chooseDestinationTextView.getText().toString();

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

        initAutoCompleteTextView(yourLocationTextView,names);
        initAutoCompleteTextView(chooseDestinationTextView,names);
    }
    private void initAutoCompleteTextView(AutoCompleteTextView acTextView, Vector<String> worlds){
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.select_dialog_singlechoice, worlds);
        acTextView.setAdapter(adapter);
        acTextView.setThreshold(1);
    }

    public static class RoutesAdapter extends BaseAdapter implements View.OnClickListener {
        private final LayoutInflater layoutInflater;
        private final Vector<SearchNewRoutePresenter.RouteView> routes;

        public RoutesAdapter(LayoutInflater layoutInflater, Vector<SearchNewRoutePresenter.RouteView> route) {
            this.layoutInflater = layoutInflater;
            this.routes = route;
        }

        @Override
        public int getCount() {
            return routes.size();
        }

        @Override
        public Object getItem(int position) {
            return routes.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            if (view == null) {
                view = layoutInflater.inflate(R.layout.activity_search_new_route_template, parent, false);
            }

            try {
                SearchNewRoutePresenter.RouteView route = routes.get(position);

                (view).setOnClickListener(this);
                view.setTag(route);

                ViewExtensions.<TextView>findViewById(view, R.id.activity_search_new_route_template_name_from_to)
                        .setText(route.getTitle());
                ViewExtensions.<TextView>findViewById(view, R.id.activity_search_new_route_template_station_count)
                        .setText(StringEx.toString(route.getCountStations()));
                ViewExtensions.<TextView>findViewById(view, R.id.activity_search_new_route_template_lines)
                        .setText(StringEx.toString(route.getLines()));
            }catch (Exception ex){
                ex.getStackTrace();
            }
            return view;
        }

        @Override
        public void onClick(View v) {
            SearchNewRoutePresenter.RouteView route = (SearchNewRoutePresenter.RouteView) v.getTag();
        }
    }
}
