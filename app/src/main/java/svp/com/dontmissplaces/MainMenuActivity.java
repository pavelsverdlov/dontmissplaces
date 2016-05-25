package svp.com.dontmissplaces;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.svp.infrastructure.mvpvs.view.AppCompatActivityView;

import butterknife.Bind;
import butterknife.ButterKnife;
import svp.com.dontmissplaces.presenters.MainMenuPresenter;
import svp.com.dontmissplaces.ui.ActivityCommutator;
import svp.com.dontmissplaces.ui.ActivityPermissions;
import svp.com.dontmissplaces.ui.MapView;
import svp.com.dontmissplaces.ui.TrackRecordingToolbarView;
import svp.com.dontmissplaces.ui.model.SessionView;
import svp.com.dontmissplaces.ui.model.TrackView;

public class MainMenuActivity extends AppCompatActivityView<MainMenuPresenter>
        implements NavigationView.OnNavigationItemSelectedListener, ActivityCommutator.ICommutativeElement {

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
    }

    private final MapView mapView;
    private final ActivityPermissions permissions;
    private TrackRecordingToolbarView recordingToolbarView;


    @Bind(R.id.track_recording_fabtoolbar) com.bowyer.app.fabtransitionlayout.FooterLayout trackRecordingFooter;
    @Bind(R.id.track_recording_start_fab) FloatingActionButton fabTrackRecordingBtn;

    public MainMenuActivity(){
        permissions = new ActivityPermissions(this);
        mapView = new MapView(this,permissions);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.mainmenu_toolbar);
        setSupportActionBar(toolbar);


        initFabTrackRecordingBtn();
        trackRecordingFooter.setFab(fabTrackRecordingBtn);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.mainmenu_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.mainmenu_nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mapView.onCreate(savedInstanceState);

    }
    @Override
    protected void onStart() {
        mapView.onStart();
        super.onStart();
        //mLoggerServiceManager.startup(this, null);
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
    private void initFabTrackRecordingBtn(){
        fabTrackRecordingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SessionView session = getPresenter().startNewTrackSession();
                mapView.startTrackRecording(session);
            }
        });
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
}


