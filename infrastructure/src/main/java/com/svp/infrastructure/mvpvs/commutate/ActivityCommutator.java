package com.svp.infrastructure.mvpvs.commutate;

import android.app.Activity;
import android.content.Intent;

import com.svp.infrastructure.mvpvs.bundle.IBundleProvider;

import java.util.HashMap;

public class ActivityCommutator {
    private final ICommutativeElement element;
    public ActivityCommutator(ICommutativeElement element) {
        this.element = element;
    }

    /*catch result from onActivityResult on main activity*/
    public void backTo(IBundleProvider bundleProvider) {
        Activity activity = element.getActivity();

        Intent intent = new Intent();
        if(bundleProvider != null) {
            bundleProvider.putInto(intent);
        }

        if(activity.getParent() != null){
            activity.getParent().setResult(element.getOperation().toInt(),intent);
        }
        activity.setResult(element.getOperation().toInt(),intent);
        activity.finish();
//        activity.onBackPressed();
    }

    protected static final HashMap<ActivityOperationItem,Class<?>> activities;

    static {
        activities = new HashMap<>();
    }

    private Activity goTo(Class<?> _class, IBundleProvider bundleProvider){
        Activity activity = element.getActivity();

        Intent intent = new Intent(activity.getBaseContext(), _class);
        if(bundleProvider != null) {
            bundleProvider.putInto(intent);
        }

        activity.startActivityForResult(intent, element.getOperation().toInt());
        return activity;
    }


    public void goTo(ActivityOperationItem operationTo) {
        goTo(activities.get(operationTo), null);
    }
    public void goTo(ActivityOperationItem operationTo, IBundleProvider bundleProvider) {
        goTo(activities.get(operationTo), bundleProvider);
    }
}
