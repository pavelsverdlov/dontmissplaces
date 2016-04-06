package svp.com.dontmissplaces.presenters;


import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.widget.Toast;

import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import com.svp.infrastructure.mvpvs.presenter.Presenter;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import svp.com.dontmissplaces.MapsActivity;
import svp.com.dontmissplaces.db.Repository;
import svp.com.dontmissplaces.utils.LocationEx;

public class MapsPresenter extends Presenter<MapsActivity,MapsActivity.ViewState> {
    private Location prevLocation;
    private final Repository repository;

    public MapsPresenter(Repository repository) {
        this.repository = repository;
    }

    @Override
    protected void onAttachedView(){
        LocationManager mLocationManager = state.getLocationManager();
//        Also, it's a good practice to stop lisnerning when there is no need for that by
//        locationManager.removeUpdates(this);
        prevLocation = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
    }
    public void onMapReady(UiSettings mUiSettings) {
        mUiSettings.setZoomControlsEnabled(true);
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
            Date currentDate = new Date(location.getTime());
            Date prevDate = new Date(prevLocation.getTime());

            long diff =  currentDate.getTime() - prevDate.getTime();
            long seconds = TimeUnit.MILLISECONDS.toSeconds(diff);
//            long minutes = TimeUnit.MILLISECONDS.toMinutes(diff);

            double speed = LocationEx.getSpeed(prevLocation, location);
            double dis = location.distanceTo(prevLocation);

            double speed2 = dis/seconds;

            LatLng latl = LocationEx.getLatLng(location);

            state.getToast(latl.latitude + " | " + latl.longitude + "\n"
                    + prevLocation.getLatitude() + " | " + prevLocation.getLongitude() + "\n"
                    + "speed: " + speed2 + "m/s  dis: " + dis + "meters " + "\n"
                    + currentDate.toString())
                    .show();

            state.addPolyline(new PolylineOptions()
                    .add(new LatLng(prevLocation.getLatitude(), prevLocation.getLongitude()), latl)
                    .width(5)
                    .color(Color.BLUE)
                    .geodesic(true));

            prevLocation = location;
        }catch (Exception ex){
            Throwable cause = ex.getCause();
        }
    }



}
