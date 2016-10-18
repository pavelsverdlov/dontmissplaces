package svp.com.dontmissstation;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomSheetBehavior;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

import com.svp.infrastructure.ActivityPermissions;
import com.svp.infrastructure.mvpvs.commutate.ActivityOperationItem;
import com.svp.infrastructure.mvpvs.commutate.ICommutativeElement;
import com.svp.infrastructure.mvpvs.view.AppCompatActivityView;

import org.osmdroid.util.BoundingBoxE6;

import java.util.Iterator;
import java.util.Vector;

import svp.app.map.GoogleMapView;
import svp.app.map.IMapView;
import svp.app.map.OnMapClickListener;
import svp.app.map.model.IMapPolyline;
import svp.app.map.model.Point2D;
import svp.com.dontmissstation.presenters.MainPresenter;
import svp.com.dontmissstation.ui.activities.ActivityOperationResult;
import svp.com.dontmissstation.ui.model.MainNavigationView;
import svp.com.dontmissstation.ui.model.POIStationView;
import svp.com.dontmissstation.ui.model.SubwayLineView;
import svp.com.dontmissstation.ui.model.SubwayStationView;
import svp.com.dontmissstation.ui.model.SubwayView;

public class MainActivity extends AppCompatActivityView<MainPresenter>
        implements NavigationView.OnNavigationItemSelectedListener, ICommutativeElement, OnMapClickListener, View.OnClickListener {

    @Override
    public ActivityOperationItem getOperation() {
        return ActivityOperationResult.Main;
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    public static class ViewState extends com.svp.infrastructure.mvpvs.viewstate.ViewState<MainActivity> {
        SubwayView subwayCache;
        private Vector<IMapPolyline> polylinesCache;
        public ViewState(MainActivity view) {
            super(view);
            polylinesCache = new Vector<>();
        }

        @Override
        protected void restore() {
            if(hasSubwayCached()){
                view.navigationView.initSubwayMenu(subwayCache);
            }else{
                view.navigationView.initGeneralMenu();
            }
        }

        @Override
        public void saveState() {
            //TODO: implement approach for caching data
        }
        public boolean hasSubwayCached(){
            return subwayCache != null;
        }
        public SubwayView getSubwayCached() {
            return subwayCache;
        }
        public void clearCache(){
            subwayCache = null;
            polylinesCache.clear();
        }

        public void showSubway(SubwayView subway){
            this.subwayCache =subway;

            for (SubwayLineView line: subway.getLines()){
                Iterator<SubwayStationView> stations = line.getStations().iterator();
                Point2D prev = stations.next().getCoordinate();
                while (stations.hasNext()){
                    IMapPolyline pl = view.mapView.createPolyline();
                    Vector<Point2D> points = new Vector<>();
                    points.add(prev);
                    view.mapView.createCircle().draw(line.getColor(),prev,8);
                    prev = stations.next().getCoordinate();
                    view.mapView.createCircle().draw(line.getColor(),prev,8);
                    points.add(prev);
                    pl.draw(line.getColor(),8,points);
                    polylinesCache.add(pl);
                }
            }

        }
        public void MapCameraMoveTo(final Point2D p){
            if(p.isEmpty()){
                getToast( "GPS location disabled.").show();
            }else {
                view.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        view.mapView.moveTo(p);
                    }
                });
            }
        }
        public void showStations(Vector<SubwayStationView> stations) {
            for (SubwayStationView s : stations) {
                view.mapView.addMarker(new POIStationView(s), -1 );
            }
        }

        @Override
        public Activity getActivity() {
            return view;
        }

        public ActivityPermissions getPermissions() {
            return view.permissions;
        }


    }

    private IMapView mapView;
    private final ActivityPermissions permissions;
    private final MainNavigationView navigationView;

    public MainActivity(){
        permissions = new ActivityPermissions(this);
        navigationView = new MainNavigationView(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); 
        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_route_toolbar_id);
        setSupportActionBar(toolbar);
//        BottomSheetBehavior<View> behavior = BottomSheetBehavior.from(findViewById(R.id.select_place_scrolling_act_content_view));
       // behavior.setPeekHeight(32);
       // behavior.setState(BottomSheetBehavior.STATE_EXPANDED);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close){
//            @Override
//            public void onDrawerStateChanged(int newState) {
//                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//                if( newState == DrawerLayout.STATE_SETTLING) {
//                    boolean o =drawer.isDrawerOpen(GravityCompat.START) ;
//                }
//            }
        };
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        navigationView.setItemSelectedListener(this);
        navigationView.initGeneralMenu();

        findViewById(R.id.activity_main_get_stations_near_me).setOnClickListener(this);
        findViewById(R.id.activity_main_init_route).setOnClickListener(this);
        findViewById(R.id.activity_main_move_to_my_location).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activity_main_get_stations_near_me:
                getPresenter().findNearStation();
                break;
            case R.id.activity_main_init_route:
                getPresenter().openRouteActivity();
                break;
            case R.id.activity_main_move_to_my_location:
                getPresenter().onMoveToMyLocation();
                break;
        }
    }
    @Override
    protected void onStop(){
        super.onStop();

    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //No call for super(). Bug on API Level > 11.
    }

    @Override
    protected void onStart() {
        super.onStart();



//        mapView = new OsmdroidMapView(getActivity(),R.id.osmdroid_map,getPresenter().gps);
        mapView = new GoogleMapView(this,R.id.activity_main_content_google_map);

        permissions.checkPermissionExternalStorage();
        permissions.checkPermissionFineLocation();

        mapView.setOnMapClickListener(this);

        mapView.onCreate(null);

        mapView.onStart();

        Handler uih = new Handler();
        uih.post(new Runnable() {
            @Override
            public void run() {
                mapView.moveTo(new Point2D(48.216667, 16.373333));
            }
        });

//        mapView.mapView.post(new Runnable() {
//            @Override
//            public void run() {
//                mapView.moveTo(new Point2D(48.859,2.296));
//            }
//        });
    }

    @Override
    protected void onResume() {
        if (this.permissions.fineLocationPermissionDenied) {
            this.permissions.showMissingPermissionError();// Permission was not granted, display error dialog.
        }

        mapView.onResume();
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if(getPresenter().isSubwayVisualizeMode()){
                getPresenter().clearSelectedSubway();
                navigationView.initGeneralMenu();
            }else {
                super.onBackPressed();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.main_menu_action_search).getActionView();
        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(true); //

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.main_activity_drawer_add_new_subway:
                getPresenter().openAddNewSubway();
                break;
            case R.id.main_activity_drawer_add_subways:
            case R.id.main_activity_drawer_route:
                getPresenter().openListSubwaysActivity();
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != ActivityPermissions.LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }
        if (this.permissions.isFineLocationGranted(permissions, grantResults)) {
            if(this.permissions.isFineLocationGranted()) {

            }else {

            }

        } else {
            this.permissions.fineLocationPermissionDenied = true;
        }
    }


    /**
     * OnMapClickListener
     * */
    @Override
    public void onMapClick(Point2D point) {
        getPresenter().pickOnPlace(point);

    }

    @Override
    public void onZoom(int zoom, BoundingBoxE6 box) {

    }

    @Override
    public void onScroll(int zoom, BoundingBoxE6 box) {

    }

    @Override
    public void onMapLongClick(Point2D point) {

    }

}
