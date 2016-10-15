package svp.app.map.android;


import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.Vector;

import svp.app.map.model.IMapPolyline;
import svp.app.map.model.Point2D;

public class GMapPolyline implements IMapPolyline {
    private final GoogleMap map;
    private Polyline polyline;
    public GMapPolyline(GoogleMap map){
        this.map = map;
    }

    @Override
    public void draw(int color, float lineWidth, Vector<Point2D> points) {
        Vector<LatLng> lanlogs = new Vector<>();

        for (Point2D p : points){
            lanlogs.add(p.getLatLng());
        }

        PolylineOptions pl = new PolylineOptions().width(lineWidth).color(color).geodesic(true)
                .addAll(lanlogs);
        polyline = map.addPolyline(pl);
    }
}
