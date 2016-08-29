package svp.com.dontmissstation.presenters;

import android.content.Intent;

import com.svp.infrastructure.mvpvs.commutate.ActivityOperationItem;

import svp.com.dontmissstation.db.Repository;
import svp.com.dontmissstation.ui.activities.*;
import svp.com.dontmissstation.ui.model.SubwayView;

public class EditSubwayPresenter extends CommutativePreferencePresenter<EditSubwayScrollingActivity,EditSubwayScrollingActivity.ViewState> {


    public EditSubwayPresenter(Repository repository) {

    }

    @Override
    protected void incomingResultFrom(ActivityOperationItem from, Intent data) {

    }

    public void openEditLineActivity() {
        commutator.goTo(ActivityOperationResult.AddSubwayLine);
    }

    public SubwayView getSubway() {
        return new SubwayView();
    }

//    public void placeSelected(SavedPlacesActivity.SavePlaceView item) {
//        commutator.backTo(new SearchPlacesActivity.SearchPlacesBundleProvider().putFoundPlaceId(item.getPlace().id));
//    }

}
