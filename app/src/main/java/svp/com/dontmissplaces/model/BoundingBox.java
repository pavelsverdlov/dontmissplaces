package svp.com.dontmissplaces.model;

import org.osmdroid.util.BoundingBoxE6;
import org.osmdroid.util.GeoPoint;

public class BoundingBox {
    public final BoundingBoxE6 box;
    public BoundingBox(BoundingBoxE6 box) {
        this.box  = box;
    }

    // does this BoundingBox r intersect s?
    public boolean intersects(BoundingBoxE6 s) {
        return (box.getLonEastE6() >= s.getLatNorthE6() && box.getLonWestE6() >= s.getLatSouthE6()
                && s.getLonEastE6() >= box.getLatNorthE6() && s.getLonWestE6() >= box.getLatSouthE6());
    }

//    public BoundingBoxE6 overlap(BoundingBoxE6 secondbox){
//
//
//        return new BoundingBoxE6();
//    }
}
