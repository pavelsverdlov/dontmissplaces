package svp.com.dontmissstation.presenters;

import android.content.Intent;

import com.svp.infrastructure.mvpvs.commutate.ActivityOperationItem;

import svp.com.dontmissstation.db.Repository;
import svp.com.dontmissstation.model.BundleRepository;
import svp.com.dontmissstation.ui.activities.*;
import svp.com.dontmissstation.ui.model.SubwayStationView;
import svp.com.dontmissstation.ui.model.SubwayLineView;
import svp.com.dontmissstation.ui.model.SubwayView;

public class EditSubwayPresenter extends CommutativePreferencePresenter<EditSubwayScrollingActivity,EditSubwayScrollingActivity.ViewState> {
    private final Repository repository;
    private SubwayView subway;

    public EditSubwayPresenter(Repository repository) {
        this.repository = repository;
    }

    @Override
    protected void incomingResultFrom(ActivityOperationItem from, Intent data) {
        subway = BundleRepository.getSubway(data,repository);
    }
    @Override
    protected void onAttachedView(EditSubwayScrollingActivity view,  Intent intent){
        super.onAttachedView(view,intent);
        subway = BundleRepository.getSubway(intent,repository);
        if(subway == null){
            //create new subway
//            subway = repository.addNewSubway();
            subway = repository.getSubwayById(1);
        }
    }

    public void openEditLineActivity(SubwayLineView line) {
        SubwayBundleProvider bundle = new SubwayBundleProvider();
        commutator.goTo(ActivityOperationResult.EditSubwayLine,bundle.putLineId(line.getId()));
    }
    public void openEditStationActivity(SubwayStationView station) {
        SubwayBundleProvider bundle = new SubwayBundleProvider();
        commutator.goTo(ActivityOperationResult.EditSubwayStation,bundle.putStationId(station.getId()));
    }

    public SubwayView getSubway() {
        return subway;
    }


//    public void placeSelected(SavedPlacesActivity.SavePlaceView item) {
//        commutator.backTo(new SearchPlacesActivity.SearchPlacesBundleProvider().putFoundPlaceId(item.getPlace().id));
//    }

}
