package svp.com.dontmissplaces.model.Map.google;

import svp.com.dontmissplaces.db.Place;
import svp.com.dontmissplaces.ui.model.IPlaceAddressDetails;

/**
 * Created by Pasha on 7/14/2016.
 */
public class GPlaceAddressDetails implements IPlaceAddressDetails {
    private final Place place;

    public GPlaceAddressDetails(Place place) {
        this.place = place;
    }

    @Override
    public String getFullAddress() {
        return place.description;
    }

    @Override
    public String getName() {
        return place.title;
    }

    @Override
    public String getStreet() {
        return place.address;
    }
}
