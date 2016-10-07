package svp.com.dontmissstation.ui.activities;

import android.app.Activity;
import android.os.Handler;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.svp.infrastructure.ActivityPermissions;
import com.svp.infrastructure.mvpvs.commutate.ActivityOperationItem;
import com.svp.infrastructure.mvpvs.commutate.ICommutativeElement;
import com.svp.infrastructure.mvpvs.view.FragmentActivityView;

import org.osmdroid.util.BoundingBoxE6;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import svp.app.map.GoogleMapView;
import svp.app.map.IMapView;
import svp.app.map.OnMapClickListener;
import svp.app.map.android.GoogleApiMapPlaceProvider;
import svp.app.map.model.Point2D;
import svp.com.dontmissstation.R;
import svp.com.dontmissstation.presenters.PickOnMapPresenter;
import svp.com.dontmissstation.ui.model.POIView;

public class PickOnMapActivity extends FragmentActivityView<PickOnMapPresenter>
        implements ICommutativeElement,OnMapClickListener, View.OnClickListener {

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

        public void showOnMap(GoogleApiMapPlaceProvider.Place place) {
            view.mapView.addMarker(new POIView(place),-1);
            openYesNotBottomPanel();
        }
        private void openYesNotBottomPanel() {
            view.bottomLayout.setVisibility(View.VISIBLE);
        }
        private void closeYesNotBottomPanel() {
            view.bottomLayout.setVisibility(View.GONE);
        }
    }

    private IMapView mapView;
    @Bind(R.id.select_place_scrolling_act_content_view) LinearLayout bottomLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_on_map);

        ButterKnife.bind(this);

        findViewById(R.id.activity_pick_on_map_place_cancel).setOnClickListener(this);
        findViewById(R.id.activity_pick_on_map_place_ok).setOnClickListener(this);

        bottomLayout.setVisibility(View.GONE);
    }
    @Override
    protected void onStart() {
        super.onStart();

        ActivityPermissions  permissions = new ActivityPermissions(this);

        mapView = new GoogleMapView(this,R.id.activity_pick_on_map_fragment_map);

        permissions.checkPermissionExternalStorage();
        permissions.checkPermissionFineLocation();

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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activity_pick_on_map_place_cancel:
                //closeYesNotBottomPanel();
                getPresenter().clearSelectedPlace();
                break;
            case R.id.activity_pick_on_map_place_ok:
                getPresenter().storeSelectedPlace();
                getPresenter().openEditStationActivity();
                break;
        }
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
