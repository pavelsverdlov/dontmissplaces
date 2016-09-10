package svp.app.map.model;

public class Place {
    public final double longitude;
    public final double latitude;
    public String city;
    public String country;
    public String title;
    public String address;
    public String description;

    public Place(double longitude, double latitude) {

        this.longitude = longitude;
        this.latitude = latitude;
    }
}
