package svp.com.dontmissplaces.presenters;

import android.content.Intent;
import android.database.MatrixCursor;
import android.util.Log;

import java.util.ArrayList;
import java.util.Vector;

import svp.com.dontmissplaces.db.Place;
import svp.com.dontmissplaces.db.Repository;
import svp.com.dontmissplaces.model.PlaceProvider;
import svp.com.dontmissplaces.model.nominatim.SearchByText;
import svp.com.dontmissplaces.ui.ActivityCommutator;
import svp.com.dontmissplaces.ui.activities.SearchPlacesActivity;

public class SearchPlacesPresenter extends CommutativePresenter<SearchPlacesActivity,SearchPlacesActivity.ViewState> {
    private static final String TAG = "SearchPlacesPresenter";
    private final Repository repository;
    SearchPlacesActivity.SearchPlacesBundleProvider bundle;

    public SearchPlacesPresenter(Repository repository) {
        this.repository = repository;
        this.lock = new Object();
    }

    @Override
    protected void incomingResultFrom(ActivityCommutator.ActivityOperationResult from, Intent data) {

    }

    @Override
    protected void onAttachedView(SearchPlacesActivity view, Intent intent) {
        bundle = new SearchPlacesActivity.SearchPlacesBundleProvider(intent);
    }

    String processing = "";
    private final Object lock;

    public void startSearch(String newText) {
        String query = newText == null ? bundle.getQuery() : newText;
        if(!query.isEmpty() && !processing.equals(query) && query.length() > 5){
            processing = query;

            PlaceProvider pp = new PlaceProvider(state.getActivity());
            Vector<SearchPlacesActivity.PlaceSearchResult> places = new Vector<>() ;
            for (Place p : pp.getPlace(newText)){
                places.add(new SearchPlacesActivity.PlaceSearchResult(p));
            }
            state.addPlaces(places);
            /*
            new SearchByText(){
                @Override
                protected void processing(ArrayList<Place> poi){
                    //MatrixCursor cursor = new MatrixCursor(new String[] { SearchPlacesActivity.PlaceSearchResult.PLACE_KEY });
                    Vector<SearchPlacesActivity.PlaceSearchResult> pv = new Vector<>();
                    for (Place p : poi){
//                        cursor.addRow(new Place[] { p });
                        pv.add(new SearchPlacesActivity.PlaceSearchResult(p));
                    }
                    state.addPlaces(pv);
                }
            }.execute(query);
*/
            state.setSearchQuery(query);
        }
    }

    public void placeSelected(SearchPlacesActivity.PlaceSearchResult item) {
        Place place = repository.poi.insert(item.getPlace());
        if(place.isStored()){
            commutator.backTo(new SearchPlacesActivity.SearchPlacesBundleProvider().putFoundPlaceId(place.id));
        }else{
            Log.w(TAG,"Place has already stored in db.");
        }

    }
}
