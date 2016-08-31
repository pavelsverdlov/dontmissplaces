package svp.com.dontmissplaces.presenters;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import com.svp.infrastructure.mvpvs.bundle.BundleProvider;
import com.svp.infrastructure.mvpvs.commutate.ActivityOperationItem;

import svp.com.dontmissplaces.db.Repository;
import svp.com.dontmissplaces.ui.activities.SavedPlacesActivity;
import svp.com.dontmissplaces.ui.activities.SearchPlacesActivity;

public class SavedPlacesPresenter extends CommutativePresenter<SavedPlacesActivity,SavedPlacesActivity.ViewState> {
    public static class SavedPlacesBundleProvider extends BundleProvider {
        private static final String KEY = "saved_palace_id";

        public SavedPlacesBundleProvider() {
            this(new Bundle());
        }
        public SavedPlacesBundleProvider(Bundle b) {
            super(b);
        }
        public SavedPlacesBundleProvider(Intent intent) {
            super(intent);
        }
        public SavedPlacesBundleProvider putSavePlace(long placeId) {
            bundle.putLong(KEY, placeId);
            return this;
        }

        public long getSavedPlaceId() {
            return bundle.getLong(KEY);
        }
    }

    private final Repository repository;

    public SavedPlacesPresenter(Repository repository) {
        this.repository = repository;
    }

    @Override
    protected void incomingResultFrom(ActivityOperationItem from, Intent data) {
        //activity_list_subways_recyclerView
    }

    public Cursor getSavedPlace() {
        return repository.place.getCursorPlaces();
    }

    public void placeSelected(SavedPlacesActivity.SavePlaceView item) {
        //TODO: move to main activity animate map to position and show place info
        commutator.backTo(new SearchPlacesActivity.SearchPlacesBundleProvider().putFoundPlaceId(item.getPlace().id));
    }
}
