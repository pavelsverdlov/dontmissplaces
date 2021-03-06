package svp.com.dontmissstation.ui.model;

import android.graphics.Color;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.LinkedList;

public class SubwayLineView extends SubwayElement{
    private final ArrayList<SubwayStationView> stations;
    private final Hashtable<Long,SubwayStationView> hashtable;
    private final SubwayView owner;
    private String name;
    public final String colorStr;

    public SubwayLineView(long id, String name, String colorStr, int stationCount, SubwayView owner){
        this.id = id;
        stations = new ArrayList<>(stationCount);
        hashtable = new Hashtable<>();
        this.name = name;
        this.colorStr = colorStr;
        this.owner = owner;
    }

    public String getName(){
        return name;
    }
    public int getColor(){ return Color.parseColor(colorStr);  }

    public Collection<SubwayStationView> getStations() {
        return stations;
    }
    public void addStation(SubwayStationView station, int indexInLine){
        stations.add(indexInLine, station);
        station.addOwnerLine(this);
//        if(stations.size() > 1) {
//            SubwayStationView prev = stations.get(stations.size() - 1);
//            station.addPrev(prev);
//            prev.addNext(station);
//        }
    }
    public void clearStations(){
        stations.clear();
    }

    public SubwayStationView getStartStation() {
        return stations.get(0);
    }

    public SubwayStationView getEndStation() {
        return stations.get(stations.size()-1);
    }

    public SubwayView getSubway() {
        return owner;
    }
}
