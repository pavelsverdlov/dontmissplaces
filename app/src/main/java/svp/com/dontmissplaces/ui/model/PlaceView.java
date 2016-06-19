package svp.com.dontmissplaces.ui.model;

import android.database.Cursor;
import android.view.View;
import android.widget.TextView;

import com.svp.infrastructure.common.PermissionUtils;
import com.svp.infrastructure.common.ViewExtensions;
import com.svp.infrastructure.common.view.ICursorParcelable;

import org.osmdroid.util.GeoPoint;

import svp.com.dontmissplaces.R;
import svp.com.dontmissplaces.db.Place;

public class PlaceView implements ICursorParcelable, IPOIView {
    protected Place place;
    private TextView title;
    private PlaceAddressDetails addressDetails;
    private PlaceExtraTags extraTags;

    public PlaceView(Place place) {
        this.place = place;
        addressDetails = new PlaceAddressDetails(place.title);
        extraTags = new PlaceExtraTags(place.extratags);
    }

    public String getName(){
        return addressDetails.getName();
    }
    public String getType() {
        return place.nominatimType;
    }
    public GeoPoint getGeoPoint(){
        return new GeoPoint(place.latitude, place.longitude);
    }
    public String getAddress(){
        return addressDetails.getFullAddress();
    }

    public PlaceExtraTags getExtraTags(){ return extraTags; }

    public PlaceView() {}
    @Override
    public void parse(Cursor cursor) {
        title.setText(Place.getTitle(cursor));
    }

    @Override
    public void initView(View view) {
        title = ViewExtensions.findViewById(view, R.id.history_tracks_item_text);
    }


}
