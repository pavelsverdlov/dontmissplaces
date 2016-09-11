package svp.com.dontmissstation.ui.model;

import android.app.Activity;
import android.support.design.widget.NavigationView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.widget.TextView;

import com.svp.infrastructure.common.view.NavigationViewDecorator;

import svp.com.dontmissstation.R;

public class MainNavigationView{
    private final Activity activity;

    public MainNavigationView(Activity activity) {
        this.activity = activity;
    }

    private NavigationView getNavigationView(){
        return (NavigationView) activity.findViewById(R.id.activity_main_nav_view);
    }

    public void setItemSelectedListener(NavigationView.OnNavigationItemSelectedListener listener){
        getNavigationView().setNavigationItemSelectedListener(listener);
    }

    public void initGeneralMenu(){
        NavigationViewDecorator nd = new NavigationViewDecorator(getNavigationView());
        nd.setMenu(R.menu.activity_main_drawer_menu_general);

    }
    public void initSubwayMenu(SubwayView subway){
        NavigationViewDecorator nd = new NavigationViewDecorator(getNavigationView());
        nd.setMenu(R.menu.activity_main_drawer_menu_selected_subway);

        Menu menu = nd.getMenu();
//        MenuItem lines = menu.findItem(R.id.main_activity_nave_menu_lines_group);
//        lines.setTitle("lines");
        int index = 0;
        SubMenu submenu = menu.addSubMenu(R.id.main_activity_nave_menu_lines_group,Menu.NONE,0,"Lines");
        submenu.setGroupCheckable(0,true,true);
        for (SubwayLineView line : subway.getLines()) {
            submenu.add(line.getName())//.add(lines.getItemId(),Menu.NONE,0,line.getName())
                    .setIcon(android.R.drawable.ic_menu_share);
            index++;
        }

        setSubwayHeader(subway);
    }

    private void setSubwayHeader(SubwayView subway) {
        ((TextView)getNavigationView().findViewById(R.id.activity_main_nav_header_subway_name))
                .setText(subway.getCountry() + " " + subway.getCity());
    }
}
