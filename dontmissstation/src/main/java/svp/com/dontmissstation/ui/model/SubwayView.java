package svp.com.dontmissstation.ui.model;

import java.util.Collection;
import java.util.Vector;

public class SubwayView extends SubwayElement{
    private final String country;
    private final String city;
    Vector<SubwayLineView> lines = new Vector<>();

    public SubwayView(long id, String country, String city) {
        this.country = country;
        this.city = city;
        this.id = id;
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
