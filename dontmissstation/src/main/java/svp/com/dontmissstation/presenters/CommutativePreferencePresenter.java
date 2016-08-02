package svp.com.dontmissstation.presenters;

import android.content.Intent;

import com.svp.infrastructure.mvpvs.commutate.ActivityOperationItem;
import com.svp.infrastructure.mvpvs.commutate.ICommutativeElement;
import com.svp.infrastructure.mvpvs.view.IActivityView;
import com.svp.infrastructure.mvpvs.viewstate.IViewState;

import svp.com.dontmissstation.ui.UserPreferenceSettings;
import svp.com.dontmissstation.ui.activities.*;

public abstract class CommutativePreferencePresenter
        <V extends IActivityView & ICommutativeElement,
                VS extends IViewState>
        extends com.svp.infrastructure.mvpvs.commutate.CommutativePresenter<V,VS> {
    protected UserPreferenceSettings userSettings;


    @Override
    protected ActivityOperationItem getOperation(int code){
        return ActivityOperationResult.get(code);
    }

    @Override
    protected void onAttachedView(V view,  Intent intent){
        userSettings = new UserPreferenceSettings(state.getActivity());
    }
}