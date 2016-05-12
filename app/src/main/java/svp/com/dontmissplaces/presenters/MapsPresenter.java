package svp.com.dontmissplaces.presenters;


import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import com.svp.infrastructure.mvpvs.presenter.Presenter;

import java.util.Date;
import java.util.Vector;
import java.util.concurrent.TimeUnit;

import svp.com.dontmissplaces.db.Repository;
import svp.com.dontmissplaces.db.Track;
import svp.com.dontmissplaces.db.Waypoint;
import svp.com.dontmissplaces.model.Traffic;
import svp.com.dontmissplaces.model.gps.GPSServiceProvider;
import svp.com.dontmissplaces.model.gps.OnLocationChangeListener;
import svp.com.dontmissplaces.ui.MapView;
import svp.com.dontmissplaces.utils.LocationEx;

public class MapsPresenter extends Presenter<MapView,MapView.ViewState> implements OnLocationChangeListener {
    private static final String TAG = "MapsPresenter";
    private Location prevLocation;
    private final Repository repository;
    private GPSServiceProvider gpsService;
    private Track track;

    public MapsPresenter(Repository repository) {
        this.repository = repository;
    }

    @Override
    protected void onAttachedView(MapView view){
        gpsService = new GPSServiceProvider(view.activity);
        if(state.checkPermissionFineLocation()) {
            gpsService.setOnLocationChangeListener(this);
        }
    }

    public void permissionFineLocationReceived(){
        if(prevLocation == null) {
           // prevLocation = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }
    }

    public void onMapReady(UiSettings mUiSettings) {
        //mUiSettings.setZoomControlsEnabled(true);
        mUiSettings.setCompassEnabled(true);
        mUiSettings.setMyLocationButtonEnabled(true);
        mUiSettings.setScrollGesturesEnabled(true);
        mUiSettings.setZoomGesturesEnabled(true);
        mUiSettings.setTiltGesturesEnabled(true);
        mUiSettings.setRotateGesturesEnabled(true);

        mUiSettings.setIndoorLevelPickerEnabled(true);

        if(prevLocation != null){
            state.moveCamera(LocationEx.getLatLng(prevLocation));
        }
    }

    public void gpsStart(Track track){
        this.track = track;
        gpsService.startup(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG,"GPS service startup");
            }
        });
        state.startLocationListening();
    }
    public void gpsStop() {
        gpsService.shutdown();
        Log.d(TAG,"GPS service shutdown");
        state.stopLocationListening();
    }

    @Override
    public void OnLocationChange(Location location) {
        try {
            Log.d(TAG,"OnLocationChange " + location);
            if(prevLocation == null){
                return;
            }

            Date currentDate = new Date(location.getTime());
            Date prevDate = new Date(prevLocation.getTime());

            long diff =  currentDate.getTime() - prevDate.getTime();
            //double speed = LocationEx.getSpeed(prevLocation, location);
            double dis = location.distanceTo(prevLocation);

//            Traffic.CustomMovement movement = Traffic.walking.createMovement(dis, TimeUnit.MILLISECONDS.toSeconds(diff));
//            if(movement.type == Traffic.SpeedTypes.Undefined){
//                return;
//            }
            repository.insertWaypoint(new Waypoint(track.id,location));
            Vector v =new Vector<LatLng>();
            v.add(LocationEx.getLatLng(prevLocation));
            v.add(LocationEx.getLatLng(location));
            state.addPolyline(v);

            //state.moveCamera(LocationEx.getLatLng(location));

        }catch (Exception ex){
            throw ex;
        }finally {
            prevLocation = location;
        }
    }


    public void showTrack(Track track) {
        Vector<Waypoint> waypoints = repository.getWaypoints(track);

        Vector<LatLng> points = new Vector<>();
        for (Waypoint w : waypoints){
            points.add(w.getLatLng());
        }

        state.addPolyline(points);
    }
}
