package svp.app.map;

import android.os.Bundle;

import java.util.UUID;

import svp.app.map.model.IMapCircle;
import svp.app.map.model.IMapPolyline;
import svp.app.map.model.IPOIView;
import svp.app.map.model.Point2D;

public interface IMapView {

    void onCreate(Bundle savedInstanceState);

    void onStart();

    void onStop();

    void onResume();

    void setOnMapClickListener(OnMapClickListener listener);

    void moveTo(Point2D point);

    UUID addMarker(IPOIView poi, int markerIdResource);
    void removeMarker(UUID id);

    IMapPolyline createPolyline();
    IMapCircle createCircle();
}

