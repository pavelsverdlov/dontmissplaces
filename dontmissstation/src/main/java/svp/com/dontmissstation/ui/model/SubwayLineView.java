package svp.com.dontmissstation.ui.model;

import android.graphics.Color;

import java.util.Collection;
import java.util.Vector;

public class SubwayLineView extends SubwayElement{
    private final Vector<SubwayStationView> stations;
    private String name;
    public final String colorStr;

    public SubwayLineView(long id,String name, String colorStr){
        this.id = id;
        stations = new Vector<>();
        this.name = name;
        this.colorStr = colorStr;
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
    }

    public SubwayStationView getStartStation() {
        return stations.get(0);
    }

    public SubwayStationView getEndStation() {
        return stations.get(stations.size()-1);
    }


}
