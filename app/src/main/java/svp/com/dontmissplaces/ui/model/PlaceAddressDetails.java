package svp.com.dontmissplaces.ui.model;

/**
 * Created by Pasha on 6/12/2016.
 */
public class PlaceAddressDetails {
    private final String name;
    private final String full;
    public PlaceAddressDetails(String title) {
        full = title;
        String[] array = title.split(",");
        name = array[0];
    }

    public String getFullAddress(){
        return full;
    }

    public String getName() {
        return name;
    }
}
