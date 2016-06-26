package svp.com.dontmissplaces.ui;

import android.app.Activity;
import android.content.Intent;

import com.svp.infrastructure.mvpvs.bundle.IBundleProvider;

import java.util.HashMap;

import svp.com.dontmissplaces.MainMenuActivity;
import svp.com.dontmissplaces.ui.activities.HistoryTracksActivity;
import svp.com.dontmissplaces.ui.activities.SaveTrackActivity;
import svp.com.dontmissplaces.ui.activities.SearchPlacesActivity;
import svp.com.dontmissplaces.ui.activities.SettingsActivity;

public final class ActivityCommutator {
    public enum ActivityOperationResult {
        Undefined(0),
        MainMenu(1),
        HistoryTracks(2),
        SaveTrack(3),
        Settings(4),
        SearchPlaces(5);

        private final int code;

        ActivityOperationResult(int code) {
            this.code = code;
        }
        public int toInt() {
            return code;
        }
        public boolean is(int key) {
            return code == key;
        }

        public static ActivityOperationResult get(int resultCode) {
            return ActivityOperationResult.values()[resultCode];
//            switch (resultCode){
//                case 2:
//                    return HistoryTracks;
//                default:
//                    return Undefined;
//            }
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
        activities.put(ActivityOperationResult.Settings, SettingsActivity.class);
        activities.put(ActivityOperationResult.SearchPlaces, SearchPlacesActivity.class);
    }

    private final ICommutativeElement element;
    public ActivityCommutator(ICommutativeElement element) {
        this.element = element;
    }

    public void goTo(ActivityOperationResult operationTo) {
        goTo(activities.get(operationTo), null);
    }
    public void goTo(ActivityOperationResult operationTo, IBundleProvider bundleProvider) {
        goTo(activities.get(operationTo), bundleProvider);
    }

    /*catch result from onActivityResult on main activity*/
    public void backTo(IBundleProvider bundleProvider) {
        Activity activity = element.getActivity();

        Intent intent = new Intent();
        if(bundleProvider != null) {
            bundleProvider.putInto(intent);
        }

        activity.setResult(element.getOperation().toInt(),intent);
        activity.finish();
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
}
