package svp.com.dontmissplaces.ui.map;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import org.osmdroid.DefaultResourceProxyImpl;
import org.osmdroid.ResourceProxy;
import org.osmdroid.api.IGeoPoint;
import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import svp.com.dontmissplaces.R;
import svp.com.dontmissplaces.ui.ActivityPermissions;
import svp.com.dontmissplaces.ui.model.SessionView;

public class OsmdroidMapView implements IMapView {
    private final Activity activity;
    private final ActivityPermissions permissions;
    private final MapView mapView;
    private final IMapController mapController;
    private MyLocationNewOverlay locationOverlay;
    private final ArrayList<OverlayItem> overlayItemArray;

    public OsmdroidMapView(Activity activity, ActivityPermissions permissions) {
        this.activity = activity;
        this.permissions = permissions;
        overlayItemArray = new ArrayList<OverlayItem>();
        mapView = (MapView)activity.findViewById(R.id.osmdroid_map);
        mapView.setTileSource(TileSourceFactory.MAPNIK);

        mapController = mapView.getController();

        DefaultResourceProxyImpl defaultResourceProxyImpl = new DefaultResourceProxyImpl(activity);
        MyItemizedIconOverlay myItemizedIconOverlay = new MyItemizedIconOverlay(overlayItemArray, null, defaultResourceProxyImpl);

        mapView.getOverlays().add(myItemizedIconOverlay);


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
}
