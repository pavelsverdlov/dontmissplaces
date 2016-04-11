package svp.com.dontmissplaces;

import android.Manifest;
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

import com.svp.infrastructure.common.PermissionUtils;
import com.svp.infrastructure.mvpvs.view.AppCompatActivityView;

import butterknife.Bind;
import butterknife.ButterKnife;
import svp.com.dontmissplaces.presenters.MainMenuPresenter;
import svp.com.dontmissplaces.ui.ActivityCommutator;
import svp.com.dontmissplaces.ui.ActivityPermissions;
import svp.com.dontmissplaces.ui.Consts;
import svp.com.dontmissplaces.ui.MapView;
import svp.com.dontmissplaces.ui.TrackRecordingToolbarView;

public class MainMenuActivity extends AppCompatActivityView<MainMenuPresenter>
        implements
        NavigationView.OnNavigationItemSelectedListener{

    public static class ViewState extends com.svp.infrastructure.mvpvs.viewstate.ViewState<MainMenuActivity> {

        protected ViewState(MainMenuActivity view) {
            super(view);
        }

        @Override
        protected void restore() {

        }

        @Override
        protected Activity getActivity() {
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

        public void beginRise(){

        }
    }

    private final MapView mapView;
    private final ActivityCommutator commutator;
    private final ActivityPermissions permissions;
    private TrackRecordingToolbarView recordingToolbarView;

    @Bind(R.id.track_recording_fabtoolbar) com.bowyer.app.fabtransitionlayout.FooterLayout trackRecordingFooter;
    @Bind(R.id.track_recording_start_fab) FloatingActionButton fabTrackRecordingBtn;

    public MainMenuActivity(){
        permissions = new ActivityPermissions(this);
        mapView = new MapView(this,permissions);
        commutator = new ActivityCommutator(this, ActivityCommutator.ActivityOperationResult.MainMenu);
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
            commutator.goToActivity(ActivityCommutator.ActivityOperationResult.HistoryTracks);
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
                getPresenter().startTrackRecording();
                mapView.setLocationChangeNotification(true);
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
                        mapView.setLocationChangeNotification(false);
                    }

                    @Override
                    public void onResumeClick() {
                        getPresenter().resumeTrackRecording();
                        mapView.setLocationChangeNotification(true);
                    }
                },
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getPresenter().stopTrackRecording();
                        mapView.setLocationChangeNotification(false);
                    }
                }
        );
    }
}


