package com.svp.infrastructure.mvpvs;

import com.svp.infrastructure.mvpvs.view.IActivityView;


public class Registrator {
    public static <T extends IActivityView> void register(Class<?> _class,
                                                           PresenterContainer.IPresenterCreator pcreator,
                                                           ViewStateContainer.IViewStateCreator<T> stateCreator){
        PresenterContainer.register(_class, pcreator);
        ViewStateContainer.Register(_class, stateCreator);
    }
}
