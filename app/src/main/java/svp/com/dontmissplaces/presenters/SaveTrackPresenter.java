package svp.com.dontmissplaces.presenters;

import svp.com.dontmissplaces.db.Repository;
import svp.com.dontmissplaces.db.Track;
import svp.com.dontmissplaces.ui.ActivityCommutator;
import svp.com.dontmissplaces.ui.BundleProvider;
import svp.com.dontmissplaces.ui.activities.SaveTrackActivity;

public class SaveTrackPresenter extends CommutativePresenter<SaveTrackActivity,SaveTrackActivity.ViewState> {

    private final Repository repository;
    private Track track;

    public SaveTrackPresenter(Repository repository) {
        this.repository = repository;
    }

    protected void onAttachedView(SaveTrackActivity view){
        super.onAttachedView(view);
        BundleProvider bundleProvider = state.getBundle();
        track = bundleProvider.getTrack();

        state.setTrackName(track.name);
    }

    public void save() {
        commutator.goTo(ActivityCommutator.ActivityOperationResult.HistoryTracks);
    }

    public void delete() {
        repository.deleteTrack(track);
        commutator.goTo(ActivityCommutator.ActivityOperationResult.HistoryTracks);
    }
}
