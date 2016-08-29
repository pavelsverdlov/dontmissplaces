package svp.com.dontmissstation.presenters;

import android.content.Intent;

import com.svp.infrastructure.mvpvs.commutate.ActivityOperationItem;

import svp.com.dontmissstation.db.Repository;
import svp.com.dontmissstation.ui.activities.EditSubwayStationActivity;

public class EditSubwayStationPresenter extends CommutativePreferencePresenter<EditSubwayStationActivity,EditSubwayStationActivity.ViewState> {
    public EditSubwayStationPresenter(Repository repository) {

    }

    @Override
    protected void incomingResultFrom(ActivityOperationItem from, Intent data) {

    }
}
