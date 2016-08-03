package svp.app.map;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;

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
import org.osmdroid.views.overlay.compass.CompassOverlay;
import org.osmdroid.views.overlay.compass.InternalCompassOrientationProvider;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;
import svp.app.map.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import svp.app.map.model.Point2D;
public abstract class OsmdroidMapView implements IMapView, MapEventsReceiver, MapListener, android.view.View.OnClickListener {
    private final Activity activity;
    private final MapView mapView;
    private final IMapController mapController;
    private final GpsLocationProvider gpsProvider;
    private final FolderOverlay poiMarkers;
    private final CompassOverlay compassOverlay;

    private static final int d = svp.app.map.R.id.map_zoom_plus_fab;

    private OnMapClickListener clickListener;

    FloatingActionButton fabZoomPlus;
    FloatingActionButton fabZoomMinus;
//    @Bind(svp.app.map.R.id.map_zoom_minus_fab)
//    FloatingActionButton fabZoomMinus;

    protected OsmdroidMapView(Activity activity, int mapResourceId) {
        this.activity = activity;

        int d = svp.app.map.R.id.map_zoom_plus_fab;

        ButterKnife.bind(this, activity);

        mapView = (MapView) activity.findViewById(mapResourceId);
        mapView.setMapListener(new DelayedMapListener(this, 100));
       // setMapViewSettings();

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
//        switch (v.getId()) {
//            case R.id.map_zoom_plus_fab:
//                mapController.setZoom(mapView.getZoomLevel() + 1);
//                break;
//            case R.id.map_zoom_minus_fab:
//                mapController.setZoom(mapView.getZoomLevel() - 1);
//                break;
//        }
    }

    public class GpsLocationProvider {
        private MyLocationNewOverlay myLocationOverlay;
        private GpsMyLocationProvider gpsMyLocationProvider;
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
            gpsMyLocationProvider = new GpsMyLocationProvider(activity);
            myLocationOverlay = new MyLocationNewOverlay(gpsMyLocationProvider, mapView);
            mapView.getOverlays().add(myLocationOverlay);
            shouldRecreate = false;
            setMyLocationUpdate(1000);
        }
        private void setMyLocationUpdate(long milliSeconds) {
            myLocationOverlay.enableMyLocation();
            myLocationOverlay.setDrawAccuracyEnabled(true);

            gpsMyLocationProvider.startLocationProvider(myLocationOverlay);
            gpsMyLocationProvider.setLocationUpdateMinDistance(100);
            gpsMyLocationProvider.setLocationUpdateMinTime(milliSeconds);
        }
    }
}

