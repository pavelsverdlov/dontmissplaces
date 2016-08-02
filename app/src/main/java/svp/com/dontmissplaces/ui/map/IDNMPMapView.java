package svp.com.dontmissplaces.ui.map;

import android.os.Bundle;

import com.svp.infrastructure.mvpvs.view.IActivityView;

import java.util.Collection;

import svp.app.map.IMapView;
import svp.app.map.OnMapClickListener;
import svp.app.map.model.Point2D;
import svp.com.dontmissplaces.ui.model.IPOIView;
import svp.com.dontmissplaces.ui.model.SessionView;

public interface IDNMPMapView extends IActivityView, IMapView {

    void showSessionsTrack(Collection<SessionView> sessions);

    void enableMyLocation();

    void startTrackRecording(SessionView session);

//    void pauseTrackRecording();

    void stopTrackRecording();

    void drawMarker(IPOIView poi);
}
