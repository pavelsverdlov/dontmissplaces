package svp.com.dontmissstation.model;

import android.support.v7.util.SortedList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Vector;

import svp.com.dontmissstation.ui.model.SubwayStationView;

public class SubwayGraph {
    private final class Curve{
        final int index;
        //TreeSet неподходи потому что если compareTo возвращает одинаковые значения
        //новый елемент в колекцию не добалсяться
        final TreeSet<Node> nodes;

        private Curve(int index, TreeSet<Node> nodes) {
            this.index = index;
            this.nodes = nodes;
        }
    }
    private final class Node implements Comparable<Node>{
        final int index;
        final float length;

        public Node(Integer index, float length) {
            this.index =index;
            this.length = length;
        }

        @Override
        public int compareTo(Node another) {
            return Float.compare(length,another.length);
        }
    }
    private HashMap<SubwayStationView,Integer> map;
    private Vector<SubwayStationView> stations;
    public Vector<Curve> stationCurves;


    public HashMap<Integer,TreeSet<Node>> route;

    private float curves[][];
    private int index;
    public SubwayGraph(){
        curves = new float[3][];
        stations = new Vector<>();
        map = new HashMap<>();
        route = new HashMap<>();
        stationCurves = new Vector<>();
        index = 0;
    }

    public void addNode(SubwayStationView from,SubwayStationView to,float length){
        Curve fromCurve = tryGetCurve(from);
        Curve toCurve = tryGetCurve(to);

        fromCurve.nodes.add(new Node(toCurve.index,length));
    }

    private Curve tryGetCurve(SubwayStationView station){
        int index;
        if(!map.containsKey(station)){
            stations.add(station);
            index = stations.size()-1;
            map.put(station,index);
            stationCurves.add(new Curve(index,new TreeSet<Node>()));
        }else{
            index = map.get(station);
        }
        return stationCurves.get(index);
    }

    public Vector<SubwayStationView> getRoute(SubwayStationView from,SubwayStationView to){
        ArrayList<Vector<SubwayStationView>> routes = new ArrayList<>();
        Vector<SubwayStationView> route = new Vector<>();

        HashMap<Integer,Curve> temp = new HashMap<>();
        for (Curve curve : stationCurves){
            temp.put(curve.index,curve);
        }

        int indexStart = map.get(from);
        int indexGo = indexStart;
        int indexFinish = map.get(to);
        while (true){
            if(indexGo == indexFinish){
                route.add(stations.get(indexGo));
                routes.add(route);
                //route = new Vector<>();
                indexGo = indexStart;
                break;
            }
            Curve curve = temp.get(indexGo);
            Node node = curve.nodes.pollFirst();
            if(node == null){
                //no ways any more
                //back prev station
                indexGo = map.get(route.lastElement());
                route.remove(route.size()-1);
                continue;
            }
            route.add(stations.get(indexGo));
            indexGo = node.index;
        }

        return route;
    }
}


























