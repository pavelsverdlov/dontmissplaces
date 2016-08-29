package svp.com.dontmissstation.presenters;

import android.content.Intent;

import com.svp.infrastructure.mvpvs.commutate.ActivityOperationItem;

import svp.com.dontmissstation.db.Repository;
import svp.com.dontmissstation.ui.activities.EditSubwayLineScrollingActivity;
import svp.com.dontmissstation.ui.model.SubwayLineView;

public class EditSubwayLinePresenter extends CommutativePreferencePresenter<EditSubwayLineScrollingActivity,EditSubwayLineScrollingActivity.ViewState> {
    public EditSubwayLinePresenter(Repository repository) {
        super();
    }

    @Override
    protected void incomingResultFrom(ActivityOperationItem from, Intent data) {

    }

    public SubwayLineView getLine() {
        return new SubwayLineView();
    }

    public void openEditStationActivity() {

    }
}
