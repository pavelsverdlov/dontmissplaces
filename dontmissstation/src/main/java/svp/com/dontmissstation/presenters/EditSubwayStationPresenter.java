package svp.com.dontmissstation.presenters;

import android.content.Intent;

import com.svp.infrastructure.mvpvs.bundle.IBundleProvider;
import com.svp.infrastructure.mvpvs.commutate.ActivityOperationItem;

import java.util.Collection;
import java.util.Vector;

import svp.com.dontmissstation.db.Repository;
import svp.com.dontmissstation.model.BundleRepository;
import svp.com.dontmissstation.ui.activities.ActivityOperationResult;
import svp.com.dontmissstation.ui.activities.EditSubwayStationActivity;
import svp.com.dontmissstation.ui.model.SubwayStationView;
import svp.com.dontmissstation.ui.model.SubwayLineView;

public class EditSubwayStationPresenter extends CommutativePreferencePresenter<EditSubwayStationActivity,EditSubwayStationActivity.ViewState> {
    private final Repository repository;
    private SubwayStationView station;

    public EditSubwayStationPresenter(Repository repository) {
        this.repository = repository;
    }

    @Override
    protected void incomingResultFrom(ActivityOperationItem from, Intent data) {
        init(data);
    }

    @Override
    protected void onAttachedView(EditSubwayStationActivity view,  Intent intent){
        super.onAttachedView(view,intent);
        init(intent);
    }

    public SubwayStationView getStation() {
        return station;
    }

    public Collection<SubwayLineView> getAvailableLines() {
        long sid = station.getOwnSubwayId();
        return repository.getSubwayLinesBySubwayId(sid);
    }

    private void init(Intent intent){
        station = BundleRepository.getStation(intent,repository);
    }

    public void openMapActivity() {
        IBundleProvider bundle = new SubwayBundleProvider()
                .putStationId(station.getId());
        commutator.goTo(ActivityOperationResult.PickOnMap, bundle);
    }
}
