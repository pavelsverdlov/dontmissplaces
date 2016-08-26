package svp.com.dontmissstation.presenters;

import android.content.Intent;

import com.svp.infrastructure.mvpvs.commutate.ActivityOperationItem;

import svp.com.dontmissstation.db.Repository;
import svp.com.dontmissstation.ui.activities.AddSubwayLineActivity;
import svp.com.dontmissstation.ui.model.SubwayLineView;

public class AddSubwayLinePresenter extends CommutativePreferencePresenter<AddSubwayLineActivity,AddSubwayLineActivity.ViewState> {
    public AddSubwayLinePresenter(Repository repository) {
        super();
    }

    @Override
    protected void incomingResultFrom(ActivityOperationItem from, Intent data) {

    }

    public SubwayLineView getLine() {
        return new SubwayLineView();
    }
}
