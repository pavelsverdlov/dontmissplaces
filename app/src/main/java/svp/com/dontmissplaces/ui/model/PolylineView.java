package svp.com.dontmissplaces.ui.model;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import svp.com.dontmissplaces.db.Waypoint;

public class PolylineView {
    public final int color;
    public final float lineWidth;
    private final List<Waypoint> points;
    private final List<PolylineView> lines;

    public PolylineView(int color, float lineWidth, Waypoint... points) {
        this.color = color;
        this.lineWidth = lineWidth;
        this.points = new ArrayList();
        lines = new ArrayList();
        for (Waypoint w : points){
            this.points.add(w);
        }
    }

    public PolylineView(int blue, int lineWidth, Iterable<Waypoint> objects) {

    }

    public void addPoint(Waypoint point){
        points.add(point);
    }

    public void clear() {
        points.clear();
    }

    public int size() {
        return points.size();
    }

    public void add(PolylineView polyline) {
        lines.add(polyline);
    }

    public void addPolylineOptions(GoogleMap map){
        //Arrays.asList(points)
        Vector<LatLng> ll = new Vector<>();
        for (Waypoint p : points){
            ll.add(p.getLatLng());
        }
        map.addPolyline(new PolylineOptions().width(lineWidth).color(color).geodesic(true).addAll(ll));
        for (PolylineView p : lines){
            p.addPolylineOptions(map);
        }
    }
}
