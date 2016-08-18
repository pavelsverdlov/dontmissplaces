package svp.app.map;

import android.os.Bundle;

import svp.app.map.model.IPOIView;
import svp.app.map.model.Point2D;

public interface IMapView {

    void onCreate(Bundle savedInstanceState);

    void onStart();

    void onStop();

    void onResume();

    void setOnMapClickListener(OnMapClickListener listener);

    void moveTo(Point2D point);

    void drawMarker(IPOIView poi,int markerIdResource);
}
