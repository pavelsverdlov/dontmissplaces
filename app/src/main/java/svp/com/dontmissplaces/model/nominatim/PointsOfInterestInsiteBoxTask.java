package svp.com.dontmissplaces.model.nominatim;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.bonuspack.location.NominatimPOIProvider;
import org.osmdroid.bonuspack.location.POI;
import org.osmdroid.bonuspack.utils.BonusPackHelper;
import org.osmdroid.util.BoundingBoxE6;
import org.osmdroid.util.GeoPoint;

import java.net.URLEncoder;
import java.util.ArrayList;

import svp.com.dontmissplaces.db.Place;

public abstract class PointsOfInterestInsiteBoxTask extends AsyncTask<ArrayList<PointsOfInterestInsiteBoxTask.InputData>, Void, Void> {
    //mServiceId

    protected abstract void processing(ArrayList<Place> poi, InputData data) ;

    //countrycodes=<countrycode>[,<countrycode>][,<countrycode>]...
    //http://nominatim.openstreetmap.org/search?format=jsonv2&q=[restaurant]&limit=200&bounded=1&viewbox=2.340642,48.871567,2.3445039999999997,48.867954&extratags=1&namedetails=1&addressdetails=1

    @Override
    protected Void doInBackground(ArrayList<InputData>... params) {
        ArrayList<InputData> datas = params[0];

        for (InputData data : datas) {
            BoundingBoxE6 box = data.box;
            StringBuffer urlString = new StringBuffer(NominatimPOIProvider.NOMINATIM_POI_SERVICE);
            urlString.append("search?")
                    .append("format=json").append("&q=[").append(URLEncoder.encode(data.keyword)).append("]")
                    .append("&limit=").append(data.maxResults)
                    .append("&bounded=1")
                    .append("&viewbox=")
                    .append(box.getLonWestE6()*1E-6).append(",")
                    .append(box.getLatNorthE6()*1E-6).append(",")
                    .append(box.getLonEastE6()*1E-6).append(",")
                    .append(box.getLatSouthE6()*1E-6);

            ArrayList<Place> poi = getThem(urlString.toString());
            processing(poi,data);
        }
        return null;
    }

    public ArrayList<Place> getThem(String url){
        Log.d(BonusPackHelper.LOG_TAG, "NominatimPOIProvider:get:"+url);
        String jString = BonusPackHelper.requestStringFromUrl(url, NominatimPOIProvider.NOMINATIM_POI_SERVICE);
        if (jString == null) {
            Log.e(BonusPackHelper.LOG_TAG, "NominatimPOIProvider: request failed.");
            return null;
        }
        try {
            JSONArray jPlaceIds = new JSONArray(jString);
            int n = jPlaceIds.length();
            ArrayList<Place> pois = new ArrayList<Place>(n);
            Bitmap thumbnail = null;
            for (int i=0; i<n; i++){
                JSONObject jPlace = jPlaceIds.getJSONObject(i);
                pois.add(new Place(jPlace));
            }
            return pois;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static class InputData {
        public final String keyword;
        public final int maxResults;
        public final BoundingBoxE6 box;

        public InputData(BoundingBoxE6 box, String keyword, int maxResults) {
            this.box = box;
            this.keyword = keyword;
            this.maxResults = maxResults;
        }
    }
}
