package com.svp.infrastructure.mvpvs.commutate;

import android.app.Activity;
import android.content.Intent;

import com.svp.infrastructure.mvpvs.presenter.Presenter;
import com.svp.infrastructure.mvpvs.view.IActivityView;
import com.svp.infrastructure.mvpvs.viewstate.IViewState;

public abstract class CommutativePresenter
        <V extends IActivityView & ICommutativeElement,
                VS extends IViewState>
        extends Presenter<V,VS> {
    protected ActivityCommutator commutator;

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if (Activity.RESULT_CANCELED == resultCode) {
            return;
        }
        incomingResultFrom(getOperation(resultCode), data);
    }

    protected abstract ActivityOperationItem getOperation(int code);
    protected abstract void incomingResultFrom(ActivityOperationItem from, Intent data);

    @Override
    protected final void onAttachedView(V view){
        super.onAttachedView(view);
        commutator = new ActivityCommutator(view);

        onAttachedView(view, view.getActivity().getIntent());
    }

    protected abstract void onAttachedView(V view,  Intent intent);
}