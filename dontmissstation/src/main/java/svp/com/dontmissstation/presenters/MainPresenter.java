package svp.com.dontmissstation.presenters;

import android.content.Intent;
import android.location.Location;
import android.os.RemoteException;
import android.util.Log;

import com.svp.infrastructure.mvpvs.commutate.ActivityOperationItem;

import org.osmdroid.util.BoundingBoxE6;
import org.osmdroid.util.GeoPoint;

import java.util.Vector;

import svp.app.map.MapViewTypes;
import svp.app.map.android.gps.GPSService;
import svp.app.map.android.gps.GPSServiceProvider;
import svp.app.map.android.gps.IGPSProvider;
import svp.app.map.model.Point2D;
import svp.com.dontmissstation.MainActivity;
import svp.com.dontmissstation.db.Repository;
import svp.com.dontmissstation.ui.activities.ActivityOperationResult;
import svp.com.dontmissstation.ui.model.SubwayStationView;
import svp.com.dontmissstation.ui.model.SubwayView;

public class MainPresenter extends CommutativePreferencePresenter<MainActivity,MainActivity.ViewState> {

    private static final String TAG = "";
    private final Repository repository;
    public IGPSProvider gps;
    SubwayView showedSubway;

    public MainPresenter(Repository repository) {
        this.repository = repository;
    }

    public boolean isSubwayVisualizeMode(){
        return showedSubway != null;
    }
    public void clearSelectedSubway() {
        showedSubway = null;
        state.clearCache();
    }

    @Override
    protected void incomingResultFrom(ActivityOperationItem from, Intent data) {
        if(ActivityOperationResult.ListSubways.is(from)){
            SubwayBundleProvider bundle = new SubwayBundleProvider(data);
            long id = bundle.getSubwayId();
            showedSubway = repository.getSubwayById(id);
            state.showSubway(showedSubway);
        }
    }

    @Override
    protected void onAttachedView(MainActivity view, Intent intent) {
        super.onAttachedView(view,intent);
        try {
            GPSServiceProvider gpsp = new GPSServiceProvider(view, GPSService.class);
            if(!state.getPermissions().checkPermissionFineLocation()) {
                throw new Exception("No permissions");
            }
            gpsp.startup(new Runnable() {
                @Override
                public void run() {
                    Log.d(TAG,"GPS service startup");
                }
            });
            gps = gpsp;
            if(state.hasSubwayCached()) {
                showedSubway = state.getSubwayCached();
                //state.refresh(view);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void onMoveToMyLocation() {
        Point2D point = Point2D.empty();
        try {
            point = new Point2D(gps.getLocation());
        } catch (Exception e) {
            Log.e(TAG, "onMoveToMyLocation", e);
        }
        state.MapCameraMoveTo(point);
    }

    /**
     * Settings
     * */
    public MapViewTypes getMapViewType() {
        return userSettings.getMapProvider();
    }

    public void openAddNewSubway() {
        commutator.goTo(ActivityOperationResult.EditNewSubway);
    }
    public void openListSubwaysActivity() {
        commutator.goTo(ActivityOperationResult.ListSubways);
    }
    public void openRouteActivity() {
        SubwayBundleProvider bundle = new SubwayBundleProvider();
        commutator.goTo(ActivityOperationResult.SearchNewRoute, bundle.putSubwayId(showedSubway.getId()));
    }

    public void pickOnPlace(Point2D point) {
//        PlaceProvider pp = new PlaceProvider(state.getActivity());
//        Place res = pp.getPlace(selectedPoint.getLatLng());
    }


    public void findNearStation() {
        try {
            Location loc = gps.getLocation();
            Point2D current = new Point2D(loc);
            int maxD = 100;
            GeoPoint p = current.getGeoPoint();
            BoundingBoxE6 bb = new BoundingBoxE6(p.getLatitudeE6() + maxD,
                    p.getLongitudeE6() + maxD,
                    p.getLatitudeE6() - maxD,
                    p.getLongitudeE6() - maxD);

            Vector<SubwayStationView> stations = repository.getNearStations(bb);
            state.showStations(stations);
        } catch (RemoteException e) {
            Log.e(TAG,"",e);
        }
    }
}
