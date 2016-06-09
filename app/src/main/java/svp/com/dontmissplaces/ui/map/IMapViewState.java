package svp.com.dontmissplaces.ui.map;

import com.google.android.gms.maps.model.LatLng;
import com.svp.infrastructure.mvpvs.viewstate.IViewState;

import svp.com.dontmissplaces.ui.model.PolylineView;

/**
 * Created by Pasha on 6/10/2016.
 */
public interface IMapViewState extends IViewState {
    boolean checkPermissionFineLocation();
    void moveCamera(LatLng latLng);
    void startLocationListening();

    void stopLocationListening();

    void addPolyline(PolylineView polylineView);
}
