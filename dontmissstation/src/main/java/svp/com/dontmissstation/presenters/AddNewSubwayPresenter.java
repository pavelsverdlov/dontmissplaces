package svp.com.dontmissstation.presenters;

import android.content.Intent;

import com.svp.infrastructure.mvpvs.commutate.ActivityOperationItem;

import svp.com.dontmissstation.db.Repository;
import svp.com.dontmissstation.ui.activities.AddNewSubwayActivity;

public class AddNewSubwayPresenter extends CommutativePreferencePresenter<AddNewSubwayActivity,AddNewSubwayActivity.ViewState> {


    public AddNewSubwayPresenter(Repository repository) {

    }

    @Override
    protected void incomingResultFrom(ActivityOperationItem from, Intent data) {

    }

//    public void placeSelected(SavedPlacesActivity.SavePlaceView item) {
//        commutator.backTo(new SearchPlacesActivity.SearchPlacesBundleProvider().putFoundPlaceId(item.getPlace().id));
//    }

}
