package svp.app.map;

import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.svp.infrastructure.common.ViewExtensions;

import java.util.HashMap;
import java.util.UUID;

import svp.app.map.IMapView;
import svp.app.map.OnMapClickListener;
import svp.app.map.android.GMapPolyline;
import svp.app.map.model.IMapPolyline;
import svp.app.map.model.IPOIView;
import svp.app.map.model.Point2D;

public class GoogleMapView implements IMapView, MapZoomController.IMapZoom, OnMapReadyCallback,
        GoogleMap.OnMapClickListener,
        GoogleMap.OnMapLongClickListener,
        GoogleMap.OnCameraChangeListener {
    private final String TAG = "GoogleMapView";
    private final FragmentActivity activity;
    private final int gmResourceId;
    private GoogleMap map;
    private OnMapClickListener listener;
    private MapZoomController zoomController;
    private final HashMap<UUID, Marker> markers;
    SupportMapFragment mapFragment;

    public GoogleMapView(FragmentActivity activity, int gmResourceId) {
        this.activity = activity;
        this.gmResourceId = gmResourceId;
        markers = new HashMap<>();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setBuildingsEnabled(true);
        map.setIndoorEnabled(true);
        map.setMyLocationEnabled(true);
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
//                .draw(new LatLng(51.5, -0.1), new LatLng(40.7, -74.0))
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
        mapFragment = (SupportMapFragment)activity.getSupportFragmentManager()
                .findFragmentById(gmResourceId);
        mapFragment.getMapAsync(this);



        zoomController = new MapZoomController(activity,this);
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {
//        mapFragment.onDestroyView();
//        FragmentManager fm = activity.getFragmentManager();
//        fm.beginTransaction()
//                .remove(fm.findFragmentById(gmResourceId))
//                .commitAllowingStateLoss();
//        fm.executePendingTransactions();
    }

    @Override
    public void onResume() {

    }

    @Override
    public void setOnMapClickListener(OnMapClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void moveTo(final Point2D point) {
//        Handler handler = new Handler();
//        handler.post(new Runnable() {
//            @Override
//            public void run() {
//                CameraPosition prevcp = map.getCameraPosition();
            CameraPosition cp = CameraPosition.fromLatLngZoom(point.getLatLng(),16);
//                                CameraPosition cp = new CameraPosition(point.getLatLng(),prevcp.zoom,prevcp.tilt,prevcp.bearing);
            map.animateCamera(CameraUpdateFactory.newCameraPosition(cp));
//                map.moveCamera(CameraUpdateFactory.newCameraPosition(cp));
//            }
//        });

    }
    @Override
    public void removeMarker(UUID id){
        if(markers.containsKey(id)){
            markers.get(id).remove();
        }
    }

    @Override
    public IMapPolyline createPolyline() {
        return new GMapPolyline(map);
    }

    @Override
    public UUID addMarker(final IPOIView poi, int markerIdResource) {
//        Handler handler = new Handler();
//        handler.post(new Runnable() {
//            @Override
//            public void run() {

               Marker marker = map.addMarker(new MarkerOptions()//.snippet("dfdsfsd")
                        .position(poi.getPoint().getLatLng())
                        .icon(BitmapDescriptorFactory.defaultMarker())
                );
        UUID key = UUID.randomUUID();
        markers.put(key,marker);
//            }
//        });
        return key;
    }

    @Override
    public void setZoom(float val) {
        map.animateCamera( CameraUpdateFactory.zoomTo(val));
    }

    @Override
    public float getZoom() {
        return map.getCameraPosition().zoom;
    }

    public void addLine(){

    }
}
