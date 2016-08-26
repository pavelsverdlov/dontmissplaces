package svp.com.dontmissstation.ui.model;

import java.util.Collection;
import java.util.Vector;

public class SubwayView {
    public Collection<SubwayLineView> getLines() {
        Vector<SubwayLineView> lines = new Vector<>();

        lines.add(new SubwayLineView());
        lines.add(new SubwayLineView());
        lines.add(new SubwayLineView());

        return lines;
    }
}
