package svp.app.map;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;

import com.svp.infrastructure.common.ViewExtensions;

import org.osmdroid.api.IMapController;
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
import org.osmdroid.views.overlay.FolderOverlay;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.Polyline;
import org.osmdroid.views.overlay.compass.CompassOverlay;
import org.osmdroid.views.overlay.compass.InternalCompassOrientationProvider;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.IMyLocationConsumer;
import org.osmdroid.views.overlay.mylocation.IMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import svp.app.map.android.gps.IGPSProvider;
import svp.app.map.android.gps.IGPSService;
import svp.app.map.android.gps.OnLocationChangeListener;
import svp.app.map.model.IPOIView;
import svp.app.map.model.Point2D;
public class OsmdroidMapView implements IMapView, MapEventsReceiver, MapListener, android.view.View.OnClickListener {
    private final Activity activity;
    private final IGPSProvider gps;
    public final MapView mapView;
    public final IMapController mapController;
    public final GpsLocationProvider gpsProvider;
    private final FolderOverlay poiMarkers;
    private final CompassOverlay compassOverlay;


    private OnMapClickListener clickListener;

    FloatingActionButton fabZoomPlus;
    FloatingActionButton fabZoomMinus;

    public OsmdroidMapView(Activity activity, int mapResourceId, IGPSProvider gps) {
        this.activity = activity;
        this.gps = gps;

        fabZoomPlus = ViewExtensions.findViewById(activity,R.id.map_zoom_plus_fab);
        fabZoomMinus = ViewExtensions.findViewById(activity,R.id.map_zoom_minus_fab);

        mapView = (MapView) activity.findViewById(mapResourceId);
        mapView.setMapListener(new DelayedMapListener(this, 100));
        setMapViewSettings();

        compassOverlay = new CompassOverlay(activity, new InternalCompassOrientationProvider(activity),
                mapView);
        compassOverlay.enableCompass();

        mapController = mapView.getController();
        poiMarkers = new FolderOverlay(activity);

        MapEventsOverlay mapEventsOverlay = new MapEventsOverlay(activity, this);

        gpsProvider = new GpsLocationProvider();

        mapController.setZoom(12);

        mapView.getOverlays().add(mapEventsOverlay);
        mapView.getOverlays().add(compassOverlay);
        mapView.getOverlays().add(poiMarkers);
        mapView.invalidate();

        fabZoomPlus.setOnClickListener(this);
        fabZoomMinus.setOnClickListener(this);
    }

    @Override
    public void onClick(android.view.View v) {
        int id = v.getId();
        if(id ==  R.id.map_zoom_plus_fab){
            mapController.setZoom(mapView.getZoomLevel() + 1);
        }else if(id == R.id.map_zoom_minus_fab){
            mapController.setZoom(mapView.getZoomLevel() - 1);
        }
    }

    /**
     * IMapView
     * */
    @Override
    public void onCreate(Bundle savedInstanceState) {    }
    @Override
    public void onStart() { }
    @Override
    public void onStop() { }
    @Override
    public void onResume() { }
    @Override
    public void moveTo(Point2D p) {
        mapController.setZoom(16);
        mapController.animateTo(p.getGeoPoint());
    }
    @Override
    public void setOnMapClickListener(OnMapClickListener listener) {
        clickListener = listener;
    }
    public void drawMarker(IPOIView poi, int markerIdResource) {
        if (poi != null) {
            for (Overlay m : poiMarkers.getItems()) {
                poiMarkers.remove(m);
            }
            Marker poiMarker = new Marker(mapView);
            poiMarker.isFlat();
//            poiMarker.setTitle(poi.getType());
            poiMarker.setSnippet(poi.getName());
            poiMarker.setIcon(activity.getResources().getDrawable(markerIdResource));
            //poiMarker.setAnchor(Marker.NOT_SET, Marker.NOT_SET);
            poiMarker.setPosition(poi.getGeoPoint());
            poiMarkers.add(poiMarker);

            mapView.postInvalidate();
        }
    }


    /**
     * Click handlers
     */

    @Override
    public boolean singleTapConfirmedHelper(GeoPoint p) {
        clickListener.onMapClick(new Point2D(p));
        return false;
    }

    @Override
    public boolean longPressHelper(GeoPoint p) {
        clickListener.onMapLongClick(new Point2D(p));
        return false;
    }

    /**
     * Map listeners
     */

    @Override
    public boolean onScroll(ScrollEvent event) {
        int zoom = mapView.getZoomLevel();
        BoundingBoxE6 box = mapView.getBoundingBox();
        clickListener.onScroll(zoom, box);
        return true;
    }

    @Override
    public boolean onZoom(ZoomEvent event) {
        int zoom = event.getZoomLevel();
        BoundingBoxE6 box = mapView.getBoundingBox();

        clickListener.onZoom(zoom, box);
        return true;
    }

    /**
     * My location handlers
     */

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
        mapView.setBuiltInZoomControls(false);
        mapView.setMultiTouchControls(true);

        mapView.setMinZoomLevel(3);
        mapView.setMaxZoomLevel(18); // Latest OSM can go to 21!
        mapView.getTileProvider().createTileCache();
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


    public class GpsLocationProvider {
        private MyLocationNewOverlay myLocationOverlay;
        private IMyLocationProvider gpsMyLocationProvider;
        private boolean shouldRecreate;

        public void turnGPSOn()
        {
            Intent intent = new Intent("android.location.GPS_ENABLED_CHANGE");
            intent.putExtra("enabled", true);
            activity.sendBroadcast(intent);

            String provider = Settings.Secure.getString(activity.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            if(!provider.contains("gps")){ //if gps is disabled
                final Intent poke = new Intent();
                poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
                poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
                poke.setData(Uri.parse("3"));
                activity.sendBroadcast(poke);
            }
        }
        private void turnGPSOn1(){
            String provider = Settings.Secure.getString(activity.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

            if(!provider.contains("gps")){ //if gps is disabled
                final Intent poke = new Intent();
                poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
                poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
                poke.setData(Uri.parse("3"));
                activity.sendBroadcast(poke);
            }
        }

        public GpsLocationProvider() {
            create();
        }
        public Point2D getMyLocation() {
            if(shouldRecreate){
                create();
            }
            GeoPoint point = myLocationOverlay.getMyLocation();
//            if(point == null){
//                turnGPSOn1();
//                return Point2D.empty();
//                LocationManager locationManager = (LocationManager) activity.getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
//                Location loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//                if(loc != null) {
//                    point = new GeoPoint(loc.getLatitude(), loc.getLongitude());
//                }
//            }
            if (point == null) {//gps disable
                shouldRecreate = true;
                return Point2D.empty();
            }
            return new Point2D(point);
        }

        private void create() {
            if(myLocationOverlay != null){
                mapView.getOverlays().remove(myLocationOverlay);
            }
            gpsMyLocationProvider = new MyLocationProvider(gps,100);//GpsMyLocationProvider(activity);
            myLocationOverlay = new MyLocationNewOverlay(gpsMyLocationProvider, mapView);
            mapView.getOverlays().add(myLocationOverlay);
            shouldRecreate = false;
            setMyLocationUpdate(1000);
        }
        private void setMyLocationUpdate(long milliSeconds) {
            myLocationOverlay.enableMyLocation();
            myLocationOverlay.setDrawAccuracyEnabled(true);

            gpsMyLocationProvider.startLocationProvider(myLocationOverlay);
//            gpsMyLocationProvider.setLocationUpdateMinDistance(100);
//            gpsMyLocationProvider.setLocationUpdateMinTime(milliSeconds);
        }
    }
    public static class MyLocationProvider implements IMyLocationProvider{
        IGPSProvider gps;
        private final long updateMinTime;
        private IMyLocationConsumer locationConsumer;
        private TrackTimer timer;

        public MyLocationProvider(IGPSProvider gps, long updateMinTime){
            this.gps = gps;

            this.updateMinTime = updateMinTime;
        }
        @Override
        public boolean startLocationProvider(IMyLocationConsumer myLocationConsumer) {
            locationConsumer = myLocationConsumer;
            timer = new TrackTimer(updateMinTime);
            return true;
        }

        @Override
        public void stopLocationProvider() {
            timer.stop();
        }

        @Override
        public Location getLastKnownLocation() {
            try {
                return gps.getLocation();
            } catch (Exception e) {
                Log.e("MyLocationProvider","getLastKnownLocation",e);
                return null;
            }
        }
        private class TrackTimer extends TimerTask {
            private TrackTimer(long intervalMillisec) {
                Timer timer = new Timer();
                timer.scheduleAtFixedRate(this, intervalMillisec, intervalMillisec);
            }

            public void stop() {
                this.cancel();
            }

            @Override
            public void run() {
                locationConsumer.onLocationChanged(getLastKnownLocation(), MyLocationProvider.this);
            }
        }
    }
}

