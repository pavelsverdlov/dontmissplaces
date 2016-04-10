package svp.com.dontmissplaces.ui;

import android.app.Activity;
import android.content.Intent;

import java.util.HashMap;

import svp.com.dontmissplaces.MainMenuActivity;
import svp.com.dontmissplaces.ui.activities.HistoryTracksActivity;

/**
 * Created by Pasha on 4/10/2016.
 */
public final class ActivityCommutator {
    public enum ActivityOperationResult {
        Undefined(0),
        MainMenu(1),
        HistoryTracks(2);

        private final int code;

        private ActivityOperationResult(int code) {
            this.code = code;
        }
        public int toInt() {
            return code;
        }
    }

    private static final HashMap<ActivityOperationResult,Class<?>> activities;

    static {
        activities = new HashMap<>();
        activities.put(ActivityOperationResult.MainMenu, MainMenuActivity.class);
        activities.put(ActivityOperationResult.HistoryTracks, HistoryTracksActivity.class);
    }

    private final Activity activity;
    private final ActivityOperationResult operationFrom;
    public ActivityCommutator(Activity activity, ActivityOperationResult operation) {
        this.activity = activity;
        this.operationFrom = operation;
    }

    public void goToActivity(ActivityOperationResult operationTo) {
        goToActivity(activities.get(operationTo));
    }

    private void goToActivity(Class<?> _class){
        activity.startActivityForResult(new Intent(activity.getBaseContext(), _class), operationFrom.toInt());
    }
}
