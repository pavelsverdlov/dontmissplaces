package svp.com.dontmissplaces.presenters;

import android.database.Cursor;

import svp.com.dontmissplaces.db.Repository;
import svp.com.dontmissplaces.db.Track;
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
        return repository.track.getCursorTracks();
    }

    public void openTrack(TrackView trackview) {
        Track track = repository.track.getTrack(trackview.id);
        BaseBundleProvider bp = new BaseBundleProvider().putTrack(track);
        commutator.backTo(bp);
    }

//    public void openTrack(TrackView trackview) {
//        track track = repository.getTrack(trackview.id);
//
//        Location l = new Location("");l.setLatitude(24.3454523d);l.setLongitude(10.123450);
//        repository.insertWaypoint(new Waypoint(track.id,l));
//        l = new Location("");l.setLatitude(24.3554523d);l.setLongitude(10.123450);
//        repository.insertWaypoint(new Waypoint(track.id,l));
//        l = new Location("");l.setLatitude(24.3654523d);l.setLongitude(10.123450);
//        repository.insertWaypoint(new Waypoint(track.id,l));
//        l = new Location("");l.setLatitude(24.3754523d);l.setLongitude(10.123450);
//        repository.insertWaypoint(new Waypoint(track.id,l));
//
//        BaseBundleProvider bp = new BaseBundleProvider().putTrack(track);
//        commutator.backTo(bp);
//    }
}
