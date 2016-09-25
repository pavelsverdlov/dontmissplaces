package svp.com.dontmissstation.ui.model;

import android.graphics.Color;

import java.util.Collection;
import java.util.Hashtable;
import java.util.LinkedList;

public class SubwayLineView extends SubwayElement{
    private final LinkedList<SubwayStationView> stations;
    private final Hashtable<Long,SubwayStationView> hashtable;
    private final SubwayView owner;
    private String name;
    public final String colorStr;

    public SubwayLineView(long id, String name, String colorStr,SubwayView owner){
        this.id = id;
        stations = new LinkedList<>();
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
    public void addStation(SubwayStationView station){
        stations.add(station);
       station.addOwnerLine(this);
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
