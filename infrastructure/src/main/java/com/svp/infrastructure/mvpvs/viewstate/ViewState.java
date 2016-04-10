package com.svp.infrastructure.mvpvs.viewstate;

import android.app.Activity;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.svp.infrastructure.mvpvs.view.IActivityView;
import com.svp.infrastructure.mvpvs.view.IViewAction;

public abstract class ViewState<V extends IActivityView> implements IViewState {
    protected V view;
    protected ViewState(V view){
        this.view = view;
    }
    public void addAction(IViewAction<V> action){
        view.executeAction(action);
    }

    public void refresh(IActivityView view){
        this.view = (V)view;
        restore();
    }

    protected abstract void restore();

    protected abstract Activity getActivity();

    public Toast getToast(CharSequence text){
        return Toast.makeText(getActivity(),text,Toast.LENGTH_SHORT);
    }
    public Snackbar getSnackbar(CharSequence text){
        /*
        return Snackbar.make(getActivity().getWindow().getDecorView(),
                    text, Snackbar.LENGTH_LONG)
                .setAction("Action", null);
                */
//top bar
        Snackbar snackbar = Snackbar.make(getActivity().getWindow().getDecorView(),
                text, Snackbar.LENGTH_INDEFINITE);
                    View v = snackbar.getView();
        FrameLayout.LayoutParams params =(FrameLayout.LayoutParams)v.getLayoutParams();
                    params.gravity = Gravity.TOP;
                    v.setLayoutParams(params);
                 return snackbar;
    }

}