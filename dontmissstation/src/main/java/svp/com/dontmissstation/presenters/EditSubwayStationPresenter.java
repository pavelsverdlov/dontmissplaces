package svp.com.dontmissstation.presenters;

import android.content.Intent;

import com.svp.infrastructure.mvpvs.commutate.ActivityOperationItem;

import svp.com.dontmissstation.db.Repository;
import svp.com.dontmissstation.ui.activities.EditSubwayStationActivity;
import svp.com.dontmissstation.ui.model.StationView;

public class EditSubwayStationPresenter extends CommutativePreferencePresenter<EditSubwayStationActivity,EditSubwayStationActivity.ViewState> {
    public EditSubwayStationPresenter(Repository repository) {

    }

    @Override
    protected void incomingResultFrom(ActivityOperationItem from, Intent data) {

    }

    public StationView getStation() {
        return new StationView();
    }
}
