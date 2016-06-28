package svp.com.dontmissplaces.presenters;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.svp.infrastructure.mvpvs.bundle.BundleProvider;
import com.svp.infrastructure.mvpvs.presenter.Presenter;
import com.svp.infrastructure.mvpvs.view.IActivityView;
import com.svp.infrastructure.mvpvs.viewstate.IViewState;

import svp.com.dontmissplaces.ui.ActivityCommutator;
import svp.com.dontmissplaces.ui.UserPreferenceSettings;

public abstract class CommutativePresenter
        <V extends IActivityView & ActivityCommutator.ICommutativeElement,
         VS extends IViewState>
        extends Presenter<V,VS> {
    protected ActivityCommutator commutator;
    protected UserPreferenceSettings userSettings;

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if (Activity.RESULT_CANCELED == resultCode) {
            return;
        }
        incomingResultFrom(ActivityCommutator.ActivityOperationResult.get(resultCode), data);
    }

    protected abstract void incomingResultFrom(ActivityCommutator.ActivityOperationResult from, Intent data);

    @Override
    protected final void onAttachedView(V view){
        super.onAttachedView(view);
        commutator = new ActivityCommutator(view);
        userSettings = new UserPreferenceSettings(state.getActivity());

        onAttachedView(view, view.getActivity().getIntent());
    }

    protected abstract void onAttachedView(V view,  Intent intent);
}
