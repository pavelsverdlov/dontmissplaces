package svp.com.dontmissplaces.ui.map;

import android.location.Location;
import android.os.Bundle;

import com.svp.infrastructure.mvpvs.view.IActivityView;

import java.util.Collection;
import java.util.Vector;

import svp.com.dontmissplaces.db.Place;
import svp.com.dontmissplaces.ui.model.SessionView;

public interface IMapView extends IActivityView {

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

    void setOnMapClickListener(OnMapClickListener listener);
    void drawMarker(Place pois);
}
