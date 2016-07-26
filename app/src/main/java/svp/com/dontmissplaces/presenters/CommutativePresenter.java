package svp.com.dontmissplaces.presenters;


import android.app.Activity;
import android.content.Intent;

import com.svp.infrastructure.mvpvs.commutate.ActivityOperationItem;
import com.svp.infrastructure.mvpvs.commutate.ICommutativeElement;
import com.svp.infrastructure.mvpvs.presenter.Presenter;
import com.svp.infrastructure.mvpvs.view.IActivityView;
import com.svp.infrastructure.mvpvs.viewstate.IViewState;

import svp.com.dontmissplaces.ui.ActivityCommutator;
import svp.com.dontmissplaces.ui.UserPreferenceSettings;

public abstract class CommutativePresenter
        <V extends IActivityView & ICommutativeElement,
         VS extends IViewState>
        extends com.svp.infrastructure.mvpvs.commutate.CommutativePresenter<V,VS> {
    protected UserPreferenceSettings userSettings;


    @Override
    protected ActivityOperationItem getOperation(int code){
        return ActivityCommutator.ActivityOperationResult.get(code);
    }

}
