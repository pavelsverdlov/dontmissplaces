package svp.com.dontmissstation.ui.model;

import java.util.UUID;

import svp.app.map.model.Point2D;

public class SubwayStationView extends SubwayElement{
    private final Point2D location;
    private String name;

    public SubwayStationView(long id, String name, Point2D location) {
        this.location = location;
        this.id = id;
        this.name = name;
    }

    public Point2D getCoordinate(){
        return location;
    }
    public String getName(){
        return UUID.randomUUID().toString();
    }


    public long getOwnSubwayId() {
        return 0;
    }
}
