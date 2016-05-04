package svp.com.dontmissplaces.ui;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.svp.infrastructure.mvpvs.view.View;

import svp.com.dontmissplaces.R;
import svp.com.dontmissplaces.model.Map.GoogleMapsPlaceService;
import svp.com.dontmissplaces.model.gps.GPSServiceProvider;
import svp.com.dontmissplaces.presenters.MapsPresenter;

public class MapView
        extends View<MapsPresenter>
        implements OnMapReadyCallback,
        GoogleMap.OnMyLocationButtonClickListener {
    private final String TAG = "MapView";

    public static class ViewState extends com.svp.infrastructure.mvpvs.viewstate.ViewState<MapView> {

        public ViewState(MapView view) {
            super(view);
        }

        public boolean checkPermissionFineLocation(){
            return view.permissions.checkPermissionFineLocation();
        }

        public void addPolyline(final PolylineOptions options){
            view.activity.runOnUiThread(new Runnable(){
                @Override
                public void run() {
                    view.mMap.addPolyline(options);
//                    view.mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(51.5, -0.1)));
//                    view.mMap.addPolyline(new PolylineOptions()
//                .add(new LatLng(51.5, -0.1), new LatLng(40.7, -74.0))
//                .width(5)
//                .color(Color.RED));
                }
            });
        }
        public void moveCamera(final LatLng latLng){
            view.activity.runOnUiThread(new Runnable(){
                @Override
                public void run() {
                    //view.mMap.addMarker(new MarkerOptions().position(latLng)/*.title("Marker in Sydney")*/);
                    view.mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                }
            });
        }


        @Override
        protected void restore() {

        }

        @Override
        public Activity getActivity() {
            return view.activity;
        }

        public void stateTest() {
            Snackbar.make(view.activity.getWindow().getDecorView().getRootView(),
                    "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
    }

    private GoogleMap mMap;
    public final FragmentActivity activity;
    private final ActivityPermissions permissions;

    public MapView(FragmentActivity activity, ActivityPermissions permissions){
        this.activity = activity;
        this.permissions = permissions;

    }

    public void onCreate(Bundle savedInstanceState) {
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment)activity.getSupportFragmentManager()
                .findFragmentById(R.id.map);

        if (savedInstanceState == null) {
            // First incarnation of this activity.
            mapFragment.setRetainInstance(true);
        }
        mapFragment.getMapAsync(this);
    }
    public void onDestroy(){}
    public void onResume(){}

    static CameraPosition cameraPosition;

    /**
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setBuildingsEnabled(true);
        mMap.setIndoorEnabled(true);
        //mMap.setMyLocationEnabled(true);
        //mMap.setTrafficEnabled(false);

        getPresenter().onMapReady(mMap.getUiSettings());

        enableMyLocation();

        if(cameraPosition !=null){
            mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }

        mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cp) {
                cameraPosition = cp;
            }
        });
       // mMap.setOnMyLocationButtonClickListener(this);

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                GoogleMapsPlaceService service = new GoogleMapsPlaceService();
                service.getPlace(mMap.getProjection(), latLng,
                        activity.getString(R.string.google_maps_key),
                        new GoogleMapsPlaceService.IGetPlaceCallback() {

                        });
                /*
                IndoorBuilding building = mMap.getFocusedBuilding();
                if (building != null) {
                    StringBuilder s = new StringBuilder();
                    for (IndoorLevel level : building.getLevels()) {
                        s.append(level.getName()).append(" ");
                    }
                    if (building.isUnderground()) {
                        s.append("is underground");
                    }
                    Toast.makeText(MapView.this, s.toString(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MapView.this, "No visible building", Toast.LENGTH_SHORT).show();
                }*/
            }
        });
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(51.5, -0.1)));
//        mMap.addPolyline(new PolylineOptions()
//                .add(new LatLng(51.5, -0.1), new LatLng(40.7, -74.0))
//                .width(5)
//                .color(Color.RED));
    }
    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(activity, "MyLocation button clicked", Toast.LENGTH_SHORT).show();
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false;
    }

    public void enableMyLocation() {
        if(permissions.isFineLocationGranted()) {
            mMap.setMyLocationEnabled(true);
            getPresenter().permissionFineLocationReceived();
        }else {
            mMap.setMyLocationEnabled(false);
        }
    }
    public void startTrackRecording() {
        getPresenter().gpsStart();
    }
    public void stopTrackRecording() {
        getPresenter().gpsStop();
    }
    public void resumeTrackRecording() {

    }
    public void pauseTrackRecording() {

    }
}
