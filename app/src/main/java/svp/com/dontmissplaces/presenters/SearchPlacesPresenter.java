package svp.com.dontmissplaces.presenters;

import android.content.Intent;

import java.util.ArrayList;
import java.util.Vector;

import svp.com.dontmissplaces.db.Place;
import svp.com.dontmissplaces.model.nominatim.SearchByText;
import svp.com.dontmissplaces.ui.ActivityCommutator;
import svp.com.dontmissplaces.ui.activities.SearchPlacesActivity;

public class SearchPlacesPresenter extends CommutativePresenter<SearchPlacesActivity,SearchPlacesActivity.ViewState> {
    SearchPlacesActivity.SearchPlacesBundleProvider bundle;

    @Override
    protected void incomingResultFrom(ActivityCommutator.ActivityOperationResult from, Intent data) {

    }

    @Override
    protected void onAttachedView(SearchPlacesActivity view, Intent intent) {
        bundle = new SearchPlacesActivity.SearchPlacesBundleProvider(intent);
    }

    String processing = "";

    public void startSearch(String newText) {
        String query = newText == null ? bundle.getQuery() : newText;
        if(!query.isEmpty() && !processing.equals(query)){
            processing = query;

            new SearchByText(){
                @Override
                protected void processing(ArrayList<Place> poi){
                    Vector<String> pv = new Vector<>();
                    for (Place p : poi){
                        pv.add(p.title);
                    }
                    state.addPlaces(pv);
                }
            }.execute(query);

            state.setSearchQuery(query);
        }
    }
}
