package svp.com.dontmissstation.ui.model;

import android.util.Log;

import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.UUID;
import java.util.Vector;

import svp.app.map.model.Point2D;

public class SubwayStationView extends SubwayElement{
    private Point2D location;
    private final HashMap<Long,SubwayLineView> lines;
    private String name;

    public SubwayStationView(long id, String name, Point2D location) {
        this.location = location;
        this.id = id;
        this.name = name;
        lines = new HashMap<>();
    }
    public void addOwnerLine(SubwayLineView line){
        lines.put(line.getId(),line);
    }

    public Point2D getCoordinate(){
        return location;
    }
    public String getName(){
        return name;
    }


    public long getOwnSubwayId() {
        return lines.values().iterator().next().getSubway().getId();
    }

    public Vector<SubwayLineView> getLines(){
        Vector<SubwayLineView> _lines = new Vector<>();
        for (SubwayLineView l: lines.values()){
            _lines.add(l);
        }
        return _lines;
    }

    public void updateCoordinate(Point2D point) {
        location = point;
    }
}
