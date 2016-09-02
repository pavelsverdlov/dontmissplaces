package svp.com.dontmissstation;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.svp.infrastructure.ActivityPermissions;
import com.svp.infrastructure.mvpvs.commutate.ActivityOperationItem;
import com.svp.infrastructure.mvpvs.commutate.ICommutativeElement;
import com.svp.infrastructure.mvpvs.view.AppCompatActivityView;

import org.osmdroid.util.BoundingBoxE6;

import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import svp.app.map.GoogleMapView;
import svp.app.map.IMapView;
import svp.app.map.OnMapClickListener;
import svp.app.map.model.IMapPolyline;
import svp.app.map.model.Point2D;
import svp.com.dontmissstation.presenters.MainPresenter;
import svp.com.dontmissstation.ui.activities.ActivityOperationResult;
import svp.com.dontmissstation.ui.model.SubwayLineView;
import svp.com.dontmissstation.ui.model.SubwayStationView;
import svp.com.dontmissstation.ui.model.SubwayView;

public class MainActivity extends AppCompatActivityView<MainPresenter>
        implements NavigationView.OnNavigationItemSelectedListener, ICommutativeElement{

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

        }

        @Override
        public void saveState() {

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
                    prev = stations.next().getCoordinate();
                    points.add(prev);
                    pl.draw(line.getColor(),4,points);
                    polylinesCache.add(pl);

                }

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



    public MainActivity(){
        permissions = new ActivityPermissions(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); 
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.activity_edit_apply_changes_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

//        mapView = new OsmdroidMapView(getActivity(),R.id.osmdroid_map,getPresenter().gps);
        mapView = new GoogleMapView(this,R.id.google_map);

        permissions.checkPermissionExternalStorage();
        permissions.checkPermissionFineLocation();

        mapView.setOnMapClickListener(new OnMapClickListener() {
            @Override
            public void onMapClick(Point2D point) {

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
        });

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
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
}
