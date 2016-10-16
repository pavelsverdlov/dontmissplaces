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
     *таблица связей между станциями
     * id | FromStationId | toStationId | length
     */
    public class StationRoute {
        public final long fromStationId;
        public final long toStationId;
        public final float length;

        private StationRoute(long fromStationId, long toStationId, float length) {
            this.fromStationId = fromStationId;
            this.toStationId = toStationId;
            this.length = length;
        }
    }

    private final Vector<StationRoute> stationRoutes = new Vector<>();

    /**
     * таблица
     * id | stationId | indexInLine | lineId
     */
    private class StationInLine {
        public final long stationId;
        public final int indexInLine;
        public final float lineId;

        private StationInLine(long stationId, int indexInLine, float lineId) {
            this.stationId = stationId;
            this.indexInLine = indexInLine;
            this.lineId = lineId;
        }
    }

    private final Vector<StationInLine> stationsInLine = new Vector<>();


    public final static String dbname = "dmsdb";
    private final SubwayView subway;
    private final Vector<SubwayStationView> stations;
    private SubwayGraph graph;


    public Repository(Context app) {
        int line1 = 0;
        int line2 = 1;

        stations = new Vector<>();
        Vector<Point2D> points = new Vector<>();
        points.add(new Point2D(48.1741, 16.3781));
        points.add(new Point2D(48.1797, 16.376));
        points.add(new Point2D(48.1869, 16.3737));
        points.add(new Point2D(48.1947, 16.3699));
        points.add(new Point2D(48.2007, 16.3689));

        points.add(new Point2D(48.2025, 16.3614));
        points.add(new Point2D(48.2118, 16.3777));
        points.add(new Point2D(48.2171, 16.3713));
        points.add(new Point2D(48.2222, 16.3676));
        points.add(new Point2D(48.2279, 16.3639));

        for (int j = 0; j < points.size(); ++j) {
            stations.add(new SubwayStationView(j, UUID.randomUUID().toString().substring(0, 10), points.get(j)));
        }
        //
        stationRoutes.add(new StationRoute(stations.get(0).getId(), stations.get(1).getId(), 12));
        stationRoutes.add(new StationRoute(stations.get(1).getId(), stations.get(2).getId(), 10));
        stationRoutes.add(new StationRoute(stations.get(2).getId(), stations.get(4).getId(), 10));
        stationRoutes.add(new StationRoute(stations.get(4).getId(), stations.get(3).getId(), 20));

        stationRoutes.add(new StationRoute(stations.get(5).getId(), stations.get(4).getId(), 30));
        stationRoutes.add(new StationRoute(stations.get(4).getId(), stations.get(6).getId(), 21));
        stationRoutes.add(new StationRoute(stations.get(6).getId(), stations.get(7).getId(), 10));
        stationRoutes.add(new StationRoute(stations.get(7).getId(), stations.get(8).getId(), 15));
        stationRoutes.add(new StationRoute(stations.get(8).getId(), stations.get(9).getId(), 15));
        //
        stationsInLine.add(new StationInLine(0, 0, line1));
        stationsInLine.add(new StationInLine(1, 1, line1));
        stationsInLine.add(new StationInLine(2, 2, line1));
        stationsInLine.add(new StationInLine(4, 3, line1));
        stationsInLine.add(new StationInLine(3, 4, line1));

        stationsInLine.add(new StationInLine(5, 0, line2));
        stationsInLine.add(new StationInLine(4, 1, line2));
        stationsInLine.add(new StationInLine(6, 2, line2));
        stationsInLine.add(new StationInLine(7, 3, line2));
        stationsInLine.add(new StationInLine(8, 4, line2));
        //
        subway = new SubwayView(1, "Austria", "Vienna");
        subway.addLine(create(line1, UUID.randomUUID().toString().substring(0, 1), "#009688"));//,0,4
        subway.addLine(create(line2, UUID.randomUUID().toString().substring(0, 1), "#4CAF50"));//,5,10
/*
        Vector<SubwayStationView> res = null;
        try {
            graph = new SubwayGraph();
            for (StationRoute route : stationRoutes) {
                graph.addNode(stations.get((int) route.fromStationId), stations.get((int) route.toStationId), route.length);
            }
            res = graph.getRoute(stations.get(1), stations.get(7));

        } catch (Exception ex) {
            ex.getMessage();
        }
        Vector<SubwayStationView> test = res;
*/
//        subway.addLine(create(2,UUID.randomUUID().toString().substring(0,1), "#CDDC39"));
//        subway.addLine(create(3,UUID.randomUUID().toString().substring(0,1), "#FF9800"));
//        subway.addLine(create(4,UUID.randomUUID().toString().substring(0,1), "#795548"));

    }

    private SubwayLineView create(int i, String substring, String s) {
        Vector<StationInLine> sts = new Vector<>();
        for (StationInLine st : stationsInLine) {
            if (st.lineId == i) {
                sts.add(st);
            }
        }
        SubwayLineView line = new SubwayLineView(i, substring, s, sts.size(), subway);
        for (StationInLine st : sts) {
            line.addStation(stations.get((int) st.stationId), st.indexInLine);
        }

        return line;
    }

    public SubwayView getSubwayById(long id) {
        return subway;
    }

    public SubwayLineView getSubwayLineById(long id) {
        for (SubwayLineView line : subway.getLines()) {
            if (line.getId() == id) {
                return line;
            }
        }
        return null;
    }

    public SubwayStationView getSubwayStationById(long id) {
        for (SubwayLineView line : subway.getLines()) {
            for (SubwayStationView station : line.getStations()) {
                if (station.getId() == id) {
                    station.clearConnects();
                    long sid = station.getId();
                    for (StationRoute route : stationRoutes){
                        if(route.fromStationId == sid ){
                            station.addNext(stations.get((int)route.toStationId));
                        }else if(route.toStationId == sid ){
                            station.addPrev(stations.get((int)route.fromStationId));
                        }
                    }
                    //TODO: add line references
                    return station;
                }
            }
        }
        return null;
    }

    public Collection<SubwayLineView> getSubwayLinesBySubwayId(long subwayId) {
        if (subway.getId() != subwayId) {
            throw new InternalError();
        }
        return subway.getLines();
    }

    public Collection<SubwayStationView> getAllStationsOfSubway(long subwayId) {
        if (subway.getId() != subwayId) {
            throw new InternalError();
        }
        Vector<SubwayStationView> stations = new Vector<>();
        for (SubwayLineView line : subway.getLines()) {
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

    public Vector<SubwayStationView> getSubwayStationsById(long subwayId) {
        if (subway.getId() != subwayId) {
            throw new InternalError();
        }
        return stations;
    }


    public void updateStationCoordinate(SubwayStationView station, Point2D point) {
        stations.get((int)station.getId()).updateCoordinate(point);
    }

    public void saveOrderedStations(SubwayLineView linetosave, Vector<SubwayStationView> stations) {
        Vector<StationRoute> temp = new Vector<>();
        for (int i = 0; i < stations.size(); i++) {
            SubwayStationView from = stations.get(i);
            SubwayStationView to = stations.get(i+1);
            temp.add(new StationRoute(from.getId(),to.getId(),20));
        }
        //stationRoutes
        SubwayLineView linetoupdate = null;
        for (SubwayLineView line : subway.getLines()){
            if(line.getId() == linetosave.getId()){
                line.clearStations();
                linetoupdate = line;
                break;
            }
        }
        if(linetoupdate == null){
            throw new InternalError();
        }
        for (int i = 0; i < stations.size(); i++) {
            SubwayStationView s = stations.get(i);
            linetoupdate.addStation(s,i);
        }
    }

    public Vector<StationRoute> getStationRoutes(long subwayId){
        return stationRoutes;
    }

}
