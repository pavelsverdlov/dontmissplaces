package svp.com.dontmissplaces.presenters;

import android.content.Intent;
import android.database.Cursor;

import com.svp.infrastructure.mvpvs.commutate.ActivityOperationItem;

import svp.com.dontmissplaces.db.Repository;
import svp.com.dontmissplaces.ui.activities.SavedPlacesActivity;

public class SavedPlacesPresenter extends CommutativePresenter<SavedPlacesActivity,SavedPlacesActivity.ViewState> {
    private final Repository repository;

    public SavedPlacesPresenter(Repository repository) {
        this.repository = repository;
    }

    @Override
    protected void incomingResultFrom(ActivityOperationItem from, Intent data) {

    }

    public Cursor getSavedPlace() {
        return repository.place.getCursorPlaces();
    }
}
