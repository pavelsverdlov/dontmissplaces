package svp.com.dontmissplaces.ui.map;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;

import com.google.android.gms.maps.model.LatLng;
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
import org.osmdroid.views.overlay.compass.*;
import org.osmdroid.views.overlay.mylocation.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

import svp.com.dontmissplaces.R;
import svp.com.dontmissplaces.model.nominatim.PhraseProvider;
import svp.com.dontmissplaces.model.nominatim.PointsOfInterestInsiteBoxTask;
import svp.com.dontmissplaces.presenters.MapsPresenter;
import svp.com.dontmissplaces.ui.ActivityPermissions;
import svp.com.dontmissplaces.ui.model.PolylineView;
import svp.com.dontmissplaces.ui.model.SessionView;

public class OsmdroidMapView extends View<MapsPresenter> implements IMapView, MapEventsReceiver,MapListener {
    private final Activity activity;
    private final ActivityPermissions permissions;
    private final MapView mapView;
    private final IMapController mapController;
    private final MyLocationNewOverlay myLocationOverlay;
    private final GpsMyLocationProvider gpsMyLocationProvider;
    private final FolderOverlay poiMarkers;
    private final CompassOverlay compassOverlay;

    public static class ViewState extends com.svp.infrastructure.mvpvs.viewstate.ViewState<OsmdroidMapView> {
        private final PolylineView polyline;
        public ViewState(OsmdroidMapView view) {
            super(view);
            polyline = new PolylineView(Color.BLUE,5);
        }

        public boolean checkPermissionFineLocation(){
            return true;
        }

        public void addPolyline(final PolylineView polyline){//final Collection<LatLng> lls){
        }
        public void moveCamera(final LatLng latLng){
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

    }

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
        return null;
    }

    @Override
    public void setOnMapClickListener(OnMapClickListener listener) {

    }


    @Override
    public boolean singleTapConfirmedHelper(GeoPoint p) {
        int maxD = 100;
        BoundingBoxE6 bb = new BoundingBoxE6(p.getLatitudeE6()+maxD,
                p.getLongitudeE6()+maxD,
                p.getLatitudeE6()-maxD,
                p.getLongitudeE6()-maxD);
        //drawMarker(p);
        ArrayList<GeoPoint> found =new ArrayList<>();
        for (GeoPoint pp :poiLocationLoaded.keySet()){
            if(bb.contains(pp)){
                found.add(pp);
            }
        }
        for (GeoPoint pp : found){
            Marker m = poiLocationLoaded.get(pp);
            m.setEnabled(true);
            poiMarkers.add(m);
        }
        mapView.invalidate();
       // new GeocoderNominatim()

        return false;
    }

    @Override
    public boolean longPressHelper(GeoPoint p) {
        return false;
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

    /**
    * Map listeners
    * */
    @Override
    public boolean onScroll(ScrollEvent event) {


        return true;
    }

    @Override
    public boolean onZoom(ZoomEvent event) {
        int zoom = event.getZoomLevel();

        final BoundingBoxE6 box = mapView.getBoundingBox();

        PhraseProvider pp = new PhraseProvider();

        ArrayList<PointsOfInterestInsiteBoxTask.InputData> datas = new ArrayList<>();
        for (String phrase : pp.getPhrases(zoom)){
            datas.add(new PointsOfInterestInsiteBoxTask.InputData(box,phrase,50));
        }

        if(datas.size() == 0){
            return true;
        }

        permissions.checkPermissionNetwork();
        new PointsOfInterestInsiteBoxTask(){
            @Override
            protected void processing(ArrayList<POI> poi, InputData data){

            }
        }.execute(datas);

/*
        getPOIs(new PointsOfInterestTask() {
            @Override
            protected ArrayList<POI> getPOI(InputData data) {
//                OverpassAPIProvider overpass = new OverpassAPIProvider();
//                String url  =overpass.urlForPOISearch(data.keyword,data.box,500,5);
//                ArrayList<POI> poi = overpass.getPOIsFromUrl(url);

                NominatimPOIProvider poiProvider = new NominatimPOIProvider(NominatimPOIProvider.NOMINATIM_POI_SERVICE);
                return poiProvider.getPOIInside(data.box, data.keyword ,data.maxResults);
            }
        },datas);
*/
        return true;
    }

    /**
     * Points of interest
     */
    private void getPOIs(GeoPoint startPoint) {
//        permissions.checkPermissionNetwork();
//        InputData input = new InputData(startPoint, "cinema", 50, 0.1);
//        new PointsOfInterestInsiteBoxTask().execute(input);
    }
    private void getPOIs(PointsOfInterestTask task, ArrayList<InputData> data) {
        permissions.checkPermissionNetwork();
        task.execute(data);
    }

    private final HashSet<Long> poiLoaded = new HashSet<>();
    private final HashMap<GeoPoint,Marker> poiLocationLoaded = new HashMap<>();

    public final class InputData {
        public final GeoPoint point;
        public final String keyword;
        public final int maxResults;
        public final double maxDistance;
        public final BoundingBoxE6 box;

        /**
         * @param maxDistance to the position, in degrees.
         *                    Note that it is used to build a bounding box around the position, not a circle.
         */
        public InputData(GeoPoint point, String keyword, int maxResults, double maxDistance) {
            this.box = null;
            this.point = point;
            this.keyword = keyword;
            this.maxResults = maxResults;
            this.maxDistance = maxDistance;
        }
        public InputData(BoundingBoxE6 box, String keyword, int maxResults) {
            this.point = null;
            this.box = box;
            this.keyword = keyword;
            this.maxResults = maxResults;
            this.maxDistance = 0;
        }
    }

    private abstract class PointsOfInterestTask extends AsyncTask<ArrayList<InputData>, Void, Void> {

        protected abstract ArrayList<POI> getPOI(InputData data);

        @Override
        protected Void doInBackground(ArrayList<InputData>... params) {
            try {
                ArrayList<InputData> datas = params[0];
                for (InputData data : datas) {
                    ArrayList<POI> pois = getPOI(data);
                    //poiProvider.getPOICloseTo(data.point, data.keyword, data.maxResults, data.maxDistance);
//                    for (Overlay item : poiMarkers.getItems()) {
//                        poiMarkers.remove(item);
//                    }
                    Drawable poiIcon = activity.getResources().getDrawable(R.drawable.map_marker);
                    for (POI poi : pois) {
                        if(!poiLoaded.add(poi.mId)){
                            continue;
                        }
                        Marker poiMarker = new Marker(mapView);
                        poiLocationLoaded.put(poi.mLocation,poiMarker);
                        poiMarker.setTitle(poi.mType);
                        poiMarker.setSnippet(poi.mDescription);
                        poiMarker.setPosition(poi.mLocation);
                        poiMarker.setEnabled(false);

                        //TODO: popularity of this POI, from 1 (lowest) to 100 (highest). 0 if not defined.
                        //filter by this value
//                    poi.mRank

//                    if (poi.mThumbnail != null) {
//                        poiMarker.setIcon(new BitmapDrawable(poi.mThumbnail));
//                    }

                        poiMarker.setIcon(poiIcon);
                        //poiMarkers.add(poiMarker);
                    }
//                    mapView.postInvalidate();
                }
            } catch (Exception ex) {
                ex.toString();
            }
            return null;
        }
    }

    /**
     * My location handlers
     */
    private void setMyLocationUpdate(long milliSeconds){
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
