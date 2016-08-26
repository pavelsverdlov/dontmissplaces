package svp.com.dontmissstation.ui.model;

import java.util.Collection;
import java.util.Vector;

public class SubwayLineView {
    public Collection<StationView> getStations() {
        Vector<StationView> st = new Vector<>();

        st.add(new StationView());
        st.add(new StationView());
        st.add(new StationView());
        st.add(new StationView());
        st.add(new StationView());
        st.add(new StationView());

        return st;
    }
}
