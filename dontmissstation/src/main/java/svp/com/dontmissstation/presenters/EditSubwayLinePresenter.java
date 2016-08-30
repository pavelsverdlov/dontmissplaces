package svp.com.dontmissstation.presenters;

import android.content.Intent;

import com.svp.infrastructure.mvpvs.commutate.ActivityOperationItem;

import svp.com.dontmissstation.db.Repository;
import svp.com.dontmissstation.ui.activities.ActivityOperationResult;
import svp.com.dontmissstation.ui.activities.EditSubwayLineScrollingActivity;
import svp.com.dontmissstation.ui.model.StationView;
import svp.com.dontmissstation.ui.model.SubwayLineView;

public class EditSubwayLinePresenter extends CommutativePreferencePresenter<EditSubwayLineScrollingActivity,EditSubwayLineScrollingActivity.ViewState> {
    private final Repository repository;
    private SubwayLineView subwayLine;
    public EditSubwayLinePresenter(Repository repository) {
        super();
        this.repository = repository;
    }

    @Override
    protected void incomingResultFrom(ActivityOperationItem from, Intent data) {
        init(data);
    }

    @Override
    protected void onAttachedView(EditSubwayLineScrollingActivity view,  Intent intent){
        super.onAttachedView(view,intent);
        init(intent);
    }


    public SubwayLineView getLine() {
        return subwayLine;
    }

    public void openEditStationActivity(StationView station) {
        SubwayBundleProvider bundle = new SubwayBundleProvider();
        commutator.goTo(ActivityOperationResult.EditSubwayStation, bundle.putStationId(station.getId()));
    }

    private void init(Intent intent){
        SubwayBundleProvider bundle = new SubwayBundleProvider(intent);
        long id = bundle.getSubwayLineId();
        subwayLine = repository.getSubwayLineById(id);
    }
}
