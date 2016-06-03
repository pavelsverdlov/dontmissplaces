package svp.com.dontmissplaces.ui.map;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;

import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.location.NominatimPOIProvider;
import org.osmdroid.bonuspack.location.POI;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.util.TileSystem;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.FolderOverlay;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.Polyline;

import java.util.ArrayList;
import java.util.Collection;

import svp.com.dontmissplaces.R;
import svp.com.dontmissplaces.ui.ActivityPermissions;
import svp.com.dontmissplaces.ui.model.SessionView;

public class OsmdroidMapView implements IMapView, MapEventsReceiver {
    private final Activity activity;
    private final ActivityPermissions permissions;
    private final MapView mapView;
    private final IMapController mapController;
    FolderOverlay poiMarkers;
    //   private MyLocationNewOverlay locationOverlay;
    //   private final ArrayList<OverlayItem> overlayItemArray;

    public OsmdroidMapView(Activity activity, ActivityPermissions permissions) {
        this.activity = activity;
        this.permissions = permissions;

        TileSystem.setTileSize(2000);

        // overlayItemArray = new ArrayList<OverlayItem>();
        mapView = (MapView) activity.findViewById(R.id.osmdroid_map);
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        //set auto resize text titles on map
        mapView.setTilesScaledToDpi(true);

        mapController = mapView.getController();

        // DefaultResourceProxyImpl defaultResourceProxyImpl = new DefaultResourceProxyImpl(activity);
        //MyItemizedIconOverlay myItemizedIconOverlay = new MyItemizedIconOverlay(overlayItemArray, null, defaultResourceProxyImpl);

        //mapView.getOverlays().add(myItemizedIconOverlay);

        MapEventsOverlay mapEventsOverlay = new MapEventsOverlay(activity, this);
        mapView.getOverlays().add(0, mapEventsOverlay);

        poiMarkers = new FolderOverlay(activity);
        mapView.getOverlays().add(poiMarkers);
    }

    @Override
    public void showSessionsTrack(Collection<SessionView> sessions) {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        permissions.checkPermissionExternalStorage();
        permissions.checkPermissionFineLocation();

        mapView.setBuiltInZoomControls(true);
        mapView.setMultiTouchControls(true);
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

        mapController.setZoom(12);
        GeoPoint startPoint = new GeoPoint(48.8583, 2.2944);//locationOverlay.getMyLocation();//

        mapController.setCenter(startPoint);
        //  locationOverlay.setDrawAccuracyEnabled(true);
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
        drawMarker(p);
        getPOIs(p);
        return false;
    }

    @Override
    public boolean longPressHelper(GeoPoint p) {
        return false;
    }
    Marker startMarker;
    private void drawMarker(GeoPoint p){
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

    private void getPOIs(GeoPoint startPoint) {
        permissions.checkPermissionNetwork();
        InputData input = new InputData(startPoint, "cinema", 50, 0.1);
        new PointsOfInterestTask().execute(input);
    }

    public final class InputData{
        public final GeoPoint point;
        public final String keyword;
        public final int maxResults;
        public final double maxDistance;
        /**
         * @param maxDistance to the position, in degrees.
         * Note that it is used to build a bounding box around the position, not a circle.
         * */
        public InputData(GeoPoint point, String keyword, int maxResults, double maxDistance) {
            this.point = point;
            this.keyword = keyword;
            this.maxResults = maxResults;
            this.maxDistance = maxDistance;
        }
    }

    private class PointsOfInterestTask extends AsyncTask<InputData, Void, Void> {
        @Override
        protected Void doInBackground(InputData... params) {
            try {
                InputData data = params[0];
                NominatimPOIProvider poiProvider = new NominatimPOIProvider(NominatimPOIProvider.NOMINATIM_POI_SERVICE);
                ArrayList<POI> pois = poiProvider.getPOICloseTo(data.point,data.keyword, data.maxResults, data.maxDistance);
                for (Overlay item : poiMarkers.getItems()) {
                    poiMarkers.remove(item);
                }
                Drawable poiIcon = activity.getResources().getDrawable(R.drawable.map_marker);
                for (POI poi : pois) {
                    Marker poiMarker = new Marker(mapView);
                    poiMarker.setTitle(poi.mType);
                    poiMarker.setSnippet(poi.mDescription);
                    poiMarker.setPosition(poi.mLocation);

                    //TODO: popularity of this POI, from 1 (lowest) to 100 (highest). 0 if not defined.
                    //filter by this value
                    //poi.mRank

                    if (poi.mThumbnail != null) {
                        poiMarker.setIcon(new BitmapDrawable(poi.mThumbnail));
                    }else{
                        poiMarker.setIcon(poiIcon);
                    }
                    poiMarkers.add(poiMarker);
                }
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mapView.invalidate();
                    }
                });
            } catch (Exception ex) {
                ex.toString();
            }
            return null;
        }
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
