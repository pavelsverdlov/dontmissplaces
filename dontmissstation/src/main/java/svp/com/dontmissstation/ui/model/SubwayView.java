package svp.com.dontmissstation.ui.model;

import java.util.Collection;
import java.util.Vector;

public class SubwayView {
    Vector<SubwayLineView> lines = new Vector<>();

    public String getCity(){
        return null;
    }
    public String getCountry(){
        return null;
    }

    public Collection<SubwayLineView> getLines() {
        return lines;
    }

    public void addLine(SubwayLineView line){
        lines.add(line);
    }
}
