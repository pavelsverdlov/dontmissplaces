package svp.com.dontmissplaces.presenters;


import com.svp.infrastructure.mvpvs.presenter.Presenter;
import com.svp.infrastructure.mvpvs.view.IActivityView;
import com.svp.infrastructure.mvpvs.viewstate.IViewState;

import svp.com.dontmissplaces.ui.ActivityCommutator;

public abstract class CommutativePresenter
        <V extends IActivityView & ActivityCommutator.ICommutativeElement,
         VS extends IViewState>
        extends Presenter<V,VS> {
    protected ActivityCommutator commutator;

    @Override
    protected void onAttachedView(V view){
        super.onAttachedView(view);
        commutator = new ActivityCommutator(view);
    }
}
