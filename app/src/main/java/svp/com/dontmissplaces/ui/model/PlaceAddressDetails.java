package svp.com.dontmissplaces.ui.model;

/*
* Toilette Tour Eiffel, 19, Avenue Gustave Eiffel, Gros-Caillou, 7e, Paris, Île-de-France, France métropolitaine, 75007, France
* */
public class PlaceAddressDetails {
    private final String full;
    private final String[] array;

    public PlaceAddressDetails(String title) {
        full = title;
        array = title.split(",");
    }

    public String getFullAddress(){
        return (getStreet() + " " +  array[1] +  ", " + array[array.length-1]).replace("  "," ").trim();
    }

    public String getName() {
        return array[0].trim();
    }

    public String getStreet() {
        return array[2].trim();
    }

}
