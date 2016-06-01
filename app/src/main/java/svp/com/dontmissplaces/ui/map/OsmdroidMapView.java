package svp.com.dontmissplaces.ui.map;

import android.app.Activity;
import android.location.Location;
import android.os.Bundle;

import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.Collection;

import svp.com.dontmissplaces.R;
import svp.com.dontmissplaces.ui.ActivityPermissions;
import svp.com.dontmissplaces.ui.model.SessionView;

public class OsmdroidMapView implements IMapView {
    private final Activity activity;
    private final ActivityPermissions permissions;
    private final MapView mapView;
    private final IMapController mapController;
    private final MyLocationNewOverlay locationOverlay;

    public OsmdroidMapView(Activity activity, ActivityPermissions permissions) {
        this.activity = activity;
        this.permissions = permissions;
        mapView = (MapView)activity.findViewById(R.id.osmdroid_map);
        mapView.setTileSource(TileSourceFactory.MAPNIK);

        mapController = mapView.getController();

        GpsMyLocationProvider gpsLocationProvider = new GpsMyLocationProvider(activity);
        gpsLocationProvider.setLocationUpdateMinDistance(5);
        gpsLocationProvider.setLocationUpdateMinTime(20);

        locationOverlay = new MyLocationNewOverlay(activity, gpsLocationProvider , mapView);
        locationOverlay.enableMyLocation(gpsLocationProvider);

        mapView.getOverlays().add(locationOverlay);
    }

    @Override
    public void showSessionsTrack(Collection<SessionView> sessions) {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        permissions.checkPermissionExternalStorage();

        mapView.setBuiltInZoomControls(true);
        mapView.setMultiTouchControls(true);
    }

    @Override
    public void onStart() {
        mapController.setZoom(9);
        GeoPoint startPoint = new GeoPoint(48.8583, 2.2944);//locationOverlay.getMyLocation();//
        mapController.setCenter(startPoint);
        locationOverlay.setDrawAccuracyEnabled(true);
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
}
