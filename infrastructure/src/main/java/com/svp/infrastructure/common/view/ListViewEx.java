package com.svp.infrastructure.common.view;

import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.Adapter;
import android.widget.ListView;

import java.lang.reflect.Field;

public class ListViewEx {
    /**
     * calculate height by Adapter items amd set it to ListView height params
     */
    public static void setHeightListView(ListView listView, Adapter listAdapter){
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    public static void getMenuItemsView(Activity a, final Menu m) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException{
        View homeButton = a.findViewById(android.R.id.home);
        ViewParent parentOfHome = homeButton.getParent().getParent(); //ActionBarView is parent of home ImageView, see layout file in sources

        if (!parentOfHome.getClass().getName().contains("ActionBarView")) {
            parentOfHome = parentOfHome.getParent();
            Class absAbv = parentOfHome.getClass().getSuperclass(); //ActionBarView -> AbsActionBarView class
            Field actionMenuPresenterField = absAbv.getDeclaredField("mActionMenuPresenter");
            actionMenuPresenterField.setAccessible(true);
            Object actionMenuPresenter = actionMenuPresenterField.get(parentOfHome);
            Field actionMenuViewField = actionMenuPresenter.getClass().getSuperclass().getDeclaredField("mMenuView");
            actionMenuViewField.setAccessible(true);
            Object actionMenuView = actionMenuViewField.get(actionMenuPresenter);
            Field childrenField= actionMenuView.getClass().getSuperclass().getSuperclass().getDeclaredField("mChildren");
            childrenField.setAccessible(true);
            Field menuField =actionMenuPresenter.getClass().getSuperclass().getDeclaredField("mMenu");
            menuField.setAccessible(true);
            Object menu = menuField.get(actionMenuPresenter);
            Object[] menuItemsAsViews = (Object[])childrenField.get(actionMenuView);
            View.OnLongClickListener longListener = new View.OnLongClickListener() {

                @Override
                public boolean onLongClick(View v) {
                    return true;
                   // return listener.onMenuItemLongClik(m.findItem(v.getId()));
                }
            };
            for(Object menuView:menuItemsAsViews ){
                View v = (View)menuView;
                v.setOnLongClickListener(longListener);
            }


        }
    }
}
