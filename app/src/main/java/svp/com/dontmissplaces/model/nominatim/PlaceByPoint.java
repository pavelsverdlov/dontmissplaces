package svp.com.dontmissplaces.model.nominatim;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.bonuspack.location.NominatimPOIProvider;
import org.osmdroid.bonuspack.utils.BonusPackHelper;

import java.net.URLEncoder;
import java.util.ArrayList;

import svp.com.dontmissplaces.db.Place;
import svp.com.dontmissplaces.model.Map.Point2D;


public abstract class PlaceByPoint extends AsyncTask<Point2D, Void, Void> {
    //http://nominatim.openstreetmap.org/search?format=jsonv2&q=48.8738866,2.319392
    @Override
    protected Void doInBackground(Point2D... params) {
        Point2D p = params[0];
        StringBuffer urlString = new StringBuffer(NominatimPOIProvider.NOMINATIM_POI_SERVICE);
        urlString.append("search?")
                .append("format=jsonv2")
                .append("&q=")
                .append(p.latitude).append(",").append(p.longitude);

        String jString = BonusPackHelper.requestStringFromUrl(urlString.toString(), NominatimPOIProvider.NOMINATIM_POI_SERVICE);
        if (jString == null) {
            return null;
        }
        try {
            JSONArray jPlaceIds = new JSONArray(jString);
            int n = jPlaceIds.length();
            ArrayList<Place> pois = new ArrayList<Place>(n);
            for (int i=0; i<n; i++){
                JSONObject jPlace = jPlaceIds.getJSONObject(i);
                pois.add(new Place(jPlace));
            }
            processing(pois);
        } catch (JSONException e) {
            Log.e("PlaceByPoint","",e);
        }
        return null;
    }
    protected abstract void processing(ArrayList<Place> poi) ;

}
