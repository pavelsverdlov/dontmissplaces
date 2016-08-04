package svp.com.dontmissplaces.ui.map;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.svp.infrastructure.mvpvs.view.View;

import java.util.Collection;

import svp.app.map.OnMapClickListener;
import svp.com.dontmissplaces.R;
import svp.com.dontmissplaces.model.Map.GoogleMapsPlaceService;
import svp.app.map.model.Point2D;
import svp.com.dontmissplaces.presenters.MapsPresenter;
import com.svp.infrastructure.ActivityPermissions;
import svp.app.map.model.IPOIView;
import svp.com.dontmissplaces.ui.model.SessionView;
import svp.com.dontmissplaces.ui.model.PolylineView;

public class GoogleMapView
        extends View<MapsPresenter>
        implements OnMapReadyCallback,
        GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMapClickListener,
        IDNMPMapView {
    private final String TAG = "GoogleMapView";
    private OnMapClickListener listener;

    public static class ViewState extends com.svp.infrastructure.mvpvs.viewstate.ViewState<GoogleMapView> implements IMapViewState {
        private final PolylineView polyline;
        public ViewState(GoogleMapView view) {
            super(view);
            polyline = new PolylineView(Color.BLUE,5);
        }

        public boolean checkPermissionFineLocation(){
            return view.permissions.checkPermissionFineLocation();
        }

        public void addPolyline(final PolylineView polyline){//final Collection<LatLng> lls){
            this.polyline.add(polyline);
            view.activity.runOnUiThread(new Runnable(){
                @Override
                public void run() {
                    polyline.addPolylineOptions(view.mMap);

                   // view.mMap.addCircle(new CircleOptions().center(lls.iterator().next()).radius(2).fillColor(Color.BLUE));
                }
            });
        }
        public void moveCamera(final LatLng latLng){
            view.activity.runOnUiThread(new Runnable(){
                @Override
                public void run() {
                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15);
                    view.mMap.animateCamera(cameraUpdate);
                    //view.mMap.addMarker(new MarkerOptions().position(latLng)/*.title("Marker in Sydney")*/);
//                    view.mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                }
            });
        }

        public void startLocationListening() { polyline.clear(); }
        public void stopLocationListening() {  }

        @Override
        protected void restore() {
            if(polyline.size() > 0){
                addPolyline(polyline);
                polyline.clear();
            }
        }

        @Override
        public void saveState() {

        }

        @Override
        public Activity getActivity() {
            return view.activity;
        }


        @NonNull
        private static PolylineOptions preparePolylineOptions(PolylineOptions options){
            return options
                    .width(5)
                    .color(Color.BLUE)
                    .geodesic(true);
        }
    }

    private GoogleMap mMap;
    public final FragmentActivity activity;
    private final ActivityPermissions permissions;

    public GoogleMapView(FragmentActivity activity, ActivityPermissions permissions){
        this.activity = activity;
        this.permissions = permissions;
    }

    public void onCreate(Bundle savedInstanceState) {
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment)activity.getSupportFragmentManager()
                .findFragmentById(R.id.google_map);

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
      //  mMap.set//setMapType();
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
//        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
//            @Override
//            public android.view.View getInfoWindow(Marker marker) {
//                marker.setTitle("TEST!");
//                marker.setSnippet("Snippet");
//                //marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.marker)));
//                marker.showInfoWindow();
//                //mMap.addMarker().
//
//                return null;
//            }
//
//            @Override
//            public android.view.View getInfoContents(Marker marker) {
//                return null;
//            }
//        });
        //http://maps.googleapis.com/maps/api/geocode/json?latlng=46.402852,30.722839&sensor=true
        mMap.setOnMapClickListener(this);
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
                    Toast.makeText(GoogleMapView.this, s.toString(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(GoogleMapView.this, "No visible building", Toast.LENGTH_SHORT).show();
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

    Marker marker;
    @Override
    public void onMapClick(LatLng latLng) {
        if(marker != null){
            marker.remove();
        }
        marker = mMap.addMarker(new MarkerOptions().position(latLng));
        listener.onMapClick(new Point2D(latLng));
    }

    public void setOnMapClickListener(OnMapClickListener listener){
        this.listener = listener;
    }

    @Override
    public void drawMarker(IPOIView pois) {

    }

    public void enableMyLocation() {
        if(permissions.isFineLocationGranted()) {
            mMap.setMyLocationEnabled(true);
//            getPresenter().permissionFineLocationReceived();
        }else {
            mMap.setMyLocationEnabled(false);
        }
    }
    public void startTrackRecording(SessionView session) {
        getPresenter().gpsStart(session);
    }
    public void stopTrackRecording() {
        getPresenter().gpsStop();
    }

    public void showSessionsTrack(Collection<SessionView> sessions) {
        getPresenter().showSessionsTrack(sessions);
    }
    public Point2D getMyLocation(){
        return new Point2D(mMap.getMyLocation());
    }

    public void moveTo(Point2D myLocation){
        //mMap.moveCamera();
    }
}
