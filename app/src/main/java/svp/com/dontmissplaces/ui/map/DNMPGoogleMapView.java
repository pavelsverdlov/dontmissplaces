package svp.com.dontmissplaces.ui.map;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import com.svp.infrastructure.mvpvs.view.View;

import java.util.Collection;
import java.util.UUID;

import svp.app.map.GoogleMapView;
import svp.app.map.OnMapClickListener;
import svp.com.dontmissplaces.R;
import svp.app.map.model.Point2D;
import svp.com.dontmissplaces.presenters.MapsPresenter;
import com.svp.infrastructure.ActivityPermissions;
import svp.app.map.model.IPOIView;
import svp.com.dontmissplaces.ui.model.SessionView;
import svp.com.dontmissplaces.ui.model.PolylineView;

public class DNMPGoogleMapView
        extends View<MapsPresenter>
    implements IDNMPMapView {
    private final String TAG = "DNMPGoogleMapView";

    public static class ViewState extends com.svp.infrastructure.mvpvs.viewstate.ViewState<DNMPGoogleMapView> implements IMapViewState {
        private final PolylineView polyline;
        public ViewState(DNMPGoogleMapView view) {
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
                  //  polyline.addPolylineOptions(view.mMap);

                   // view.mMap.addCircle(new CircleOptions().center(lls.iterator().next()).radius(2).fillColor(Color.BLUE));
                }
            });
        }
        public void moveCamera(final LatLng latLng){
            view.activity.runOnUiThread(new Runnable(){
                @Override
                public void run() {
                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15);
                    //view.mMap.animateCamera(cameraUpdate);
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

    public final FragmentActivity activity;
    private final ActivityPermissions permissions;
    private final GoogleMapView mapView;

    public DNMPGoogleMapView(FragmentActivity activity, ActivityPermissions permissions){
        this.activity = activity;
        this.permissions = permissions;
        mapView = new GoogleMapView(activity,R.id.google_map);
    }

    public void onCreate(Bundle savedInstanceState) {
        mapView.onCreate(savedInstanceState);
    }
    public void onResume(){ mapView.onResume();}


    public void setOnMapClickListener(OnMapClickListener listener){
        mapView.setOnMapClickListener(listener);
    }

    private UUID markerId;
    @Override
    public void drawMarker(IPOIView poi) {
        if(markerId != null){
            mapView.removeMarker(markerId);
        }
        markerId = mapView.addMarker(poi,0);
    }
    @Override
    public UUID addMarker(IPOIView poi, int markerIdResource){
        return mapView.addMarker(poi,markerIdResource);
    }

    @Override
    public void removeMarker(UUID id) {
        mapView.removeMarker(id);
    }

    public void enableMyLocation() {
        if(permissions.isFineLocationGranted()) {
           // mMap.setMyLocationEnabled(true);
//            getPresenter().permissionFineLocationReceived();
        }else {
//            mMap.setMyLocationEnabled(false);
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
    public void moveTo(Point2D p){
        mapView.moveTo(p);
    }
}
