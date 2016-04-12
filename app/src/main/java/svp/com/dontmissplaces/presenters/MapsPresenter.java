package svp.com.dontmissplaces.presenters;


import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;

import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.PolylineOptions;
import com.svp.infrastructure.mvpvs.presenter.Presenter;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import svp.com.dontmissplaces.db.Repository;
import svp.com.dontmissplaces.model.Traffic;
import svp.com.dontmissplaces.ui.MapView;
import svp.com.dontmissplaces.utils.LocationEx;

public class MapsPresenter extends Presenter<MapView,MapView.ViewState> {
    private Location prevLocation;
    private final Repository repository;
    LocationManager mLocationManager;

    public MapsPresenter(Repository repository) {
        this.repository = repository;
    }

    @Override
    protected void onAttachedView(){
        mLocationManager = state.getLocationManager();
//        Also, it's a good practice to stop lisnerning when there is no need for that by
//        locationManager.removeUpdates(this);
        if(state.checkPermissionFineLocation()) {
            prevLocation = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }
    }
    @Override
    protected void onDetachedView() {
        //state.clearLocationManager(locationManager);.removeUpdates(this);
    }
    public void permissionFineLocationReceived(){
        if(prevLocation == null) {
            prevLocation = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
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

    public void test(){
        state.stateTest();
    }

    public void onMyLocationChange(Location location){
        try {
            if(prevLocation == null){
                return;
            }

            Date currentDate = new Date(location.getTime());
            Date prevDate = new Date(prevLocation.getTime());

            long diff =  currentDate.getTime() - prevDate.getTime();
            //double speed = LocationEx.getSpeed(prevLocation, location);
            double dis = location.distanceTo(prevLocation);

            Traffic.CustomMovement movement = Traffic.walking.createMovement(dis, TimeUnit.MILLISECONDS.toSeconds(diff));
            if(movement.type == Traffic.SpeedTypes.Undefined){
                return;
            }

//            double speed2 = dis/seconds;
//            DecimalFormat myFormatter = new DecimalFormat("%f%n");
//            String format ="%1$,.2f";
//            state.getSnackbar(
//                      "loc.min: " + String.format(format,location.getSpeed())
//                    + " calc.min: " + String.format(format,speed) + "/" + String.format(format,speed2) + "\n"
//                    + " dis: " + String.format(format,dis) + " secs: " + seconds)
//                    .show();

            state.addPolyline(new PolylineOptions()
                    .add(LocationEx.getLatLng(prevLocation), LocationEx.getLatLng(location))
                    .width(5)
                    .color(Color.BLUE)
                    .geodesic(true));


        }catch (Exception ex){
           Throwable cause = ex.getCause();
            //throw ex;
        }finally {
            prevLocation = location;
        }
    }




}
