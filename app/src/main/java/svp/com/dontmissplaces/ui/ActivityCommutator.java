package svp.com.dontmissplaces.ui;

import android.app.Activity;
import android.content.Intent;

import com.svp.infrastructure.mvpvs.bundle.IBundleProvider;
import com.svp.infrastructure.mvpvs.commutate.ActivityOperationItem;
import com.svp.infrastructure.mvpvs.commutate.ICommutativeElement;

import java.util.HashMap;

import svp.com.dontmissplaces.MainMenuActivity;
import svp.com.dontmissplaces.ui.activities.HistoryTracksActivity;
import svp.com.dontmissplaces.ui.activities.SaveTrackActivity;
import svp.com.dontmissplaces.ui.activities.SearchPlacesActivity;
import svp.com.dontmissplaces.ui.activities.SettingsActivity;

public final class ActivityCommutator extends com.svp.infrastructure.mvpvs.commutate.ActivityCommutator{

    public ActivityCommutator(ICommutativeElement element) {
        super(element);
    }

    public static final class ActivityOperationResult {
        public static final ActivityOperationItem Undefined= new ActivityOperationItem(0);
        public static final ActivityOperationItem MainMenu = new ActivityOperationItem(1);
        public static final ActivityOperationItem HistoryTracks= new ActivityOperationItem(2);
        public static final ActivityOperationItem SaveTrack= new ActivityOperationItem(3);
        public static final ActivityOperationItem Settings= new ActivityOperationItem(4);
        public static final ActivityOperationItem SearchPlaces= new ActivityOperationItem(5);

        public static ActivityOperationItem get(int resultCode) {
            switch (resultCode){
                case 1:
                    return MainMenu;
                case 2:
                    return HistoryTracks;
                case 3:
                    return SaveTrack;
                case 4:
                    return Settings;
                case 5:
                    return SearchPlaces;
                default:
                    return Undefined;
            }
        }
    }


    public static void register() {
        activities.put(ActivityOperationResult.MainMenu, MainMenuActivity.class);
        activities.put(ActivityOperationResult.HistoryTracks, HistoryTracksActivity.class);
        activities.put(ActivityOperationResult.SaveTrack, SaveTrackActivity.class);
        activities.put(ActivityOperationResult.Settings, SettingsActivity.class);
        activities.put(ActivityOperationResult.SearchPlaces, SearchPlacesActivity.class);
    }


}
