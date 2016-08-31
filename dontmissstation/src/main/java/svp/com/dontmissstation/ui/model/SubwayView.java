package svp.com.dontmissstation.ui.model;

import java.util.Collection;
import java.util.Vector;

public class SubwayView {
    private final String country;
    private final String city;
    Vector<SubwayLineView> lines = new Vector<>();

    public SubwayView(String country, String city) {
        this.country = country;
        this.city = city;
    }

    public String getCity(){
        return city;
    }
    public String getCountry(){
        return country;
    }

    public Collection<SubwayLineView> getLines() {
        return lines;
    }

    public void addLine(SubwayLineView line){
        lines.add(line);
    }
}
