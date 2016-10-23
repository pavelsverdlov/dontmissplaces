package svp.com.dontmissstation.presenters;


import android.content.Intent;
import android.os.AsyncTask;

import com.svp.infrastructure.AlertDialogManager;
import com.svp.infrastructure.ConnectionDetector;
import com.svp.infrastructure.mvpvs.commutate.ActivityOperationItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import svp.app.map.android.GeocoderPlaceProvider;
import svp.app.map.android.GoogleApiMapPlaceProvider;
import svp.app.map.model.Place;
import svp.app.map.model.Point2D;
import svp.com.dontmissstation.db.Repository;
import svp.com.dontmissstation.model.BundleRepository;
import svp.com.dontmissstation.ui.activities.PickOnMapActivity;
import svp.com.dontmissstation.ui.model.POIView;
import svp.com.dontmissstation.ui.model.SubwayStationView;

public class PickOnMapPresenter extends CommutativePreferencePresenter<PickOnMapActivity, PickOnMapActivity.ViewState> {

    private final Repository repository;
    private SubwayStationView station;
    private POIView poi;
    Point2D selectedPoint;

    public PickOnMapPresenter(Repository repository) {
        this.repository = repository;
    }

    @Override
    protected void incomingResultFrom(ActivityOperationItem from, Intent data) {
        station = BundleRepository.getStation(data,repository);
    }

    @Override
    protected void onAttachedView(PickOnMapActivity view, Intent intent) {
        super.onAttachedView(view, intent);
        station = BundleRepository.getStation(intent,repository);
    }

    public void openEditStationActivity() {
        commutator.backTo(new SubwayBundleProvider().putStationId(station.getId()));
    }

    public void searchNearestStation(Point2D point) {
//        GeocoderPlaceProvider pp = new GeocoderPlaceProvider(state.getActivity());
//        Place res = pp.getPlace(selectedPoint.getLatLng());
        ConnectionDetector cd = new ConnectionDetector(state.getActivity());

        if (cd.isConnectingToInternet()) {
            //    return;
        }

        this.selectedPoint = point;
        new LoadPlaces().execute();
    }

    public void clearSelectedPlace(){
        selectedPoint = Point2D.empty();
        state.closeYesNotBottomPanel();
    }

    private void setSelectedPlace(List<GoogleApiMapPlaceProvider.Place> results){
        if(results.size() > 0) {
            GoogleApiMapPlaceProvider.Place place = results.get(0);
            GoogleApiMapPlaceProvider.Place.Location loc = place.geometry.location;
            selectedPoint = new Point2D(loc.lat, loc.lng);
            poi = new POIView(place);

        }else{
            GeocoderPlaceProvider pp = new GeocoderPlaceProvider(state.getActivity());
            Place place = pp.getPlace(selectedPoint.getLatLng());
            poi = new POIView(place.title, place.address,selectedPoint);;
        }
        state.showOnMap(poi);
        state.openYesNotBottomPanel();
    }

    public void storeSelectedPlace() {
        station.setName(poi.getAddress());
        repository.updateStationCoordinate(station,selectedPoint);
        state.closeYesNotBottomPanel();
    }

    class LoadPlaces extends AsyncTask<String, String, String> {
        GoogleApiMapPlaceProvider.PlacesList nearPlaces;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected String doInBackground(String... args) {
            GoogleApiMapPlaceProvider googlePlaces = new GoogleApiMapPlaceProvider();
            try {
                String types = "subway_station";
                //meters
                double radius = 100;
                nearPlaces = googlePlaces.search(selectedPoint.latitude,
                        selectedPoint.longitude, radius, types);
            } catch (Exception e) {
                e.printStackTrace();
                nearPlaces = new GoogleApiMapPlaceProvider.PlacesList();
                nearPlaces.results= new Vector<>();
            }
            return null;
        }

        protected void onPostExecute(String file_url) {
            state.getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    String status = nearPlaces.status;

                    setSelectedPlace(nearPlaces.results);

                    /*
                    if (status.equals("OK")) {
                        if (nearPlaces.results != null) {

                        }
                    } else if (status.equals("ZERO_RESULTS")) {
                        // Zero results found
//                        alert.showAlertDialog(MainActivity.this, "Near Places",
//                                "Sorry no places found. Try to change the types of places",
//                                false);
                    } else if (status.equals("UNKNOWN_ERROR")) {
//                        alert.showAlertDialog(MainActivity.this, "Places Error",
//                                "Sorry unknown error occured.",
//                                false);
                    } else if (status.equals("OVER_QUERY_LIMIT")) {
//                        alert.showAlertDialog(MainActivity.this, "Places Error",
//                                "Sorry query limit to google places is reached",
//                                false);
                    } else if (status.equals("REQUEST_DENIED")) {
//                        alert.showAlertDialog(MainActivity.this, "Places Error",
//                                "Sorry error occured. Request is denied",
//                                false);
                    } else if (status.equals("INVALID_REQUEST")) {
//                        alert.showAlertDialog(MainActivity.this, "Places Error",
//                                "Sorry error occured. Invalid Request",
//                                false);
                    } else {
//                        alert.showAlertDialog(MainActivity.this, "Places Error",
//                                "Sorry error occured.",
//                                false);
                    }
                    */
                }
            });

        }

    }
}
