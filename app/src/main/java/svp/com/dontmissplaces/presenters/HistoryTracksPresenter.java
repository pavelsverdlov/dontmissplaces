package svp.com.dontmissplaces.presenters;

import android.database.Cursor;
import android.widget.Toast;

import com.svp.infrastructure.mvpvs.presenter.Presenter;

import svp.com.dontmissplaces.db.Repository;
import svp.com.dontmissplaces.db.Track;
import svp.com.dontmissplaces.ui.ActivityCommutator;
import svp.com.dontmissplaces.ui.BaseBundleProvider;
import svp.com.dontmissplaces.ui.activities.HistoryTracksActivity;
import svp.com.dontmissplaces.ui.model.TrackView;

public class HistoryTracksPresenter extends CommutativePresenter<HistoryTracksActivity,HistoryTracksActivity.ViewState> {

    private final Repository repository;

    public HistoryTracksPresenter(Repository repository) {
        this.repository = repository;
    }

    @Override
    public void onAttachedView(HistoryTracksActivity view) {
        super.onAttachedView(view);
        CharSequence text = state.getBundle().getPreviousActionText();
        if(text != null) {
            state.getToast(text);
        }
    }


    public Cursor getCursorTracks(){
        return repository.getCursorTracks();
    }


    public void openTrack(TrackView trackview) {
        Track track = repository.getTrack(trackview.id);
        BaseBundleProvider bp = new BaseBundleProvider().putTrack(track);
        commutator.backTo(bp);
    }
}
