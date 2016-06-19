package svp.com.dontmissplaces;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.svp.infrastructure.common.ViewExtensions;
import com.svp.infrastructure.mvpvs.view.AppCompatActivityView;

import org.osmdroid.util.BoundingBoxE6;

import butterknife.Bind;
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

        public ActivityPermissions getPermissions(){
            return view.permissions;
        }

        public void expandTrackRecordingToolbar() {
            //view.trackRecordingFooter.expandFab();
//            view.createTrackRecordingToolbarView();
        }
        public void slideOutFabTrackRecordingToolbar() {
//            view.trackRecordingFooter.slideOutFab();
            view.recordingToolbarView = null;
        }

        public void updateTrackTime(final long timeSpent) {
            view.runOnUiThread(new Runnable(){
                @Override
                public void run() {
                //    view.recordingToolbarView.updateTime(timeSpent);
                }});
        }
        public void updateTrackDispance(final String dis) {
            view.runOnUiThread(new Runnable(){
                @Override
                public void run() {
                  //  view.recordingToolbarView.updateDispance(dis);
                }});
        }

        public void beginRise(){

        }

        public void displayTrackOnMap(TrackView track) {
            view.setTitle(track.getHeader());
            view.mapView.showSessionsTrack(track.sessions);
        }

        public void showPlaceInfo(IPOIView place){
            if(place == null){
                return;
            }
            view.showPlaceInfoLayout();
            ((TextView)view.findViewById(R.id.select_place_show_title)).setText(place.getName());
            String type = place.getType();
            switch (PhraseProvider.getType(type)){
                case Food:
                    String cuisine = place.getExtraTags().getCuisine();
                    if(!cuisine.isEmpty()){
                        type = type + " (" + cuisine +")";
                    }
                    break;
            }
            ((TextView)view.findViewById(R.id.select_place_show_placetype)).setText(type);
            ViewExtensions.<TextView>findViewById(view,R.id.select_place_show_address)
                    .setText(place.getAddress());

            view.mapView.drawMarker(place);
        }
    }

    private IMapView mapView;
    private final ActivityPermissions permissions;
    private TrackRecordingToolbarView recordingToolbarView;
    private OverMapBottomSheetBehavior behavior;
    private final int bottomPanelHeight = 224;

    View placeInfoHeader;

    public MainMenuActivity(){
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

        CoordinatorLayout coordinatorLayout = (CoordinatorLayout)MainMenuActivity.this.findViewById(R.id.content_main_menu_coordinator_layout);
        behavior = OverMapBottomSheetBehavior.from(this.findViewById(R.id.select_place_scrolling_act_content_view));
        behavior.setPeekHeight(bottomPanelHeight);


        placeInfoHeader = coordinatorLayout.findViewById(R.id.select_place_header_layout);
        placeInfoHeader.setOnClickListener(this);
        //action_bottom_panel
        findViewById(R.id.track_recording_start_btn).setOnClickListener(this);
        findViewById(R.id.track_recording_show_place_info_btn).setOnClickListener(this);

        coordinatorLayout.findViewById(R.id.select_place_content_header_layout)
                .setOnClickListener(this);
    }
    /**
     * Base click handler of activity
     * */
    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.track_recording_show_place_info_btn:
                showPlaceByCurrentLocation(v);
                break;
            case R.id.track_recording_start_btn:
                startNewTrackSession();
                break;
            case R.id.select_place_header_layout:
                invertStatePlaceInfoLayout(behavior.getState());
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        switch (getPresenter().getMapViewType()){
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
            getPresenter().openSettings();
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
    /** Start recording track btn */
    private void startNewTrackSession(){
        SessionView session = getPresenter().startNewTrackSession();
        mapView.startTrackRecording(session);
    }
    //TODO: implement new functional for this actions
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

    /** place show/save */
    @Override
    public void onMapClick(Point2D point) {
        getPresenter().showPlaceInfoByLocation(point);
    }
    @Override
    public void onZoom(int zoom, BoundingBoxE6 box){
        getPresenter().searchPOI(zoom, box);
    }
    @Override
    public void onScroll(int zoom, BoundingBoxE6 box){
        getPresenter().searchPOI(zoom, box);
    }

    private void showPlaceByCurrentLocation(View v){
        getPresenter().showPlaceInfoByLocation(new Point2D(mapView.getMyLocation()));
    }

    private void showPlaceInfoLayout() {
        invertStatePlaceInfoLayout(BottomSheetBehavior.STATE_EXPANDED);
    }
    private void invertStatePlaceInfoLayout(int state){
        if(state == BottomSheetBehavior.STATE_EXPANDED){
            behavior.setState(
                    BottomSheetBehavior.STATE_COLLAPSED,
                    bottomPanelHeight + placeInfoHeader.getMeasuredHeight());
        }else{
            behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        }
    }
}


