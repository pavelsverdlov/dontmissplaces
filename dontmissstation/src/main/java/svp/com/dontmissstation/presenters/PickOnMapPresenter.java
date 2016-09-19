package svp.com.dontmissstation.presenters;


import android.content.Intent;
import android.os.AsyncTask;
import android.widget.ListAdapter;

import com.svp.infrastructure.AlertDialogManager;
import com.svp.infrastructure.ConnectionDetector;
import com.svp.infrastructure.mvpvs.commutate.ActivityOperationItem;

import java.util.ArrayList;
import java.util.HashMap;

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
        //    return;
        }

//        GoogleApiMapPlaceProvider.PlacesList places;
//        try {
//            GoogleApiMapPlaceProvider p = new GoogleApiMapPlaceProvider();
//            places = p.search(point.latitude, point.longitude, 1000, "cafe|restaurant");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        this.point = point;
        new LoadPlaces().execute();

    }

    GoogleApiMapPlaceProvider googlePlaces;
    GoogleApiMapPlaceProvider.PlacesList nearPlaces;
    Point2D point;
    AlertDialogManager alert = new AlertDialogManager();
    ArrayList<HashMap<String, String>> placesListItems = new ArrayList<HashMap<String,String>>();


    class LoadPlaces extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        protected String doInBackground(String... args) {
            googlePlaces = new GoogleApiMapPlaceProvider();
            try {
                String types = "cafe|restaurant";// "subway_station";
                //meters
                double radius = 100;
                nearPlaces = googlePlaces.search(point.latitude,
                        point.longitude, radius, types);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String file_url) {
           state.getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    String status = nearPlaces.status;
                    // Check for all possible status
                    if(status.equals("OK")){
                        // Successfully got places details
                        if (nearPlaces.results != null) {
                            // loop through each place
                            for (GoogleApiMapPlaceProvider.Place p : nearPlaces.results) {
                                HashMap<String, String> map = new HashMap<String, String>();

                                // Place reference won't display in listview - it will be hidden
                                // Place reference is used to get "place full details"
                                map.put("reference", p.reference);

                                // Place name
                                map.put("name", p.name);


                                // adding HashMap to ArrayList
                                placesListItems.add(map);
                            }
                            if(nearPlaces.results.size() > 0) {
                                PickOnMapPresenter.this.state.showOnMap(nearPlaces.results.get(0));
                            }


                            // list adapter
//                            ListAdapter adapter = new SimpleAdapter(MainActivity.this, placesListItems,
//                                    R.layout.list_item,
//                                    new String[] { KEY_REFERENCE, KEY_NAME}, new int[] {
//                                    R.id.reference, R.id.name });
//
//                            // Adding data into listview
//                            lv.setAdapter(adapter);
                        }
                    }
                    else if(status.equals("ZERO_RESULTS")){
                        // Zero results found
//                        alert.showAlertDialog(MainActivity.this, "Near Places",
//                                "Sorry no places found. Try to change the types of places",
//                                false);
                    }
                    else if(status.equals("UNKNOWN_ERROR"))
                    {
//                        alert.showAlertDialog(MainActivity.this, "Places Error",
//                                "Sorry unknown error occured.",
//                                false);
                    }
                    else if(status.equals("OVER_QUERY_LIMIT"))
                    {
//                        alert.showAlertDialog(MainActivity.this, "Places Error",
//                                "Sorry query limit to google places is reached",
//                                false);
                    }
                    else if(status.equals("REQUEST_DENIED"))
                    {
//                        alert.showAlertDialog(MainActivity.this, "Places Error",
//                                "Sorry error occured. Request is denied",
//                                false);
                    }
                    else if(status.equals("INVALID_REQUEST"))
                    {
//                        alert.showAlertDialog(MainActivity.this, "Places Error",
//                                "Sorry error occured. Invalid Request",
//                                false);
                    }
                    else
                    {
//                        alert.showAlertDialog(MainActivity.this, "Places Error",
//                                "Sorry error occured.",
//                                false);
                    }
                }
            });

        }

    }
}
