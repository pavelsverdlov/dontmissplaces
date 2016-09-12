package svp.com.dontmissstation.presenters;


import android.content.Intent;

import com.svp.infrastructure.ConnectionDetector;
import com.svp.infrastructure.mvpvs.commutate.ActivityOperationItem;

import svp.app.map.android.GoogleApiMapPlaceProvider;
import svp.app.map.android.PlaceProvider;
import svp.app.map.model.Place;
import svp.app.map.model.Point2D;
import svp.com.dontmissstation.db.Repository;
import svp.com.dontmissstation.ui.activities.PickOnMapActivity;

public class PickOnMapPresenter  extends CommutativePreferencePresenter<PickOnMapActivity,PickOnMapActivity.ViewState> {

    public PickOnMapPresenter(Repository repository) {

    }

    @Override
    protected void incomingResultFrom(ActivityOperationItem from, Intent data) {

    }

    public void searchNearestStation(Point2D point) {
//        PlaceProvider pp = new PlaceProvider(state.getActivity());
//        Place res = pp.getPlace(point.getLatLng());
        ConnectionDetector cd = new ConnectionDetector(state.getActivity());

        if(cd.isConnectingToInternet()){
            return;
        }

        GoogleApiMapPlaceProvider.PlacesList places;
        try {
            GoogleApiMapPlaceProvider p = new GoogleApiMapPlaceProvider();
            places = p.search(point.latitude, point.longitude, 1000, "cafe|restaurant");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
