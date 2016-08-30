package svp.com.dontmissstation.ui.model;

import java.util.UUID;

import svp.app.map.model.Point2D;

public class StationView extends SubwayElement{
    private String name;

    public StationView(String name) {
        this.name = name;
    }

    public Point2D getCoordinate(){
        return Point2D.empty();
    }
    public String getName(){
        return UUID.randomUUID().toString();
    }


}
