package svp.com.dontmissstation.ui.activities.edit.subway.station;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.svp.infrastructure.common.ViewExtensions;

import java.util.Vector;

import svp.com.dontmissstation.R;
import svp.com.dontmissstation.ui.model.LineUIView;
import svp.com.dontmissstation.ui.model.SubwayLineView;
import svp.com.dontmissstation.ui.model.SubwayStationView;

public class ConnectStationsAdapter extends BaseAdapter {
    private OnClickListener listener;

    public interface OnClickListener{
        void onClick(SubwayStationView result);
    }

    private final LayoutInflater layoutInflater;
    private final Vector<SubwayStationView> stations;

    public ConnectStationsAdapter(LayoutInflater layoutInflater, Vector<SubwayStationView> stations) {
        this.layoutInflater = layoutInflater;
        this.stations = stations;
    }

    @Override
    public int getCount() {
        return stations.size();
    }

    @Override
    public Object getItem(int position) {
        return stations.get(position);
    }

    @Override
    public long getItemId(int position) {
        return stations.get(position).hashCode();
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (view == null) {
            view = layoutInflater.inflate(R.layout.activity_edit_station_connect_stations_template, parent, false);
        }
        SubwayStationView station = stations.get(position);
        ViewExtensions.<TextView>findViewById(view,R.id.activity_edit_subway_station_from_station_name_textview)
                .setText(station.getName());
        LinearLayout linesLayout = ViewExtensions.findViewById(view,R.id.activity_edit_subway_station_from_lines_layout);

        for (SubwayLineView line : station.getLines()) {
            LineUIView linev = new LineUIView(view.getContext(), line);
            linev.addTo(linesLayout);
        }
        //TODO: detect this is prev of next
        view.setTag(station);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick((SubwayStationView)v.getTag());
            }
        });

        return view;
    }

    public void setOnClickListener(OnClickListener listener) {
        this.listener = listener;
    }
}
