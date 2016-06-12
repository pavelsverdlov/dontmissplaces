package svp.com.dontmissplaces.presenters;

import android.content.Intent;

import com.google.android.gms.maps.model.LatLng;
import com.svp.infrastructure.mvpvs.bundle.IBundleProvider;

import org.osmdroid.util.BoundingBoxE6;
import org.osmdroid.util.GeoPoint;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import svp.com.dontmissplaces.MainMenuActivity;
import svp.com.dontmissplaces.db.Place;
import svp.com.dontmissplaces.db.Repository;
import svp.com.dontmissplaces.db.SessionTrack;
import svp.com.dontmissplaces.db.Track;
import svp.com.dontmissplaces.db.Waypoint;
import svp.com.dontmissplaces.model.Map.Point2D;
import svp.com.dontmissplaces.model.PlaceProvider;
import svp.com.dontmissplaces.ui.ActivityCommutator;
import svp.com.dontmissplaces.ui.BaseBundleProvider;
import svp.com.dontmissplaces.ui.map.MapViewTypes;
import svp.com.dontmissplaces.ui.model.SessionView;
import svp.com.dontmissplaces.ui.model.TrackView;

public class MainMenuPresenter extends CommutativePresenter<MainMenuActivity,MainMenuActivity.ViewState> {
    private TrackTimer timer;
    private final Repository repository;
    private Track recordingTrack;
    private SessionTrack recordingSession;
    private MapViewTypes mapViewType;

    public MainMenuPresenter(Repository repository) {
        this.repository = repository;
        mapViewType = MapViewTypes.Osmdroid;
    }

    public SessionView startNewTrackSession() {
        if(timer != null){
            timer.stop();
            timer = null;
        }
        timer = new TrackTimer(1000);
        state.expandTrackRecordingToolbar();

        if(recordingTrack == null){
            Date date = new Date();
            recordingTrack = new Track(-1,date.toString(),date.getTime());
        }

        timer.start();

        recordingTrack = repository.track.getOrInsertTrack(recordingTrack);
        recordingSession = repository.track.insertSession(recordingTrack);

        return new SessionView(recordingSession,new Vector<Waypoint>());
    }
    public void pauseTrackRecording() {
        timer.pause();
    }
    public void stopTrackRecording() {
        timer.cancel();
        state.slideOutFabTrackRecordingToolbar();

        IBundleProvider bp = new BaseBundleProvider().putTrack(recordingTrack);
        commutator.goTo(ActivityCommutator.ActivityOperationResult.SaveTrack, bp);
    }
    public void resumeTrackRecording() {
        timer.resume();
    }

    public void openHistoryTracks() {
        commutator.goTo(ActivityCommutator.ActivityOperationResult.HistoryTracks);
    }
    public void openSettings() {
        commutator.goTo(ActivityCommutator.ActivityOperationResult.Settings);
    }

    public void incomingResultFrom(ActivityCommutator.ActivityOperationResult from, Intent data) {
        if(ActivityCommutator.ActivityOperationResult.HistoryTracks == from){
            BaseBundleProvider bp = new BaseBundleProvider(data);
            recordingTrack = bp.getTrack();

            Vector<SessionView> sessions = new Vector<>();
            for (SessionTrack session : repository.track.getSessions(recordingTrack)){
                sessions.add(new SessionView(session,repository.track.getWaypoints(session)));
            }

            state.displayTrackOnMap(new TrackView(recordingTrack, sessions));
        }
    }

    public void showPlaceInfoByLocation(Point2D point) {
//        PlaceProvider pp = new PlaceProvider(state.getActivity());
//        Place p = pp.getPlace(point.getLatLng());
//        place p = pp.getPlace(new LatLng(46.4708294,30.7043384));

        int maxD = 100;
        GeoPoint p = point.getGeoPoint();
        BoundingBoxE6 bb = new BoundingBoxE6(p.getLatitudeE6()+maxD,
                p.getLongitudeE6()+maxD,
                p.getLatitudeE6()-maxD,
                p.getLongitudeE6()-maxD);

        Vector<Place> pois = repository.poi.get(bb);

        if(pois.size() > 0) {
            state.showPlaceInfo(pois.get(0));
        }
    }


    public MapViewTypes getMapViewType() {
        mapViewType = userSettings.getMapProvider();
        state.getActivity();
        return mapViewType;
    }



    private class TrackTimer extends TimerTask{
        private long elapsedMses;
        private long intervalMsec;

        private boolean isPause;
        private boolean isStop;

        public TrackTimer(long countDownInterval) {
            intervalMsec = countDownInterval;
        }

        public void start(){
            Timer timer = new Timer();
            timer.scheduleAtFixedRate(this, intervalMsec, intervalMsec);
        }

        public void pause(){
            isPause = true;
        }
        public void resume() {
            isPause = false;
        }

        public void stop(){
            isStop = true;
            this.cancel();
        }

        @Override
        public void run() {
            if(isPause || isStop){
                return;
            }
            elapsedMses += intervalMsec;
            state.updateTrackTime(elapsedMses);

//            Location loc = state.getLocation();
//            if(loc != null) {
//                state.updateTrackDispance(loc.toString());
//            }
        }
    }
}
