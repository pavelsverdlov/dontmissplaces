package svp.com.dontmissplaces.presenters;


import android.location.Location;
import android.util.Log;

import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.svp.infrastructure.mvpvs.presenter.Presenter;

import java.util.Collection;
import java.util.Date;

import svp.com.dontmissplaces.db.Repository;
import svp.com.dontmissplaces.db.Waypoint;
import svp.com.dontmissplaces.model.gps.GPSServiceProvider;
import svp.com.dontmissplaces.model.gps.OnLocationChangeListener;
import svp.com.dontmissplaces.ui.map.GoogleMapView;
import svp.com.dontmissplaces.ui.map.IMapView;
import svp.com.dontmissplaces.ui.map.IMapViewState;
import svp.com.dontmissplaces.ui.model.PolylineView;
import svp.com.dontmissplaces.ui.model.SessionView;
import svp.com.dontmissplaces.utils.LocationEx;

public class MapsPresenter extends Presenter<IMapView,IMapViewState> implements OnLocationChangeListener {
    private static final String TAG = "MapsPresenter";
    private Location prevLocation;
    private Waypoint prevWaypoint;
    private final Repository repository;
    private GPSServiceProvider gpsService;
    private SessionView sessionTrack;

    public MapsPresenter(Repository repository) {
        this.repository = repository;
    }

    @Override
    protected void onAttachedView(IMapView view){
        gpsService = new GPSServiceProvider(state.getActivity());
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

    public void gpsStart(SessionView session){
        this.sessionTrack = session;
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
        Waypoint waypoint = null;
        try {
            Log.d(TAG,"OnLocationChange " + location);

            if(prevLocation == null){
                return;
            }
            waypoint = createWaypoint(location);

            Date currentDate = new Date(location.getTime());
            Date prevDate = new Date(prevLocation.getTime());

            long diff =  currentDate.getTime() - prevDate.getTime();
            //double speed = LocationEx.getSpeed(prevLocation, location);
            double dis = location.distanceTo(prevLocation);

//            Traffic.CustomMovement movement = Traffic.walking.createMovement(dis, TimeUnit.MILLISECONDS.toSeconds(diff));
//            if(movement.type == Traffic.SpeedTypes.Undefined){
//                return;
//            }
            state.addPolyline(PolylineView.create(prevWaypoint, waypoint));

            //state.moveCamera(LocationEx.getLatLng(location));
        }catch (Exception ex){
            throw ex;
        }finally {
            prevWaypoint = waypoint;
            prevLocation = location;
        }
    }

    private Waypoint createWaypoint(Location location){
        Waypoint waypoint = new Waypoint(sessionTrack.getId(),location);
        repository.track.insertWaypoint(waypoint);
        return waypoint;
    }

    public void showSessionsTrack(Collection<SessionView> sessions) {
        PolylineView polyline = null;
        LatLng first = null;
        for (SessionView s : sessions) {
            PolylineView p = PolylineView.create(s.waypoints);
            if(polyline == null) {
                first = s.waypoints.firstElement().getLatLng();
                polyline = p;
            }else{
                polyline.add(p);
            }
        }

        state.addPolyline(polyline);
        state.moveCamera(first);
    }
}
