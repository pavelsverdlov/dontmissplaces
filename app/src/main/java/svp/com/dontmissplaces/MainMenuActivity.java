package svp.com.dontmissplaces;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.SearchManager;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;

import com.svp.infrastructure.common.ViewExtensions;
import com.svp.infrastructure.mvpvs.view.AppCompatActivityView;

import org.osmdroid.util.BoundingBoxE6;

import butterknife.ButterKnife;
import svp.com.dontmissplaces.model.Map.Point2D;
import svp.com.dontmissplaces.model.nominatim.PhraseProvider;
import svp.com.dontmissplaces.presenters.MainMenuPresenter;
import svp.com.dontmissplaces.ui.ActivityCommutator;
import svp.com.dontmissplaces.ui.ActivityPermissions;
import svp.com.dontmissplaces.ui.behaviors.OverMapBottomSheetBehavior;
import svp.com.dontmissplaces.ui.map.*;
import svp.com.dontmissplaces.ui.TrackRecordingToolbarView;
import svp.com.dontmissplaces.ui.map.IMapView;
import svp.com.dontmissplaces.ui.map.OnMapClickListener;
import svp.com.dontmissplaces.ui.model.IPOIView;
import svp.com.dontmissplaces.ui.model.SessionView;
import svp.com.dontmissplaces.ui.model.TrackView;
//http://wptrafficanalyzer.in/blog/adding-google-places-autocomplete-api-as-custom-suggestions-in-android-search-dialog/
public class MainMenuActivity extends AppCompatActivityView<MainMenuPresenter>
        implements NavigationView.OnNavigationItemSelectedListener,
        ActivityCommutator.ICommutativeElement,
        View.OnClickListener,
        OnMapClickListener,
        LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = "MainMenuActivity";

    @Override
    public ActivityCommutator.ActivityOperationResult getOperation() {
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
                    //    view.recordingToolbarView.updateTime(timeSpent);
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

        public void showPlaceInfo(final IPOIView place) {
            if (place == null) {
                return;
            }
            view.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    view.showPlaceInfoLayout();
                    ViewExtensions.<TextView>setOnLongClickListener(view, R.id.select_place_show_title,
                            new View.OnLongClickListener() {
                                @Override
                                public boolean onLongClick(View v) {
                                    CharSequence text = ((TextView) v).getText();
                                    ClipboardManager clipboard = (ClipboardManager) view.getSystemService(CLIPBOARD_SERVICE);
                                    clipboard.setText(text);
                                    getSnackbar("Name was copied to clipboard.");
                                    return true;
                                }
                            })
                        .setText(place.getName());
                    String type = place.getType();
                    switch (PhraseProvider.getType(type)) {
                        case Food:
                            String cuisine = place.getExtraTags().getCuisine();
                            if (!cuisine.isEmpty()) {
                                type = type + " (" + cuisine + ")";
                            }
                            break;
                    }
                    ((TextView) view.findViewById(R.id.select_place_show_placetype)).setText(type);
                    ViewExtensions.<TextView>findViewById(view, R.id.select_place_show_address)
                            .setText(place.getAddress());

                    view.mapView.drawMarker(place);
                }
            });
        }


    }

    private IMapView mapView;
    private final ActivityPermissions permissions;
    private TrackRecordingToolbarView recordingToolbarView;
    private OverMapBottomSheetBehavior behavior;
    private final int bottomPanelHeight = 224;

    View placeInfoHeader;

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
        behavior = OverMapBottomSheetBehavior.from(this.findViewById(R.id.select_place_scrolling_act_content_view));
        behavior.setPeekHeight(bottomPanelHeight);


        placeInfoHeader = coordinatorLayout.findViewById(R.id.select_place_header_layout);
        placeInfoHeader.setOnClickListener(this);
        //action_bottom_panel
        ViewExtensions.setOnClickListener(this,R.id.track_recording_start_btn, this);
        findViewById(R.id.track_recording_show_place_info_btn).setOnClickListener(this);
        findViewById(R.id.move_to_my_location).setOnClickListener(this);

        coordinatorLayout.findViewById(R.id.select_place_content_header_layout)
                .setOnClickListener(this);

        handleIntent(getIntent());
    }

    /**
     * Base click handler of activity
     */
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.track_recording_show_place_info_btn:
                showPlacesByCurrentLocation(v);
                break;
            case R.id.track_recording_start_btn:
                startNewTrackSession();
                break;
            case R.id.select_place_header_layout:
                invertStatePlaceInfoLayout(behavior.getState());
                break;
            case R.id.move_to_my_location:
                if (permissions.checkPermissionFineLocation() && permissions.checkPermissionCoarseLocation()){
                    mapView.moveTo(mapView.getMyLocation());
                }else{

                }
                break;

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        switch (getPresenter().getMapViewType()) {
            case Google:
                mapView = new GoogleMapView(this, permissions);
                break;
            case Osmdroid:
                mapView = new OsmdroidMapView(this, permissions);
                break;
        }

        mapView.onCreate(null);
        mapView.setOnMapClickListener(this);

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
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.content_main_map_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        handleIntent(intent);
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

        if (id == R.id.nav_mainmenu_history_tracks) {
            getPresenter().openHistoryTracks();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.content_main_map_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_CANCELED == resultCode) {
            return;
        }
        getPresenter().incomingResultFrom(
                ActivityCommutator.ActivityOperationResult.get(resultCode), data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != ActivityPermissions.LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }
        if (this.permissions.isFineLocationGranted(permissions, grantResults)) {
            mapView.enableMyLocation();
        } else {
            this.permissions.fineLocationPermissionDenied = true;
        }
    }

    /** Start recording track btn  */
    private void startNewTrackSession() {
        getPresenter().startNewTrackSession();
    }
    private void createTrackRecordingToolbarView() {
        recordingToolbarView = new TrackRecordingToolbarView(this);
        recordingToolbarView.show();
        recordingToolbarView.setClickListeners(
                new TrackRecordingToolbarView.OnResumePauseClickListener() {
                    @Override
                    public void onPauseClick() {
                        getPresenter().pauseTrackRecording();
                        mapView.pauseTrackRecording();
                    }

                    @Override
                    public void onResumeClick() {
                        getPresenter().resumeTrackRecording();
                        mapView.resumeTrackRecording();
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
        getPresenter().showPlaceInfoByPoint(p);
    }


    private void showPlacesByCurrentLocation(View v) {
        getPresenter().showPlaceInfoByPoint(mapView.getMyLocation());
    }

    private void showPlaceInfoLayout() {
        invertStatePlaceInfoLayout(BottomSheetBehavior.STATE_EXPANDED);
    }

    private void invertStatePlaceInfoLayout(int state) {
        if (state == BottomSheetBehavior.STATE_EXPANDED) {
            behavior.setState(
                    BottomSheetBehavior.STATE_COLLAPSED,
                    bottomPanelHeight + placeInfoHeader.getMeasuredHeight());
        } else {
            behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        }
    }


    /*
    * Search
    * */
    private void initSearch(Menu menu){
        SearchManager searchManager =(SearchManager) this.getSystemService(Context.SEARCH_SERVICE);
        MenuItem item = menu.findItem(R.id.main_menu_action_search);
        SearchView searchView =(SearchView) MenuItemCompat.getActionView(item);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(this.getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                getPresenter().openSearch(newText);
                return true;
            }
        });
    }
    private void handleIntent(Intent intent){
//        if(intent.getAction().equals(Intent.ACTION_SEARCH)){
//            doSearch(intent.getStringExtra(SearchManager.QUERY));
//        }else if(intent.getAction().equals(Intent.ACTION_VIEW)){
//            getPlace(intent.getStringExtra(SearchManager.EXTRA_DATA_KEY));
//        }
    }
    private void doSearch(String query){
        Bundle data = new Bundle();
        data.putString("query", query);
        getSupportLoaderManager().restartLoader(0, data, this);
    }

    private void getPlace(String query){
        Bundle data = new Bundle();
        data.putString("query", query);
        getSupportLoaderManager().restartLoader(1, data, this);
    }
    @Override
    public Loader<Cursor> onCreateLoader(int arg0, Bundle query) {
        CursorLoader cLoader = null;
        if(arg0==0)
            cLoader = new CursorLoader(getBaseContext(),
                    svp.com.dontmissplaces.model.Map.google.PlaceProvider.SEARCH_URI, null, null, new String[]{ query.getString("query") }, null);
        else if(arg0==1)
            cLoader = new CursorLoader(getBaseContext(),
                    svp.com.dontmissplaces.model.Map.google.PlaceProvider.DETAILS_URI, null, null, new String[]{ query.getString("query") }, null);
        return cLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> arg0, Cursor c) {
       // showLocations(c);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> arg0) {
        // TODO Auto-generated method stub
    }
    @SuppressLint("NewApi")
    @Override
    public boolean onSearchRequested() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
//            MenuItem mi = mMenu.findItem(R.id.search);
//            if(mi.isActionViewExpanded()){
//                mi.collapseActionView();
//            } else{
//                mi.expandActionView();
//            }
        } else{
            //onOptionsItemSelected(mMenu.findItem(R.id.search));
        }
        return super.onSearchRequested();
    }
}


