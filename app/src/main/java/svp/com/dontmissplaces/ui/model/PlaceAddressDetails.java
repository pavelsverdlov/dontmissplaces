package svp.com.dontmissplaces.ui.model;

/**
 * Created by Pasha on 6/12/2016.
 */
public class PlaceAddressDetails {
    private final String name;
    public PlaceAddressDetails(String title) {
        String[] array = title.split(",");
        name = array[0];
    }

    public String getName() {
        return name;
    }
}
