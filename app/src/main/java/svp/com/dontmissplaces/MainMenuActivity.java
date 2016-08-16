package svp.com.dontmissplaces;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.TextView;

import com.svp.infrastructure.common.ViewExtensions;
import com.svp.infrastructure.mvpvs.commutate.ActivityOperationItem;
import com.svp.infrastructure.mvpvs.commutate.ICommutativeElement;
import com.svp.infrastructure.mvpvs.view.AppCompatActivityView;

import org.osmdroid.util.BoundingBoxE6;

import butterknife.Bind;
import butterknife.ButterKnife;
import svp.app.map.model.Point2D;
import svp.com.dontmissplaces.presenters.MainMenuPresenter;
import svp.com.dontmissplaces.ui.ActivityCommutator;
import com.svp.infrastructure.ActivityPermissions;
import svp.com.dontmissplaces.ui.PlaceInfoLayoutView;
import svp.com.dontmissplaces.ui.TrackRecordingToolbarView;
import svp.com.dontmissplaces.ui.map.GoogleMapView;
import svp.com.dontmissplaces.ui.map.IDNMPMapView;
import svp.app.map.OnMapClickListener;
import svp.com.dontmissplaces.ui.map.DNMPOsmdroidMapView;
import svp.com.dontmissplaces.ui.model.IPOIView;
import svp.com.dontmissplaces.ui.model.SessionView;
import svp.com.dontmissplaces.ui.model.TrackView;
//http://wptrafficanalyzer.in/blog/adding-google-places-autocomplete-api-as-custom-suggestions-in-android-search-dialog/
public class MainMenuActivity extends AppCompatActivityView<MainMenuPresenter>
        implements NavigationView.OnNavigationItemSelectedListener,
        ICommutativeElement,
        View.OnClickListener,
        OnMapClickListener {

    private static final String TAG = "MainMenuActivity";

    @Override
    public ActivityOperationItem getOperation() {
        return ActivityCommutator.ActivityOperationResult.MainMenu;
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    public static class ViewState extends com.svp.infrastructure.mvpvs.viewstate.ViewState<MainMenuActivity> {

        protected ViewState(MainMenuActivity view) {
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

        public ActivityPermissions getPermissions() {
            return view.permissions;
        }

        public void startTrackRecording(SessionView sessionView) {
            view.mapView.startTrackRecording(sessionView);
            view.createTrackRecordingToolbarView();
        }

        public void slideOutFabTrackRecordingToolbar() {
//            view.trackRecordingFooter.slideOutFab();
            view.recordingToolbarView = null;
        }

        public void updateTrackTime(final long timeSpent) {
            view.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                view.recordingToolbarView.updateTime(timeSpent);
                }
            });
        }

        public void updateTrackDispance(final String dis) {
            view.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //  view.recordingToolbarView.updateDispance(dis);
                }
            });
        }

        public void beginRise() {

        }

        public void displayTrackOnMap(TrackView track) {
            view.setTitle(track.getHeader());
            view.mapView.showSessionsTrack(track.sessions);
        }

        public void showPlaceInfo(final IPOIView place, final Point2D point) {
            if (place == null) {
                return;
            }
            view.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    view.placeInfoLayoutView.show(ViewState.this, place);
                    view.mapView.drawMarker(place);
                }
            });
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

    }

    private IDNMPMapView mapView;
    private final ActivityPermissions permissions;
    private TrackRecordingToolbarView recordingToolbarView;
    private PlaceInfoLayoutView placeInfoLayoutView;
//    private OverMapBottomSheetBehavior behavior;
//    private final int bottomPanelHeight = 224;
    @Bind(R.id.select_place_move_to_location_btn) TextView moveToLocation;
    @Bind(R.id.select_place_person_pin) TextView personPin;
    @Bind(R.id.select_place_beenhere_btn) ImageButton personBeenHere;


    public MainMenuActivity() {
        permissions = new ActivityPermissions(this);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.mainmenu_toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.content_main_map_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.mainmenu_nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        CoordinatorLayout coordinatorLayout = (CoordinatorLayout) MainMenuActivity.this.findViewById(R.id.content_main_menu_coordinator_layout);

        placeInfoLayoutView = new PlaceInfoLayoutView(this);
//        behavior = OverMapBottomSheetBehavior.from(this.findViewById(R.id.select_place_scrolling_act_content_view));
//        behavior.setPeekHeight(bottomPanelHeight);

        //action_bottom_panel
        ViewExtensions.setOnClickListener(this,R.id.track_recording_start_btn, this);
        findViewById(R.id.track_recording_show_place_info_btn).setOnClickListener(this);
        findViewById(R.id.move_to_my_location).setOnClickListener(this);

        coordinatorLayout.findViewById(R.id.select_place_content_header_layout)
                .setOnClickListener(this);
        moveToLocation.setOnClickListener(this);
        personPin.setOnClickListener(this);
        personBeenHere.setOnClickListener(this);
    }

    /**
     * Base click handler of activity
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.track_recording_show_place_info_btn:
                getPresenter().showPlaceInfoNearPoint(Point2D.empty());
                break;
            case R.id.track_recording_start_btn:
                getPresenter().startNewTrackSession();
                break;
            case R.id.move_to_my_location:
                getPresenter().onMoveToMyLocation();
                break;
            case R.id.select_place_move_to_location_btn:
                mapView.moveTo(placeInfoLayoutView.getPlace().getPoint());
                break;
            case R.id.select_place_person_pin:
                getPresenter().pinPlace(placeInfoLayoutView.getPlace());
                break;
            case R.id.select_place_beenhere_btn:
                getPresenter().beenInPlace(placeInfoLayoutView.getPlace());
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        final ViewStub stub = ViewExtensions.findViewById(this,R.id.layout_stub_map_switcher);
        switch (getPresenter().getMapViewType()) {
            case Google:
                stub.setLayoutResource(R.layout.content_google_map);
                mapView = new GoogleMapView(this, permissions);
                break;
            case Osmdroid:
                stub.setLayoutResource(R.layout.content_osm_map);
                mapView = new DNMPOsmdroidMapView(this, permissions,getPresenter().gps);
                break;
        }
        View view = stub.inflate();
        mapView.onCreate(null);
        mapView.setOnMapClickListener(MainMenuActivity.this);

        mapView.onStart();
    }

    @Override
    protected void onStop() {
        mapView.onStop();
        super.onStop();
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
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        if(placeInfoLayoutView.isShow()){
            placeInfoLayoutView.hide();
            return;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.content_main_map_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        initSearch(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.main_menu_action_settings:
                getPresenter().openSettings();
                return true;
            case R.id.main_menu_action_search:
                //onSearchRequested();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id){
            case R.id.nav_mainmenu_history_tracks:
                getPresenter().openHistoryTracks();
                break;
            case R.id.nav_mainmenu_saved_places:
                getPresenter().openSavedPlaces();
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.content_main_map_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        getPresenter().onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != ActivityPermissions.LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }
        if (this.permissions.isFineLocationGranted(permissions, grantResults)) {
            if(this.permissions.isFineLocationGranted()) {
                getPresenter().permissionFineLocationReceived();
            }else {
                //TODO:
            }
            mapView.enableMyLocation();
        } else {
            this.permissions.fineLocationPermissionDenied = true;
        }
    }

    /** Start recording track btn  */
    private void createTrackRecordingToolbarView() {
        recordingToolbarView = new TrackRecordingToolbarView(this);
        recordingToolbarView.show();
        recordingToolbarView.setClickListeners(
                new TrackRecordingToolbarView.OnResumePauseClickListener() {
                    @Override
                    public void onPauseClick() {
                        getPresenter().pauseTrackRecording();
//                        mapView.pauseTrackRecording();
                    }

                    @Override
                    public void onResumeClick() {
                        getPresenter().resumeTrackRecording();
//                        mapView.resumeTrackRecording();
                    }
                },
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getPresenter().stopTrackRecording();
                        mapView.stopTrackRecording();
                    }
                }
        );
    }

    /**
     * place show/save
     */
    @Override
    public void onMapClick(Point2D point) {
        getPresenter().showPlaceInfoByPoint(point);
    }

    @Override
    public void onZoom(int zoom, BoundingBoxE6 box) {
        getPresenter().searchPOI(zoom, box);
    }

    @Override
    public void onScroll(int zoom, BoundingBoxE6 box) {
        getPresenter().searchPOI(zoom, box);
    }

    @Override
    public void onMapLongClick(Point2D p) {
        getPresenter().showPlaceInfoNearPoint(p);
    }





    /*
    * Search
    * */
    private void initSearch(Menu menu){
        final SearchManager searchManager =(SearchManager) this.getSystemService(Context.SEARCH_SERVICE);
        final MenuItem item = menu.findItem(R.id.main_menu_action_search);
        final SearchView searchView =(SearchView) MenuItemCompat.getActionView(item);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(this.getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.length() > 5) {
                    getPresenter().openSearch(newText);
                }
//                item.collapseActionView();
//                searchView.setIconified(true);
                return true;
            }
        });
    }
}


