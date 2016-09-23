package svp.com.dontmissstation.presenters;


import android.content.Intent;

import com.svp.infrastructure.mvpvs.commutate.ActivityOperationItem;

import java.util.Collection;

import svp.com.dontmissstation.db.Repository;
import svp.com.dontmissstation.ui.activities.RouteScrollingActivity;
import svp.com.dontmissstation.ui.model.SubwayStationView;
import svp.com.dontmissstation.ui.model.SubwayView;

public class RouteScrollingPresenter extends CommutativePreferencePresenter<RouteScrollingActivity,RouteScrollingActivity.ViewState> {

    private final Repository repository;
    private SubwayView subway;

    public RouteScrollingPresenter(Repository repository) {
        this.repository = repository;
    }

    @Override
    protected void incomingResultFrom(ActivityOperationItem from, Intent data) {
        initSubway(data);
    }
    @Override
    protected void onAttachedView(RouteScrollingActivity view, Intent intent){
        super.onAttachedView(view,intent);
        initSubway(intent);
    }

    public Collection<SubwayStationView> getStation() {
        return subway.getAllStations();
    }

    private void initSubway(Intent data){
        SubwayBundleProvider bundle = new SubwayBundleProvider(data);
        long id = bundle.getSubwayId();
        subway = repository.getSubwayById(id);
    }
}
