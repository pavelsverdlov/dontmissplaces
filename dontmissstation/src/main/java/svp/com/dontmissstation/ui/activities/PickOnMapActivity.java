package svp.com.dontmissstation.ui.activities;

import android.app.Activity;
import android.os.Handler;
import android.os.Bundle;

import com.svp.infrastructure.mvpvs.commutate.ActivityOperationItem;
import com.svp.infrastructure.mvpvs.commutate.ICommutativeElement;
import com.svp.infrastructure.mvpvs.view.FragmentActivityView;

import org.osmdroid.util.BoundingBoxE6;

import svp.app.map.GoogleMapView;
import svp.app.map.IMapView;
import svp.app.map.OnMapClickListener;
import svp.app.map.model.Point2D;
import svp.com.dontmissstation.R;
import svp.com.dontmissstation.presenters.PickOnMapPresenter;

public class PickOnMapActivity extends FragmentActivityView<PickOnMapPresenter>
        implements ICommutativeElement,OnMapClickListener{

    @Override
    public ActivityOperationItem getOperation() {
        return ActivityOperationResult.PickOnMap;
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    public static class ViewState extends com.svp.infrastructure.mvpvs.viewstate.ViewState<PickOnMapActivity> {

        public ViewState(PickOnMapActivity view) {
            super(view);
        }

        @Override
        protected void restore() {

        }

        @Override
        public void saveState() {

        }

        @Override
        public Activity getActivity() {
            return view;
        }
    }

    private IMapView mapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_on_map);
    }
    @Override
    protected void onStart() {
        super.onStart();

        mapView = new GoogleMapView(this,R.id.activity_pick_on_map_fragment_map);
        mapView.setOnMapClickListener(this);
        mapView.onCreate(null);
        mapView.onStart();
        Handler uih = new Handler();
        uih.post(new Runnable() {
            @Override
            public void run() {
                mapView.moveTo(new Point2D(48.216667, 16.373333));
            }
        });
    }

    /**
     * Map events
     * */

    @Override
    public void onMapClick(Point2D point) {
        getPresenter().searchNearestStation(point);
    }

    @Override
    public void onZoom(int zoom, BoundingBoxE6 box) {

    }

    @Override
    public void onScroll(int zoom, BoundingBoxE6 box) {

    }

    @Override
    public void onMapLongClick(Point2D point) {

    }
}
