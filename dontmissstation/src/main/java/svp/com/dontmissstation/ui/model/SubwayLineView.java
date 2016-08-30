package svp.com.dontmissstation.ui.model;

import android.graphics.Color;

import java.util.Collection;
import java.util.Vector;

public class SubwayLineView extends SubwayElement{
    private final Vector<StationView> stations;
    private String name;
    private int color;

    public SubwayLineView(long id,String name, String colorStr){
        this.id = id;
        stations = new Vector<>();
        this.name = name;
        color = Color.parseColor(colorStr);
    }

    public String getName(){
        return name;
    }
    public int getColor(){ return color;  }

    public Collection<StationView> getStations() {
        return stations;
    }
    public void addStation(StationView station){
        stations.add(station);
    }

    public StationView getStartStation() {
        return stations.get(0);
    }

    public StationView getEndStation() {
        return stations.get(stations.size()-1);
    }


}
