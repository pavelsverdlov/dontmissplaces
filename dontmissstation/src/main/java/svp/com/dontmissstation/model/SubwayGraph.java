package svp.com.dontmissstation.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Vector;

import svp.com.dontmissstation.ui.model.SubwayStationView;

public class SubwayGraph {
    private HashMap<SubwayStationView,Integer> map;
    private Vector<SubwayStationView> stations;
    private float curves[][];
    private int index;
    public SubwayGraph(){
        curves = new float[3][];
        stations = new Vector<>();
        map = new HashMap<>();
        index = 0;
    }

    public void addNode(SubwayStationView from,SubwayStationView to,float length){
        if(!map.containsKey(from)){
            stations.add(from);
            map.put(from,stations.size());
        }
        if(!map.containsKey(to)){
            stations.add(to);
            map.put(to,stations.size());
        }
        curves[0][index] = map.get(from);
        curves[1][index] = map.get(from);
        curves[1][index] = length;
        index++;
    }
}
