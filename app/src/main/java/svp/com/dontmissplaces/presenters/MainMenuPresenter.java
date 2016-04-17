package svp.com.dontmissplaces.presenters;

import android.location.Location;

import com.svp.infrastructure.mvpvs.presenter.Presenter;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import svp.com.dontmissplaces.MainMenuActivity;
import svp.com.dontmissplaces.db.Repository;


/**
 * Created by Pasha on 4/9/2016.
 */
public class MainMenuPresenter extends Presenter<MainMenuActivity,MainMenuActivity.ViewState> {
    private TrackTimer timer;
    public MainMenuPresenter(Repository repository) {

    }

    public void startTrackRecording() {
        if(timer != null){
            timer.stop();
            timer = null;
        }
        timer = new TrackTimer(1000);
        state.expandTrackRecordingToolbar();

        timer.start();
    }
    public void pauseTrackRecording() {
        timer.pause();
    }
    public void stopTrackRecording() {
        timer.cancel();
        state.slideOutFabTrackRecordingToolbar();
    }
    public void resumeTrackRecording() {
        timer.resume();
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

            Location loc = state.getLocation();
            if(loc != null) {
                state.updateTrackDispance(loc.toString());
            }
        }
    }
}
