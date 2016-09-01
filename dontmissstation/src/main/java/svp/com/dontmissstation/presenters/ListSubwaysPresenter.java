package svp.com.dontmissstation.presenters;

import android.content.Intent;
import android.hardware.camera2.CaptureRequest;

import com.svp.infrastructure.mvpvs.commutate.ActivityOperationItem;

import java.util.Collection;
import java.util.Vector;

import svp.com.dontmissstation.db.Repository;
import svp.com.dontmissstation.ui.activities.ActivityOperationResult;
import svp.com.dontmissstation.ui.activities.ListSubwaysActivity;
import svp.com.dontmissstation.ui.model.*;

public class ListSubwaysPresenter extends CommutativePreferencePresenter<ListSubwaysActivity,ListSubwaysActivity.ViewState> {
    private final Repository repository;

    public ListSubwaysPresenter(Repository repository) {

        this.repository = repository;
    }

    @Override
    protected void incomingResultFrom(ActivityOperationItem from, Intent data) {

    }

    public void subwaySelected(SubwayView item) {
        commutator.backTo(new SubwayBundleProvider().putSubwayId(item.getId()));
      //  commutator.goTo(ActivityOperationResult.Main,new SubwayBundleProvider().putSubwayId(item.getId()));
    }

    public Vector<SubwayView> getSubways() {
        return repository.getSubways();
    }
}
