package svp.app.map.model;

import org.json.JSONException;
import org.json.JSONObject;

public class PlaceExtraTags {
    private final String cuisine = "cuisine";

    private final JSONObject extratags;
    //"extratags":{"website":"http:\/\/mure-restaurant.com\/","opening_hours":"Mo-Fr 08:30-17:00; Sa 10:00-17:00"}
    //"extratags":{"cuisine":"chinese"}

    public PlaceExtraTags(String extratags){
        JSONObject object;
        try {
            object = new JSONObject(extratags);
        } catch (Exception e) {
            e.printStackTrace();
            object = new JSONObject();
        }
        this.extratags = object;
    }

    public String getCuisine(){
        return extratags.optString(cuisine);
    }
}
