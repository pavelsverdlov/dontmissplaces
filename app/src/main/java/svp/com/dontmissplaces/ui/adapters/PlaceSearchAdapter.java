package svp.com.dontmissplaces.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.Vector;

import svp.com.dontmissplaces.R;
import svp.com.dontmissplaces.ui.activities.SearchPlacesActivity;

public class PlaceSearchAdapter extends BaseAdapter {
    private OnClickListener listener;

    public interface OnClickListener{
        void onClick(SearchPlacesActivity.PlaceSearchResult result);
    }

    private final LayoutInflater layoutInflater;
    private final Vector<SearchPlacesActivity.PlaceSearchResult> places;

    public PlaceSearchAdapter(LayoutInflater layoutInflater, Vector<SearchPlacesActivity.PlaceSearchResult> places) {
        this.layoutInflater = layoutInflater;
        this.places = places;
    }

    @Override
    public int getCount() {
        return places.size();
    }

    @Override
    public Object getItem(int position) {
        return places.get(position);
    }

    @Override
    public long getItemId(int position) {
        return places.get(position).hashCode();
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (view == null) {
            view = layoutInflater.inflate(R.layout.activity_history_tracks_item_template, parent, false);
        }
        SearchPlacesActivity.PlaceSearchResult place = places.get(position);
        place.initView(view);

        view.setTag(place);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick((SearchPlacesActivity.PlaceSearchResult)v.getTag());
            }
        });

        return view;
    }

    public void setOnClickListener(OnClickListener listener) {
        this.listener = listener;
    }
}
