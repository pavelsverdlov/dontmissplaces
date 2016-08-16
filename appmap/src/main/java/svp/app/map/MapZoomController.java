package svp.app.map;

import android.app.Activity;
import android.support.design.widget.FloatingActionButton;
import android.view.View;

import com.svp.infrastructure.common.ViewExtensions;

/**
 * Created by Pasha on 8/17/2016.
 */
public class MapZoomController implements View.OnClickListener{
    public interface IMapZoom{
        void setZoom(float val);
        float getZoom();
    }
    private final FloatingActionButton fabZoomPlus;
    private final FloatingActionButton fabZoomMinus;
    private final Activity activity;
    private final IMapZoom mapView;

    public MapZoomController(Activity activity, IMapZoom mapView){
        this.activity = activity;
        this.mapView = mapView;
        fabZoomPlus = ViewExtensions.findViewById(activity,R.id.map_zoom_plus_fab);
        fabZoomMinus = ViewExtensions.findViewById(activity, R.id.map_zoom_minus_fab);

        fabZoomPlus.setOnClickListener(this);
        fabZoomMinus.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id ==  R.id.map_zoom_plus_fab){
            mapView.setZoom(mapView.getZoom() + 1);
        }else if(id == R.id.map_zoom_minus_fab){
            mapView.setZoom(mapView.getZoom() - 1);
        }
    }
}
