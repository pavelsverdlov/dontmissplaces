package svp.com.dontmissplaces.presenters;

import android.content.Intent;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import svp.com.dontmissplaces.MainMenuActivity;
import svp.com.dontmissplaces.db.Repository;
import svp.com.dontmissplaces.db.Track;
import svp.com.dontmissplaces.ui.ActivityCommutator;
import svp.com.dontmissplaces.ui.BaseBundleProvider;

import com.svp.infrastructure.mvpvs.bundle.BundleProvider;
import com.svp.infrastructure.mvpvs.bundle.IBundleProvider;

public class MainMenuPresenter extends CommutativePresenter<MainMenuActivity,MainMenuActivity.ViewState> {
    private TrackTimer timer;
    private final Repository repository;
    private Track recordingTrack;

    public MainMenuPresenter(Repository repository) {
        this.repository = repository;
    }

    public Track startTrackRecording() {
        if(timer != null){
            timer.stop();
            timer = null;
        }
        timer = new TrackTimer(1000);
        state.expandTrackRecordingToolbar();

        timer.start();
        recordingTrack = repository.insertTrack(new Date().toString());
        return recordingTrack;
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

    public void incomingResultFrom(int resultCode, Intent data) {
        if(ActivityCommutator.ActivityOperationResult.HistoryTracks.is(resultCode)){
            BaseBundleProvider bp = new BaseBundleProvider(data);
            if(ActivityCommutator.ActivityOperationResult.HistoryTracks.is(resultCode)){
                Track track = bp.getTrack();
                state.displayTrackOnMap(track);
            }
        }
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
