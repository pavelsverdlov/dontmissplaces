package svp.com.dontmissplaces.presenters;

import android.content.Intent;
import android.database.MatrixCursor;

import java.util.ArrayList;
import java.util.Vector;

import svp.com.dontmissplaces.db.Place;
import svp.com.dontmissplaces.model.nominatim.SearchByText;
import svp.com.dontmissplaces.ui.ActivityCommutator;
import svp.com.dontmissplaces.ui.activities.SearchPlacesActivity;

public class SearchPlacesPresenter extends CommutativePresenter<SearchPlacesActivity,SearchPlacesActivity.ViewState> {
    SearchPlacesActivity.SearchPlacesBundleProvider bundle;

    public SearchPlacesPresenter() {
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
        if(!query.isEmpty() && !processing.equals(query)){
            processing = query;

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

            state.setSearchQuery(query);
        }
    }
}
