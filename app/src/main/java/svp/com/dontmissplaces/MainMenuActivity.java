package svp.com.dontmissplaces;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.svp.infrastructure.mvpvs.view.AppCompatActivityView;

import butterknife.Bind;
import butterknife.ButterKnife;
import svp.com.dontmissplaces.db.Place;
import svp.com.dontmissplaces.presenters.MainMenuPresenter;
import svp.com.dontmissplaces.ui.ActivityCommutator;
import svp.com.dontmissplaces.ui.ActivityPermissions;
import svp.com.dontmissplaces.ui.map.MapView;
import svp.com.dontmissplaces.ui.TrackRecordingToolbarView;
import svp.com.dontmissplaces.ui.map.IMapView;
import svp.com.dontmissplaces.ui.map.OnMapClickListener;
import svp.com.dontmissplaces.ui.model.PlaceView;
import svp.com.dontmissplaces.ui.model.SessionView;
import svp.com.dontmissplaces.ui.model.TrackView;
import svp.com.dontmissplaces.utils.LocationEx;

public class MainMenuActivity extends AppCompatActivityView<MainMenuPresenter>
        implements  NavigationView.OnNavigationItemSelectedListener,
                    ActivityCommutator.ICommutativeElement,
                    View.OnClickListener,
                    OnMapClickListener {

    private static final String TAG = "MainMenuActivity";

    @Override
    public ActivityCommutator.ActivityOperationResult getOperation() {
        return ActivityCommutator.ActivityOperationResult.MainMenu;
    }
    @Override
    public Activity getActivity() { return this; }

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

        public void expandTrackRecordingToolbar() {
            view.trackRecordingFooter.expandFab();
            view.createTrackRecordingToolbarView();
        }
        public void slideOutFabTrackRecordingToolbar() {
            view.trackRecordingFooter.slideOutFab();
            view.recordingToolbarView = null;
        }

        public void updateTrackTime(final long timeSpent) {
            view.runOnUiThread(new Runnable(){
                @Override
                public void run() {
                    view.recordingToolbarView.updateTime(timeSpent);
                }});
        }
        public void updateTrackDispance(final String dis) {
            view.runOnUiThread(new Runnable(){
                @Override
                public void run() {
                    view.recordingToolbarView.updateDispance(dis);
                }});
        }

        public void beginRise(){

        }

        public void displayTrackOnMap(TrackView track) {
            view.setTitle(track.getHeader());
            view.mapView.showSessionsTrack(track.sessions);
        }

        public void showPlaceInfo(Place place){
            view.showPlaceInfoLayout();
            ((TextView)view.findViewById(R.id.select_place_show_title)).setText(place.address);//place.country + " " + place.city + " " +place.address + " " + place.description);
            ((TextView)view.findViewById(R.id.select_place_show_latlng)).setText(place.latitude + ", "+place.longitude);
        }
    }

    private final IMapView mapView;
    private final ActivityPermissions permissions;
    private TrackRecordingToolbarView recordingToolbarView;

    @Bind(R.id.track_recording_fabtoolbar) com.bowyer.app.fabtransitionlayout.FooterLayout trackRecordingFooter;
    @Bind(R.id.track_recording_start_fab) FloatingActionButton fabTrackRecordingBtn;
    @Bind(R.id.track_recording_show_place_info) FloatingActionButton fabShowPlaceByCurrentLocationBtn;
    @Bind(R.id.floating_action_layout) LinearLayout floatingActionlayout;

   // @Bind(R.id.map_app_bar_layout) AppBarLayout mapLayout;
//    @Bind(R.id.select_place_scrolling_act_content_view) View mapContentLayout;
    @Bind(R.id.select_place_scrolling_act_save_fab) FloatingActionButton fabSavePlaceLocationBtn;

    public MainMenuActivity(){
        permissions = new ActivityPermissions(this);
        mapView = new MapView(this,permissions);
    }
     BottomSheetBehavior behavior;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.mainmenu_toolbar);
        setSupportActionBar(toolbar);

        trackRecordingFooter.setFab(fabTrackRecordingBtn);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.mainmenu_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.mainmenu_nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        fabShowPlaceByCurrentLocationBtn.setOnClickListener(this);
        fabTrackRecordingBtn.setOnClickListener(this);
        fabSavePlaceLocationBtn.setOnClickListener(this);
        fabSavePlaceLocationBtn.setVisibility(View.GONE);

        CoordinatorLayout coordinatorLayout = (CoordinatorLayout)MainMenuActivity.this.findViewById(R.id.content_main_menu_coordinator_layout);
        behavior = BottomSheetBehavior.from(coordinatorLayout.findViewById(R.id.select_place_scrolling_act_content_view));
        behavior.setPeekHeight(0);

        coordinatorLayout.findViewById(R.id.select_place_scrolling_header_layout)
                .setOnClickListener(this);

        mapView.onCreate(savedInstanceState);
        mapView.setOnMapClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.track_recording_show_place_info:
                showPlaceByCurrentLocation(v);
                break;
            case R.id.track_recording_start_fab:
                startNewTrackSession();
                break;
            case R.id.select_place_scrolling_header_layout:
                if(behavior.getState() == BottomSheetBehavior.STATE_EXPANDED){
                    behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }else{
                    behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
                break;
            case R.id.select_place_scrolling_act_save_fab:

                break;
        }
    }

    @Override
    protected void onStart() {
        mapView.onStart();
        super.onStart();
    }
    @Override
    protected void onStop(){
        mapView.onStop();
        super.onStop();
    }
    @Override
    protected void onResume(){
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
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.mainmenu_drawer_layout);
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
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
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
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.mainmenu_drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_CANCELED == resultCode) {
            return;
        }
        getPresenter().incomingResultFrom(
                ActivityCommutator.ActivityOperationResult.get(resultCode),data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != ActivityPermissions.LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }
        if (this.permissions.isFineLocationGranted(permissions, grantResults)){
            mapView.enableMyLocation();
        }else{
            this.permissions.fineLocationPermissionDenied = true;
        }
    }
    /**
    * Start recording track btn
    */
    private void startNewTrackSession(){
        SessionView session = getPresenter().startNewTrackSession();
        mapView.startTrackRecording(session);
    }
    private void createTrackRecordingToolbarView(){
        recordingToolbarView = new TrackRecordingToolbarView(this);
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
     * Place show/save
     */
    @Override
    public void onMapClick(LatLng latLng) {
        getPresenter().showPlaceInfoByLocation(latLng);
    }
    private void showPlaceByCurrentLocation(View v){
        getPresenter().showPlaceInfoByLocation(LocationEx.getLatLng(mapView.getMyLocation()));

//        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
//            @Override
//            public void onStateChanged(@NonNull View bottomSheet, int newState) {
//                // React to state change
//            }
//
//            @Override
//            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
//                // React to dragging events
//            }
//        });
    }
    private void showPlaceInfoLayout() {
        fabSavePlaceLocationBtn.setVisibility(View.VISIBLE);
        behavior.setPeekHeight(225);
        behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }
    private void hidePlaceInfoLayout() {
        floatingActionlayout.setVisibility(View.VISIBLE);

//        Display display = getWindowManager().getDefaultDisplay();
//        Point size = new Point();
//        display.getSize(size);
//        ViewGroup.LayoutParams params = mapLayout.getLayoutParams();
//        params.height = size.y;
//        mapLayout.setLayoutParams(params);

//        mapContentLayout.setVisibility(View.GONE);

//        setFabGone(fabSavePlaceLocationBtn);
    }


    private void setFabGone(FloatingActionButton fab){
        CoordinatorLayout.LayoutParams p = (CoordinatorLayout.LayoutParams) fab.getLayoutParams();
        p.setBehavior(null); //should disable default animations
        p.setAnchorId(View.NO_ID); //should let you set visibility
        fab.setLayoutParams(p);
        fab.setVisibility(View.GONE); // View.INVISIBLE might also be worth trying
    }
    private void setFabVisible(FloatingActionButton fab){
        CoordinatorLayout.LayoutParams p = (CoordinatorLayout.LayoutParams) fab.getLayoutParams();
        p.setBehavior(new FloatingActionButton.Behavior());
//        p.setAnchorId(R.id.map_app_bar_layout);
        fab.setLayoutParams(p);
    }
}


