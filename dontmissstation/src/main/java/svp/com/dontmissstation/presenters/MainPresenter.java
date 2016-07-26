package svp.com.dontmissstation.presenters;

import android.content.Intent;

import com.svp.infrastructure.mvpvs.commutate.ActivityOperationItem;
import com.svp.infrastructure.mvpvs.commutate.CommutativePresenter;

import svp.com.dontmissstation.MainActivity;

public class MainPresenter extends CommutativePresenter<MainActivity,MainActivity.ViewState> {
    @Override
    protected ActivityOperationItem getOperation(int code) {
        return null;
    }

    @Override
    protected void incomingResultFrom(ActivityOperationItem from, Intent data) {

    }

    @Override
    protected void onAttachedView(MainActivity view, Intent intent) {

    }
}
