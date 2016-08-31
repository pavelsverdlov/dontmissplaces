package svp.com.dontmissstation.presenters;

import android.content.Intent;

import com.svp.infrastructure.mvpvs.commutate.ActivityOperationItem;

import svp.com.dontmissstation.ui.activities.ListSubwaysActivity;
import svp.com.dontmissstation.ui.model.*;

public class ListSubwaysPresenter extends CommutativePreferencePresenter<ListSubwaysActivity,ListSubwaysActivity.ViewState> {
    @Override
    protected void incomingResultFrom(ActivityOperationItem from, Intent data) {

    }

    public void subwaySelected(SubwayView item) {
    }
}
