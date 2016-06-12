package svp.com.dontmissplaces.ui.map;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;

import com.google.android.gms.maps.model.*;
import com.svp.infrastructure.mvpvs.view.IActivityView;
import com.svp.infrastructure.mvpvs.view.View;

import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.location.*;
import org.osmdroid.events.DelayedMapListener;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.events.MapListener;
import org.osmdroid.events.ScrollEvent;
import org.osmdroid.events.ZoomEvent;
import org.osmdroid.tileprovider.tilesource.OnlineTileSourceBase;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.BoundingBoxE6;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.*;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;
import org.osmdroid.views.overlay.compass.*;
import org.osmdroid.views.overlay.mylocation.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Vector;

import svp.com.dontmissplaces.R;
import svp.com.dontmissplaces.db.Place;
import svp.com.dontmissplaces.model.Map.Point2D;
import svp.com.dontmissplaces.model.nominatim.PhraseProvider;
import svp.com.dontmissplaces.model.nominatim.PointsOfInterestInsiteBoxTask;
import svp.com.dontmissplaces.presenters.MapsPresenter;
import svp.com.dontmissplaces.ui.ActivityPermissions;
import svp.com.dontmissplaces.ui.model.PolylineView;
import svp.com.dontmissplaces.ui.model.SessionView;

public class OsmdroidMapView extends View<MapsPresenter> implements IMapView, MapEventsReceiver, MapListener {
    private final Activity activity;
    private final ActivityPermissions permissions;
    private final MapView mapView;
    private final IMapController mapController;
    private final MyLocationNewOverlay myLocationOverlay;
    private final GpsMyLocationProvider gpsMyLocationProvider;
    private final FolderOverlay poiMarkers;
    private final CompassOverlay compassOverlay;

    public static class ViewState extends com.svp.infrastructure.mvpvs.viewstate.ViewState<OsmdroidMapView> implements IMapViewState {
        private final PolylineView polyline;

        public ViewState(OsmdroidMapView view) {
            super(view);
            polyline = new PolylineView(Color.BLUE, 5);
        }

        public boolean checkPermissionFineLocation() {
            return true;
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
        }

        @Override
        public Activity getActivity() {
            return view.activity;
        }

    }

    private OnMapClickListener clickListener;

    public OsmdroidMapView(Activity activity, ActivityPermissions permissions) {
        this.activity = activity;
        this.permissions = permissions;

        mapView = (MapView) activity.findViewById(R.id.osmdroid_map);
        mapView.setMapListener(new DelayedMapListener(this, 100));
        setMapViewSettings();

        compassOverlay = new CompassOverlay(activity, new InternalCompassOrientationProvider(activity),
                mapView);
        compassOverlay.enableCompass();

        mapController = mapView.getController();
        poiMarkers = new FolderOverlay(activity);

        MapEventsOverlay mapEventsOverlay = new MapEventsOverlay(activity, this);

        gpsMyLocationProvider = new GpsMyLocationProvider(activity);
        myLocationOverlay = new MyLocationNewOverlay(gpsMyLocationProvider, mapView);
        setMyLocationUpdate(1000);

        mapController.setZoom(12);

        mapView.getOverlays().add(mapEventsOverlay);
        mapView.getOverlays().add(compassOverlay);
        mapView.getOverlays().add(poiMarkers);
        mapView.getOverlays().add(myLocationOverlay);
        mapView.invalidate();
    }

    /**
     * IMapView
     */
    @Override
    public void showSessionsTrack(Collection<SessionView> sessions) {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        permissions.checkPermissionExternalStorage();
        permissions.checkPermissionFineLocation();




/*
        GpsMyLocationProvider gpsLocationProvider = new GpsMyLocationProvider(activity);
        gpsLocationProvider.setLocationUpdateMinDistance(5);
        gpsLocationProvider.setLocationUpdateMinTime(20);


        locationOverlay = new MyLocationNewOverlay(activity, gpsLocationProvider , mapView);
        locationOverlay.enableMyLocation(gpsLocationProvider);
        locationOverlay.runOnFirstFix(new Runnable() {
            public void run() {
                mapController.animateTo(locationOverlay
                        .getMyLocation());
            }
        });

        mapView.getOverlays().add(locationOverlay);

        LocationManager locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, gpsLocationProvider );
        //locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, myLocationListener);
        */
    }

    @Override
    public void onStart() {
        super.onStart();
        GeoPoint startPoint = new GeoPoint(48.8583, 2.2944);
        //setCenter
        mapController.animateTo(startPoint);
    }

    @Override
    public void onStop() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void enableMyLocation() {

    }

    @Override
    public void startTrackRecording(SessionView session) {

    }

    @Override
    public void pauseTrackRecording() {

    }

    @Override
    public void resumeTrackRecording() {

    }

    @Override
    public void stopTrackRecording() {

    }

    @Override
    public Location getMyLocation() {
        return myLocationOverlay.getMyLocation().l;
    }

    @Override
    public void setOnMapClickListener(OnMapClickListener listener) {
        clickListener = listener;
    }

    public void drawMarker(Place poi) {
        if (poi != null) {
            for (Overlay m : poiMarkers.getItems()) {
                poiMarkers.remove(m);
            }
            Marker poiMarker = new Marker(mapView);
            poiMarker.setTitle(poi.nominatimType);
            poiMarker.setSnippet(poi.title);
            poiMarker.setIcon(activity.getResources().getDrawable(R.drawable.map_marker));
            poiMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
            poiMarker.setPosition(new GeoPoint(poi.latitude, poi.longitude));
            poiMarkers.add(poiMarker);

            mapView.postInvalidate();
        }
    }

    /** Click handlers */

    @Override
    public boolean singleTapConfirmedHelper(GeoPoint p) {
        clickListener.onMapClick(new Point2D(p));
        return false;
    }

    @Override
    public boolean longPressHelper(GeoPoint p) {
        return false;
    }

    /**  Map listeners */

    @Override
    public boolean onScroll(ScrollEvent event) {
        int zoom = mapView.getZoomLevel();
        BoundingBoxE6 box = mapView.getBoundingBox();
        clickListener.onScroll(zoom,box);
        return true;
    }
    @Override
    public boolean onZoom(ZoomEvent event) {
        int zoom = event.getZoomLevel();
        BoundingBoxE6 box = mapView.getBoundingBox();
        permissions.checkPermissionNetwork();

        clickListener.onZoom(zoom,box);
        return true;
    }

    /** My location handlers */

    private void setMyLocationUpdate(long milliSeconds) {
        myLocationOverlay.enableMyLocation();
        myLocationOverlay.setDrawAccuracyEnabled(true);

        gpsMyLocationProvider.startLocationProvider(myLocationOverlay);
        gpsMyLocationProvider.setLocationUpdateMinDistance(100);
        gpsMyLocationProvider.setLocationUpdateMinTime(milliSeconds);
    }

    private void setMapViewSettings() {
        //get value from settings
        String titleSourceType = "1";
        OnlineTileSourceBase tileSource;
        //TODO: refactor to enum
        switch (titleSourceType) {
            case "mapnik":
                tileSource = TileSourceFactory.MAPNIK;
                break;
            case "cyclemap":
                tileSource = TileSourceFactory.CYCLEMAP;
                break;
            case "osmpublictransport":
                //show all rouds on top
                tileSource = TileSourceFactory.PUBLIC_TRANSPORT;
                break;
            case "mapquestosm":
                //mentro station on top most
                tileSource = TileSourceFactory.MAPQUESTOSM;
                break;
            default:
                tileSource = TileSourceFactory.DEFAULT_TILE_SOURCE;
                break;
        }

        mapView.setTileSource(tileSource);
        //set auto resize text titles on map
        mapView.setTilesScaledToDpi(true);
        mapView.setBuiltInZoomControls(true);
        mapView.setMultiTouchControls(true);
        mapView.setBuiltInZoomControls(true);

        mapView.setMinZoomLevel(3);
        mapView.setMaxZoomLevel(18); // Latest OSM can go to 21!
        mapView.getTileProvider().createTileCache();
    }


    Marker startMarker;
    private void drawMarker(GeoPoint p) {
        if (startMarker != null) {
            startMarker.remove(mapView);
        }
        startMarker = new Marker(mapView);
        startMarker.setIcon(activity.getResources().getDrawable(R.drawable.map_marker));
        //setTitle is displayed when clicking on marker
        //startMarker.setTitle("Start point");
        startMarker.setPosition(p);
        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        mapView.getOverlays().add(startMarker);

        mapView.invalidate();
    }

    private void drawLine(GeoPoint s, GeoPoint e) {
        ArrayList<GeoPoint> pp = new ArrayList<GeoPoint>();
        pp.add(s);
        pp.add(e);
        Polyline roadOverlay = new Polyline(activity);
        roadOverlay.setColor(0x800000FF);
        roadOverlay.setWidth(10);
        roadOverlay.setPoints(pp);

        mapView.getOverlays().add(roadOverlay);
        mapView.invalidate();
    }
/*
    private class MyItemizedIconOverlay extends ItemizedIconOverlay<OverlayItem> {
        public MyItemizedIconOverlay(
                List<OverlayItem> pList,
                org.osmdroid.views.overlay.ItemizedIconOverlay.OnItemGestureListener<OverlayItem> pOnItemGestureListener,
                ResourceProxy pResourceProxy) {
            super(pList, pOnItemGestureListener, pResourceProxy);
            // TODO Auto-generated constructor stub
        }

        @Override
        public void draw(Canvas canvas, MapView mapview, boolean arg2) {
            // TODO Auto-generated method stub
            super.draw(canvas, mapview, arg2);

            if (!overlayItemArray.isEmpty()) {

                //overlayItemArray have only ONE element only, so I hard code to get(0)
                IGeoPoint in = overlayItemArray.get(0).getPoint();

                Point out = new Point();
                mapview.getProjection().toPixels(in, out);

                Bitmap bm = BitmapFactory.decodeResource(activity.getResources(), R.drawable.ic_done_white_24dp);
                canvas.drawBitmap(bm,
                        out.x - bm.getWidth() / 2,  //shift the bitmap center
                        out.y - bm.getHeight() / 2,  //shift the bitmap center
                        null);
            }
        }
    }
    */
}
