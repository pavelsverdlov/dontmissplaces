package svp.com.dontmissstation.ui;

import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import svp.com.dontmissstation.ui.activities.AddNewSubwayActivity;

/**
 * Created by Pasha on 8/26/2016.
 */
public class RecyclerViewEx {

    public static void setCustomSettings(RecyclerView view, Activity activity) {
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        view.setHasFixedSize(true);
        // use a linear layout manager
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(activity);
        view.setLayoutManager(mLayoutManager);
    }
}
