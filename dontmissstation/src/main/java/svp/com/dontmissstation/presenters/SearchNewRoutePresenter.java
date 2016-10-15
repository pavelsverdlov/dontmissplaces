package svp.com.dontmissstation.presenters;

import android.content.Intent;

import com.svp.infrastructure.mvpvs.commutate.ActivityOperationItem;

import java.util.Collection;

import svp.com.dontmissstation.db.Repository;
import svp.com.dontmissstation.model.BundleRepository;
import svp.com.dontmissstation.ui.activities.PickOnMapActivity;
import svp.com.dontmissstation.ui.activities.SearchNewRouteActivity;
import svp.com.dontmissstation.ui.model.SubwayStationView;
import svp.com.dontmissstation.ui.model.SubwayView;

public class SearchNewRoutePresenter extends CommutativePreferencePresenter<SearchNewRouteActivity,SearchNewRouteActivity.ViewState>{
    private final Repository repository;
    private SubwayView subway;

    public SearchNewRoutePresenter(Repository repository) {
        this.repository = repository;
    }

    @Override
    protected void incomingResultFrom(ActivityOperationItem from, Intent data) {
        subway = BundleRepository.getSubway(data,repository);
    }
    @Override
    protected void onAttachedView(SearchNewRouteActivity view, Intent intent) {
        super.onAttachedView(view, intent);
        subway = BundleRepository.getSubway(intent,repository);
    }

    public void searchNewRoute(String from, String to) {
        
    }

    public Collection<SubwayStationView> getSubwayStations() {
        return subway.getAllStations();
    }
}
