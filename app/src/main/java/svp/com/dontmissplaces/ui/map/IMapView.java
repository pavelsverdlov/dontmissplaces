package svp.com.dontmissplaces.ui.map;

import android.location.Location;
import android.os.Bundle;

import java.util.Collection;

import svp.com.dontmissplaces.ui.model.SessionView;

public interface IMapView {

    void showSessionsTrack(Collection<SessionView> sessions);

    void onCreate(Bundle savedInstanceState);

    void onStart();

    void onStop();

    void onResume();

    void enableMyLocation();

    void startTrackRecording(SessionView session);

    void pauseTrackRecording();

    void resumeTrackRecording();

    void stopTrackRecording();

    Location getMyLocation();
}
