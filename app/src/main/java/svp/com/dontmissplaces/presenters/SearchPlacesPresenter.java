package svp.com.dontmissplaces.presenters;

import android.content.Intent;

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

    public void startSearch() {
        String query = bundle.getQuery();
        if(!query.isEmpty()){
            state.setSearchQuery(query);
        }
    }
}
