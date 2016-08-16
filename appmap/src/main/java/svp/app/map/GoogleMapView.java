package svp.app.map;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import svp.app.map.IMapView;
import svp.app.map.OnMapClickListener;
import svp.app.map.model.Point2D;

public class GoogleMapView implements IMapView, OnMapReadyCallback, GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener, GoogleMap.OnCameraChangeListener {
    private final String TAG = "GoogleMapView";
    private final FragmentActivity activity;
    private final int gmResourceId;
    private GoogleMap map;
    private OnMapClickListener listener;

    public GoogleMapView(FragmentActivity activity, int gmResourceId) {
        this.activity = activity;
        this.gmResourceId = gmResourceId;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setBuildingsEnabled(true);
        map.setIndoorEnabled(true);
//        enableMyLocation();

//        if(cameraPosition !=null){
//            map.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
//        }
        map.setOnCameraChangeListener(this);
        map.setOnMapClickListener(this);
        map.setOnMapLongClickListener(this);
//        new GoogleMap.OnMapLongClickListener() {
//            @Override
//            public void onMapLongClick(LatLng latLng) {
//                GoogleMapsPlaceService service = new GoogleMapsPlaceService();
//                service.getPlace(map.getProjection(), latLng,
//                        activity.getString(R.string.google_maps_key),
//                        new GoogleMapsPlaceService.IGetPlaceCallback() {
//
//                        });
                /*
                IndoorBuilding building = map.getFocusedBuilding();
                if (building != null) {
                    StringBuilder s = new StringBuilder();
                    for (IndoorLevel level : building.getLevels()) {
                        s.append(level.getName()).append(" ");
                    }
                    if (building.isUnderground()) {
                        s.append("is underground");
                    }
                    Toast.makeText(GoogleMapView.this, s.toString(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(GoogleMapView.this, "No visible building", Toast.LENGTH_SHORT).show();
                }*/
//            }
//        });
//        map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(51.5, -0.1)));
//        map.addPolyline(new PolylineOptions()
//                .add(new LatLng(51.5, -0.1), new LatLng(40.7, -74.0))
//                .width(5)
//                .color(Color.RED));
    }


    @Override
    public void onMapClick(LatLng latLng) {
        listener.onMapClick(new Point2D(latLng));
    }
    @Override
    public void onMapLongClick(LatLng latLng) {
        listener.onMapLongClick(new Point2D(latLng));
    }
    @Override
    public void onCameraChange(CameraPosition cameraPosition) {

    }

    /**
     * IMapView
     * */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        SupportMapFragment mapFragment = (SupportMapFragment)activity.getSupportFragmentManager()
                .findFragmentById(gmResourceId);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void setOnMapClickListener(OnMapClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void moveTo(Point2D point) {

        CameraPosition prevcp = map.getCameraPosition();
        CameraPosition cp = new CameraPosition(point.getLatLng(),prevcp.zoom,prevcp.tilt,prevcp.bearing);
        map.animateCamera(CameraUpdateFactory.newCameraPosition(cp));
//        map.moveCamera(CameraUpdateFactory.newCameraPosition(cp));
    }



}