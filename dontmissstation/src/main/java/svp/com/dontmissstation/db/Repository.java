package svp.com.dontmissstation.db;

import android.content.Context;

import java.util.Collection;
import java.util.UUID;
import java.util.Vector;

import svp.app.map.model.Point2D;
import svp.com.dontmissstation.model.SubwayGraph;
import svp.com.dontmissstation.ui.model.SubwayStationView;
import svp.com.dontmissstation.ui.model.SubwayLineView;
import svp.com.dontmissstation.ui.model.SubwayView;


public class Repository {
    /**
    * нужна таблица связей между станциями
     * id | FromStationId | ToStationId | Length
    * */


    public final static String dbname = "dmsdb";
    private final SubwayView subway;
    private final Vector<SubwayStationView> stations;
    private SubwayGraph graph;

    public Repository(Context app) {
        stations = new Vector<>();
        Vector<Point2D> points  = new Vector<>();
        points.add(new Point2D(48.1741,16.3781));
        points.add(new Point2D(48.1797,16.376));
        points.add(new Point2D(48.1869,16.3737));
        points.add(new Point2D(48.1947,16.3699));
        points.add(new Point2D(48.2007,16.3689));

        points.add(new Point2D(48.2025,16.3614));
        points.add(new Point2D(48.2118,16.3777));
        points.add(new Point2D(48.2171,16.3713));
        points.add(new Point2D(48.2222,16.3676));
        points.add(new Point2D(48.2279,16.3639));

        for (int j =0 ; j < points.size() ; ++j){
            stations.add(new SubwayStationView(j,UUID.randomUUID().toString().substring(0,6),points.get(j)));
        }

        subway = new SubwayView(1,"Austria","Vienna");
        subway.addLine(create(0,UUID.randomUUID().toString().substring(0,1), "#009688",0,4));
        subway.addLine(create(1,UUID.randomUUID().toString().substring(0,1), "#4CAF50",5,10));

        for (SubwayLineView line: subway.getLines()) {
            line.addStation(stations.get(4));
        }
        try{
        graph = new SubwayGraph();
        graph.addNode(stations.get(0),stations.get(1),12);
        graph.addNode(stations.get(1),stations.get(2),10);
        graph.addNode(stations.get(2),stations.get(3),20);
        graph.addNode(stations.get(4),stations.get(5),10);
        graph.addNode(stations.get(5),stations.get(6),30);
        graph.addNode(stations.get(6),stations.get(7),10);
        graph.addNode(stations.get(7),stations.get(8),15);

        graph.getRoute(stations.get(0),stations.get(8));
        }catch (Exception ex){
            ex.getMessage();
        }

//        subway.addLine(create(2,UUID.randomUUID().toString().substring(0,1), "#CDDC39"));
//        subway.addLine(create(3,UUID.randomUUID().toString().substring(0,1), "#FF9800"));
//        subway.addLine(create(4,UUID.randomUUID().toString().substring(0,1), "#795548"));

    }

    private SubwayLineView create(int i, String substring, String s, int startInx, int count){
        SubwayLineView line = new SubwayLineView(i, substring, s, subway);
        for (int j =startInx; j < count ; ++j){
            line.addStation(stations.get(j));
        }

        return line;
    }

    public SubwayView getSubwayById(long id) {
        return subway;
    }

    public SubwayLineView getSubwayLineById(long id) {
        for (SubwayLineView line : subway.getLines()){
            if(line.getId() == id){
                return line;
            }
        }
        return null;
    }

    public SubwayStationView getSubwayStationById(long id) {
        for (SubwayLineView line : subway.getLines()){
            for (SubwayStationView station : line.getStations()) {
                if (station.getId() == id) {
                    return station;
                }
            }
        }
        return null;
    }

    public Collection<SubwayLineView> getSubwayLinesBySubwayId(long subwayId) {
        if(subway.getId() != subwayId){
            throw new InternalError();
        }
        return subway.getLines();
    }
    public Collection<SubwayStationView> getAllStationsOfSubway(long subwayId) {
        if(subway.getId() != subwayId){
            throw new InternalError();
        }
        Vector<SubwayStationView> stations = new Vector<>();
        for (SubwayLineView line : subway.getLines()){
            for (SubwayStationView station : line.getStations()) {
                stations.add(station);
            }
        }
        return stations;
    }

    public Vector<SubwayView> getSubways() {
        Vector<SubwayView> s = new Vector<>();
        s.add(subway);
        return s;
    }
}
