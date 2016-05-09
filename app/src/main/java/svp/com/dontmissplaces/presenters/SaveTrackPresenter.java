package svp.com.dontmissplaces.presenters;

import svp.com.dontmissplaces.R;
import svp.com.dontmissplaces.db.Repository;
import svp.com.dontmissplaces.db.Track;
import svp.com.dontmissplaces.ui.ActivityCommutator;
import com.svp.infrastructure.mvpvs.bundle.BundleProvider;
import com.svp.infrastructure.mvpvs.bundle.IBundleProvider;

import svp.com.dontmissplaces.ui.BaseBundleProvider;
import svp.com.dontmissplaces.ui.activities.SaveTrackActivity;

public class SaveTrackPresenter extends CommutativePresenter<SaveTrackActivity,SaveTrackActivity.ViewState> {

    private final Repository repository;
    private Track track;

    public SaveTrackPresenter(Repository repository) {
        this.repository = repository;
    }

    protected void onAttachedView(SaveTrackActivity view){
        super.onAttachedView(view);
        BaseBundleProvider bundleProvider = state.getBundle();
        track = bundleProvider.getTrack();

        state.setTrackName(track.name);
    }

    public void save() {
        IBundleProvider bp = BundleProvider.create()
                .putActionText(state.getString(R.string.snackbar_activity_save_track_was_saved));
        commutator.goTo(ActivityCommutator.ActivityOperationResult.HistoryTracks, bp);
    }

    public void delete() {
        repository.deleteTrack(track);
        IBundleProvider bp = BundleProvider.create()
                .putActionText(state.getString(R.string.snackbar_activity_save_track_was_deleted));
        commutator.goTo(ActivityCommutator.ActivityOperationResult.HistoryTracks, bp);
    }
}
