package svp.com.dontmissplaces.presenters;


import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.CountDownTimer;

import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import com.svp.infrastructure.mvpvs.presenter.Presenter;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import svp.com.dontmissplaces.db.Repository;
import svp.com.dontmissplaces.model.Traffic;
import svp.com.dontmissplaces.ui.MapView;
import svp.com.dontmissplaces.utils.LocationEx;

public class MapsPresenter extends Presenter<MapView,MapView.ViewState> {
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

            Date currentDate = new Date(location.getTime());
            Date prevDate = new Date(prevLocation.getTime());

            long diff =  currentDate.getTime() - prevDate.getTime();
            long seconds = TimeUnit.MILLISECONDS.toSeconds(diff);
//            long minutes = TimeUnit.MILLISECONDS.toMinutes(diff);

            double speed = LocationEx.getSpeed(prevLocation, location);
            double dis = location.distanceTo(prevLocation);

            Traffic.SpeedTypes speedType = Traffic.walking.getSpeedType(speed, dis, diff);
            if(speedType == Traffic.SpeedTypes.Undefined){
                return;
            }

            double speed2 = dis/seconds;

            LatLng latl = LocationEx.getLatLng(location);


//            DecimalFormat myFormatter = new DecimalFormat("%f%n");
            String format ="%1$,.2f";
            state.getSnackbar(
                      "loc.min: " + String.format(format,location.getSpeed())
                    + " calc.min: " + String.format(format,speed) + "/" + String.format(format,speed2) + "\n"
                    + " dis: " + String.format(format,dis) + " secs: " + seconds)
                    .show();

//            state.addPolyline(new PolylineOptions()
//                    .add(new LatLng(prevLocation.getLatitude(), prevLocation.getLongitude()), latl)
//                    .width(5)
//                    .color(Color.BLUE)
//                    .geodesic(true));

            prevLocation = location;
        }catch (Exception ex){
//            Throwable cause = ex.getCause();
            throw ex;
        }
    }




}
