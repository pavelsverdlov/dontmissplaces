package svp.com.dontmissplaces.ui.map;

import android.app.Activity;
import android.graphics.Color;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import android.location.Address;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.PolylineOptions;
import com.svp.infrastructure.mvpvs.view.View;

import java.util.Collection;
import java.util.List;
import java.util.Locale;

import svp.com.dontmissplaces.R;
import svp.com.dontmissplaces.model.Map.GoogleMapsPlaceService;
import svp.com.dontmissplaces.presenters.MapsPresenter;
import svp.com.dontmissplaces.ui.ActivityPermissions;
import svp.com.dontmissplaces.ui.model.SessionView;
import svp.com.dontmissplaces.ui.model.PolylineView;

public class MapView
        extends View<MapsPresenter>
        implements OnMapReadyCallback,
        GoogleMap.OnMyLocationButtonClickListener,
        IMapView {
    private final String TAG = "MapView";

    public static class ViewState extends com.svp.infrastructure.mvpvs.viewstate.ViewState<MapView> {
        private final PolylineView polyline;
        public ViewState(MapView view) {
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

    public MapView(FragmentActivity activity, ActivityPermissions permissions){
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
        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public android.view.View getInfoWindow(Marker marker) {
                marker.setTitle("TEST!");
                marker.setSnippet("Snippet");
                //marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.marker)));
                marker.showInfoWindow();
                //mMap.addMarker().

                return null;
            }

            @Override
            public android.view.View getInfoContents(Marker marker) {
                return null;
            }
        });
        //http://maps.googleapis.com/maps/api/geocode/json?latlng=46.402852,30.722839&sensor=true
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                String strAdd = "";
                Geocoder geocoder = new Geocoder(activity, Locale.getDefault());
                try {
                    List<android.location.Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                    if (addresses != null) {
                        Address returnedAddress = addresses.get(0);
                        String city = addresses.get(0).getLocality();
                        String state = addresses.get(0).getAdminArea();
                        String country = addresses.get(0).getCountryName();
                        String postalCode = addresses.get(0).getPostalCode();
                        String knownName = addresses.get(0).getFeatureName();
                        StringBuilder strReturnedAddress = new StringBuilder("");

                        for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                            strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                        }
                        strAdd = strReturnedAddress.toString();
                        Log.w("My Current loction address", "" + strReturnedAddress.toString());
                    } else {
                        Log.w("My Current loction address", "No Address returned!");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.w("My Current loction address", "Canont get Address!");
                }
                String test = strAdd;
            }
        });

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
    public void startTrackRecording(SessionView session) {
        getPresenter().gpsStart(session);
    }
    public void stopTrackRecording() {
        getPresenter().gpsStop();
    }
    public void resumeTrackRecording() {

    }
    public void pauseTrackRecording() {

    }
    public void showSessionsTrack(Collection<SessionView> sessions) {
        getPresenter().showSessionsTrack(sessions);
    }
}
