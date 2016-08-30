package svp.com.dontmissstation.db;

import android.content.Context;

import java.util.UUID;

import svp.com.dontmissstation.ui.model.StationView;
import svp.com.dontmissstation.ui.model.SubwayLineView;
import svp.com.dontmissstation.ui.model.SubwayView;


public class Repository {
    public final static String dbname = "dmsdb";
    private final SubwayView subway;

    public Repository(Context app) {
        subway = new SubwayView();
        subway.addLine(create(0,UUID.randomUUID().toString().substring(0,1), "#009688"));
        subway.addLine(create(1,UUID.randomUUID().toString().substring(0,1), "#4CAF50"));
        subway.addLine(create(2,UUID.randomUUID().toString().substring(0,1), "#CDDC39"));
        subway.addLine(create(3,UUID.randomUUID().toString().substring(0,1), "#FF9800"));
        subway.addLine(create(4,UUID.randomUUID().toString().substring(0,1), "#795548"));

    }

    private SubwayLineView create(int i, String substring, String s){
        SubwayLineView line = new SubwayLineView(i, substring, s);

        line.addStation(new StationView(UUID.randomUUID().toString().substring(0,6)));
        line.addStation(new StationView(UUID.randomUUID().toString().substring(0,6)));
        line.addStation(new StationView(UUID.randomUUID().toString().substring(0,6)));
        line.addStation(new StationView(UUID.randomUUID().toString().substring(0,6)));
        line.addStation(new StationView(UUID.randomUUID().toString().substring(0,6)));
        line.addStation(new StationView(UUID.randomUUID().toString().substring(0,6)));
        line.addStation(new StationView(UUID.randomUUID().toString().substring(0,6)));

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
}
