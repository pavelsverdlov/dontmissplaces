package svp.com.dontmissstation.presenters;

import android.content.Intent;

import com.svp.infrastructure.mvpvs.commutate.ActivityOperationItem;

import java.util.Collection;

import svp.com.dontmissstation.db.Repository;
import svp.com.dontmissstation.ui.activities.StationListActivity;
import svp.com.dontmissstation.ui.model.SubwayStationView;

public class StationListPresenter extends CommutativePreferencePresenter<StationListActivity,StationListActivity.ViewState>{
    public StationListPresenter(Repository repository) {

    }

    @Override
    protected void incomingResultFrom(ActivityOperationItem from, Intent data) {

    }

    public Collection<SubwayStationView> getStation() {
        return null;
    }
}
