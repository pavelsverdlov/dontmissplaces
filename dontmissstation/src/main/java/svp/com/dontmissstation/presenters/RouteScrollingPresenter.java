package svp.com.dontmissstation.presenters;


import android.content.Intent;

import com.svp.infrastructure.mvpvs.commutate.ActivityOperationItem;

import svp.com.dontmissstation.db.Repository;
import svp.com.dontmissstation.ui.activities.RouteScrollingActivity;

public class RouteScrollingPresenter extends CommutativePreferencePresenter<RouteScrollingActivity,RouteScrollingActivity.ViewState> {

    public RouteScrollingPresenter(Repository repository) {

    }

    @Override
    protected void incomingResultFrom(ActivityOperationItem from, Intent data) {

    }
}
