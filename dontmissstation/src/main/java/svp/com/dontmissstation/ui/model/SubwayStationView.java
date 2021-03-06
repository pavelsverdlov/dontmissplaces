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
    private final Vector<SubwayStationView> prevStations;
    private final Vector<SubwayStationView> nextStations;

    public SubwayStationView(long id, String name, Point2D location) {
        this.location = location;
        this.id = id;
        this.name = name;
        prevStations = new Vector<>();
        nextStations = new Vector<>();
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
    public void setName(String n){
        name = n;
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

    public void addPrev(SubwayStationView prevStation) {
        this.prevStations.add(prevStation);
    }
    public void addNext(SubwayStationView nextStation) {
        this.nextStations.add(nextStation);
    }

    public Collection<SubwayStationView> getPrevs(){
        return prevStations;
    }
    public Collection<SubwayStationView> getNexts(){
        return nextStations;
    }
    public void clearConnects() {
        nextStations.clear();
        prevStations.clear();
    }
}
