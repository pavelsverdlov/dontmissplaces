package svp.com.dontmissplaces.ui.map;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;

import com.google.android.gms.maps.model.LatLng;
import com.svp.infrastructure.mvpvs.view.View;

import org.osmdroid.api.IGeoPoint;

import java.util.Collection;

import svp.app.map.OnMapClickListener;
import svp.app.map.OsmdroidMapView;
import svp.app.map.android.gps.IGPSProvider;
import svp.com.dontmissplaces.R;
import svp.app.map.model.Point2D;
import svp.com.dontmissplaces.presenters.MapsPresenter;
import com.svp.infrastructure.ActivityPermissions;
import svp.app.map.model.IPOIView;
import svp.com.dontmissplaces.ui.model.PolylineView;
import svp.com.dontmissplaces.ui.model.SessionView;

public class DNMPOsmdroidMapView extends View<MapsPresenter> implements IDNMPMapView{
    private final Activity activity;
    private final ActivityPermissions permissions;

//    private final MapView mapView;
//    private final IMapController mapController;
//    private final GpsLocationProvider gpsProvider;
//    private final FolderOverlay poiMarkers;
//    private final CompassOverlay compassOverlay;

    public static class ViewState extends com.svp.infrastructure.mvpvs.viewstate.ViewState<DNMPOsmdroidMapView> implements IMapViewState {
        private final PolylineView polyline;
        private IGeoPoint startPoint;
        private int zoomLevel;

        public ViewState(DNMPOsmdroidMapView view) {
            super(view);
            polyline = new PolylineView(Color.BLUE, 5);
            zoomLevel = -1;
        }

        public boolean checkPermissionFineLocation() {
            return view.permissions.checkPermissionFineLocation();
        }

        public void addPolyline(final PolylineView polyline) {//final Collection<LatLng> lls){
        }

        public void moveCamera(final LatLng latLng) {
        }

        public void startLocationListening() {
            polyline.clear();
        }

        public void stopLocationListening() {
        }

        @Override
        protected void restore() {
            if (polyline.size() > 0) {
                addPolyline(polyline);
                polyline.clear();
            }
            if(startPoint == null){
                Point2D point = view.osmdroidMapView.gpsProvider.getMyLocation();
                if(!point.isEmpty()){
                    startPoint = point.getGeoPoint();
                }
            }
            if(startPoint != null) {
                view.osmdroidMapView.mapController.animateTo(startPoint);
            }
            if(zoomLevel != -1){
                view.osmdroidMapView.mapController.setZoom(zoomLevel);
            }
        }

        public void saveState(){
            startPoint = view.osmdroidMapView.mapView.getMapCenter();
            zoomLevel = view.osmdroidMapView.mapView.getZoomLevel();
        }

        @Override
        public Activity getActivity() {
            return view.activity;
        }

    }

//    private OnMapClickListener clickListener;
//    @Bind(R.id.map_zoom_plus_fab)
//    FloatingActionButton fabZoomPlus;
//    @Bind(R.id.map_zoom_minus_fab)
//    FloatingActionButton fabZoomMinus;

    private final OsmdroidMapView osmdroidMapView;

    public DNMPOsmdroidMapView(Activity activity, ActivityPermissions permissions,IGPSProvider gps) {
        this.activity = activity;
        this.permissions = permissions;
        osmdroidMapView = new OsmdroidMapView(activity, R.id.osmdroid_map, gps);
//
//        ButterKnife.bind(this, activity);
//
//        mapView = (MapView) activity.findViewById(R.id.osmdroid_map);
//        mapView.setMapListener(new DelayedMapListener(this, 100));
//        setMapViewSettings();
//
//        compassOverlay = new CompassOverlay(activity, new InternalCompassOrientationProvider(activity),
//                mapView);
//        compassOverlay.enableCompass();
//
//        mapController = mapView.getController();
//        poiMarkers = new FolderOverlay(activity);
//
//        MapEventsOverlay mapEventsOverlay = new MapEventsOverlay(activity, this);
//
//        gpsProvider = new GpsLocationProvider();
//
//        mapController.setZoom(12);
//
//        mapView.getOverlays().add(mapEventsOverlay);
//        mapView.getOverlays().add(compassOverlay);
//        mapView.getOverlays().add(poiMarkers);
//        mapView.invalidate();
//
//        fabZoomPlus.setOnClickListener(this);
//        fabZoomMinus.setOnClickListener(this);
    }

//    @Override
//    public void onClick(android.view.View v) {
//        switch (v.getId()) {
//            case R.id.map_zoom_plus_fab:
//                mapController.setZoom(mapView.getZoomLevel() + 1);
//                break;
//            case R.id.map_zoom_minus_fab:
//                mapController.setZoom(mapView.getZoomLevel() - 1);
//                break;
//        }
//    }

    /**
     * IDNMPMapView
     */
    @Override
    public void showSessionsTrack(Collection<SessionView> sessions) {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        osmdroidMapView.onCreate(savedInstanceState);
        permissions.checkPermissionExternalStorage();
        permissions.checkPermissionFineLocation();
    }

    @Override
    public void onStart() {
        super.onStart();
        osmdroidMapView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        osmdroidMapView.onStop();
    }

    @Override
    public void onResume() {
        osmdroidMapView.onResume();
    }

    @Override
    public void enableMyLocation() {

    }

    @Override
    public void startTrackRecording(SessionView session) {

    }


    @Override
    public void stopTrackRecording() {

    }

    @Override
    public void moveTo(Point2D p) {
        osmdroidMapView.moveTo(p);
    }

    @Override
    public void drawMarker(IPOIView poi, int markerIdResource) {
        osmdroidMapView.drawMarker(poi,markerIdResource);
    }

    @Override
    public void setOnMapClickListener(OnMapClickListener listener) {
        osmdroidMapView.setOnMapClickListener(listener);
    }

    public void drawMarker(IPOIView poi) {
        drawMarker(poi,R.drawable.map_marker);
    }
}
