package svp.com.dontmissplaces.ui.map;

import org.osmdroid.util.BoundingBoxE6;

import svp.com.dontmissplaces.model.Map.Point2D;

public interface OnMapClickListener {
    void onMapClick(Point2D point);
    void onZoom(int zoom, BoundingBoxE6 box);
    void onScroll(int zoom, BoundingBoxE6 box);
    void onMapLongClick(Point2D point);
}
