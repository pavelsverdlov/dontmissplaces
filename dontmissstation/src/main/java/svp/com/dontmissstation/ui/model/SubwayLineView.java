package svp.com.dontmissstation.ui.model;

import android.graphics.Color;
import android.graphics.drawable.Drawable;

import java.util.Collection;
import java.util.Vector;

public class SubwayLineView {

    public String getName(){
        return null;
    }
    public int getColor(){
        return Color.parseColor("#FFFFFF");
    }

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
