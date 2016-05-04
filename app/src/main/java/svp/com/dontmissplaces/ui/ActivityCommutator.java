package svp.com.dontmissplaces.ui;

import android.app.Activity;
import android.content.Intent;

import java.util.HashMap;

import svp.com.dontmissplaces.MainMenuActivity;
import svp.com.dontmissplaces.ui.activities.HistoryTracksActivity;
import svp.com.dontmissplaces.ui.activities.SaveTrackActivity;

/**
 * Created by Pasha on 4/10/2016.
 */
public final class ActivityCommutator {
    public enum ActivityOperationResult {
        Undefined(0),
        MainMenu(1),
        HistoryTracks(2),
        SaveTrack(3);

        private final int code;

        private ActivityOperationResult(int code) {
            this.code = code;
        }
        public int toInt() {
            return code;
        }
    }

    public interface ICommutativeElement{
        ActivityOperationResult getOperation();
        Activity getActivity();
    }

    private static final HashMap<ActivityOperationResult,Class<?>> activities;

    static {
        activities = new HashMap<>();
        activities.put(ActivityOperationResult.MainMenu, MainMenuActivity.class);
        activities.put(ActivityOperationResult.HistoryTracks, HistoryTracksActivity.class);
        activities.put(ActivityOperationResult.SaveTrack, SaveTrackActivity.class);
    }

    private final ICommutativeElement element;
    public ActivityCommutator(ICommutativeElement element) {
        this.element = element;
    }

    public void goTo(ActivityOperationResult operationTo) {
        goTo(activities.get(operationTo), null);
    }
    public void goTo(ActivityOperationResult operationTo, BundleProvider bundleProvider) {
        goTo(activities.get(operationTo), bundleProvider);
    }

    /*catch result from onActivityResult on main activity*/
    public void backTo(BundleProvider bundleProvider) {
        Activity activity = element.getActivity();

        Intent intent = new Intent();
        if(bundleProvider != null) {
            bundleProvider.putInto(intent);
        }

        activity.setResult(Activity.RESULT_OK,intent);
        activity.finish();
    }

    private Activity goTo(Class<?> _class, BundleProvider bundleProvider){
        Activity activity = element.getActivity();

        Intent intent = new Intent(activity.getBaseContext(), _class);
        if(bundleProvider != null) {
            bundleProvider.putInto(intent);
        }

        activity.startActivityForResult(intent, element.getOperation().toInt());
        return activity;
    }
}
