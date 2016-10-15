package svp.app.map.android;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;

import svp.app.map.model.IMapCircle;
import svp.app.map.model.Point2D;

public class GMapCircle implements IMapCircle {
    private final GoogleMap map;
    private Circle circle;
    public GMapCircle(GoogleMap map){
        this.map = map;
    }

    @Override
    public void draw(int color, Point2D point, double radius) {
        CircleOptions options = new CircleOptions()
                .fillColor(color)
                .radius(radius)
                .strokeWidth(0)
                .center(point.getLatLng());
        circle = map.addCircle(options);
    }
}
