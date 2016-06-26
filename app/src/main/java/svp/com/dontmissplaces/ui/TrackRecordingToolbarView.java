package svp.com.dontmissplaces.ui;

import android.app.Activity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import svp.com.dontmissplaces.R;

/**
 * Created by Pasha on 4/10/2016.
 */
public class TrackRecordingToolbarView {


    public abstract static class OnResumePauseClickListener{
        public abstract void onPauseClick();
        public abstract void onResumeClick();
    }

    @Bind(R.id.track_recording_resume_pause) ImageButton resume_pause;
    @Bind(R.id.track_recording_stop) ImageButton stop;
    @Bind(R.id.track_recording_timer_output) TextView timeoutput;
    @Bind(R.id.track_recording_distance_output) TextView disoutput;
    @Bind(R.id.track_recording_nav_header_toolbar) View view;

    public boolean isPause;

    public TrackRecordingToolbarView(Activity activity) {
        ButterKnife.bind(this, activity);
    }

    public void show() {
        view.setVisibility(View.VISIBLE);
    }
    public void hide() {
        view.setVisibility(View.GONE);
    }

    public void updateTime(long millis){
        long hour = TimeUnit.MILLISECONDS.toHours(millis);
        millis -= TimeUnit.HOURS.toMillis(hour);

        long minute = TimeUnit.MILLISECONDS.toMinutes(millis);
        millis -= TimeUnit.MINUTES.toMillis(minute);

        long second = TimeUnit.MILLISECONDS.toSeconds(millis);

        timeoutput.setText(String.format("%02d:%02d:%02d", hour, minute, second));
    }
    public void updateDispance(String dis){
        disoutput.setText(dis);
    }

    public void setClickListeners(final OnResumePauseClickListener resumePauseListener,
                                  View.OnClickListener stopListener){
        resume_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int res = isPause ? R.drawable.ic_pause_white_48dp : R.drawable.ic_play_arrow_white_48dp;
                resume_pause.setImageResource(res);
                isPause = !isPause;
                if(isPause) {
                    resumePauseListener.onPauseClick();
                }else{
                    resumePauseListener.onResumeClick();
                }
            }
        });
        stop.setOnClickListener(stopListener);
    }

}
