package svp.com.dontmissstation.db;

import android.content.Context;

import java.util.Collection;
import java.util.UUID;
import java.util.Vector;

import svp.app.map.model.Point2D;
import svp.com.dontmissstation.ui.model.SubwayStationView;
import svp.com.dontmissstation.ui.model.SubwayLineView;
import svp.com.dontmissstation.ui.model.SubwayView;


public class Repository {
    public final static String dbname = "dmsdb";
    private final SubwayView subway;

    public Repository(Context app) {
        subway = new SubwayView(1,"Austria","Vienna");
        subway.addLine(create(0,UUID.randomUUID().toString().substring(0,1), "#009688"));
        subway.addLine(create(1,UUID.randomUUID().toString().substring(0,1), "#4CAF50"));
        subway.addLine(create(2,UUID.randomUUID().toString().substring(0,1), "#CDDC39"));
        subway.addLine(create(3,UUID.randomUUID().toString().substring(0,1), "#FF9800"));
        subway.addLine(create(4,UUID.randomUUID().toString().substring(0,1), "#795548"));

    }

    private SubwayLineView create(int i, String substring, String s){
        SubwayLineView line = new SubwayLineView(i, substring, s);

        Vector<Point2D> points  = new Vector<>();
        points.add(new Point2D(48.1741,16.3781));
        points.add(new Point2D(48.1797,16.376));
        points.add(new Point2D(48.1869,16.3737));
        points.add(new Point2D(48.1947,16.3699));
        points.add(new Point2D(48.2007,16.3689));
        points.add(new Point2D(48.2025,16.3614));

        for (int j =0 ; j < 6 ; ++j){
            line.addStation(new SubwayStationView(j,UUID.randomUUID().toString().substring(0,6),points.get(j)));
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
        return subway.getLines();
    }

    public Vector<SubwayView> getSubways() {
        Vector<SubwayView> s = new Vector<>();
        s.add(subway);
        return s;
    }
}
