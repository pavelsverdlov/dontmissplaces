package com.svp.infrastructure.mvpvs;

import com.svp.infrastructure.mvpvs.presenter.IPresenter;
import com.svp.infrastructure.mvpvs.presenter.Presenter;

import java.util.ArrayList;
import java.util.HashMap;

public class PresenterContainer {
    public interface IPresenterCreator{
        IPresenter create();
    }

    private final static HashMap<Class<?>,IPresenterCreator> creator;
    private final static HashMap<Class<?>,IPresenter> presenters;

    static {
        creator = new HashMap<>();
        presenters = new HashMap<>();
    }

    public static void Register(Class<?> viewType,IPresenterCreator cr){
        creator.put(viewType, cr);
    }

    private ArrayList<IPresenter> getPresenters(Class<?> type){
        ArrayList<IPresenter> press = new ArrayList<>();
        IPresenterCreator p = creator.get(type);

        //for (IPresenterCreator p: creators) {
            IPresenter presenter;
            if(presenters.containsKey(type)){
                presenter = presenters.get(type);
            }else{
                presenter = p.create();
                presenters.put(type,presenter);
            }
            press.add(presenter);
        //}

        return press;
    }

    public <P extends Presenter> P get(Class<?> viewType){
        return (P)getPresenters(viewType).get(0);
    }

}
