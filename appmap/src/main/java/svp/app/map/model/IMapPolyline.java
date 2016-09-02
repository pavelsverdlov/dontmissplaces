package svp.app.map.model;


import java.util.Vector;

public interface IMapPolyline {
    void draw(int color, float lineWidth, Vector<Point2D> points) ;
}
