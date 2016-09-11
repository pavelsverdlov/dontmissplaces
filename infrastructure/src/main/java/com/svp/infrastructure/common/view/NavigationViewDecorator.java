package com.svp.infrastructure.common.view;


import android.app.Activity;
import android.support.design.widget.NavigationView;
import android.view.Menu;

public class NavigationViewDecorator {
    private final NavigationView navigation;

    public NavigationViewDecorator(NavigationView navigation){
        this.navigation = navigation;
    }
    public void setMenu(int resourceMenuId){
        navigation.getMenu().clear();
        navigation.inflateMenu(resourceMenuId);
    }

    public Menu getMenu() {
        return navigation.getMenu();
    }
}
