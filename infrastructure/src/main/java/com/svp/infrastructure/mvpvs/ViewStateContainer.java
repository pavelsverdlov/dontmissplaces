package com.svp.infrastructure.mvpvs;

import android.app.Activity;

import com.svp.infrastructure.mvpvs.view.IActivityView;
import com.svp.infrastructure.mvpvs.viewstate.IViewState;

import java.util.HashMap;

public class ViewStateContainer {
    public interface IViewStateCreator<V extends Activity & IActivityView>{
        IViewState create(V view);
    }

    private static final HashMap<Class<?>,IViewStateCreator> creators;
    private static final HashMap<Class<?>,IViewState> states;

    static {
        creators = new HashMap<>();
        states = new HashMap<>();
    }

    public static void Register(Class<?> type, IViewStateCreator cr){
        creators.put(type, cr);
    }

    public <V extends Activity & IActivityView, VS extends IViewState> VS addView(V view) {
        Class<?> type = view.getClass();
        if(states.containsKey(type)) {
            states.get(type).refresh(view);
        } else{
            states.put(type, creators.get(type).create(view));
        }
        return (VS)states.get(type);
    }
    public <V extends Activity & IActivityView> void removeView(V view) {
//        UUID id = view.getId();
        Class<?> type = view.getClass();
        states.remove(type);
    }
}
