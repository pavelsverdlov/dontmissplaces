package svp.com.dontmissplaces.model.nominatim;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.bonuspack.location.NominatimPOIProvider;
import org.osmdroid.bonuspack.utils.BonusPackHelper;
import org.osmdroid.util.BoundingBoxE6;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashSet;

import svp.com.dontmissplaces.db.Place;

public abstract class PointsOfInterestInsiteBoxTask extends AsyncTask<ArrayList<PointsOfInterestInsiteBoxTask.InputData>, Void, Void> {
    private final String NAME = "PointsOfInterestInsiteBoxTask";
    private final String UTF_8 = java.nio.charset.StandardCharsets.UTF_8.toString();
    //mServiceId

    private HashSet<Integer> hash;

    protected abstract void processing(ArrayList<Place> poi, InputData data) ;

    //countrycodes=<countrycode>[,<countrycode>][,<countrycode>]... &addressdetails=1
    //http://nominatim.openstreetmap.org/search?format=jsonv2&q=[restaurant]&limit=200&bounded=1&viewbox=2.340642,48.871567,2.3445039999999997,48.867954&extratags=1&namedetails=1

    @Override
    protected Void doInBackground(ArrayList<InputData>... params) {
        ArrayList<InputData> datas = params[0];
        hash = new HashSet<>();
        for (InputData data : datas) {
            BoundingBoxE6 box = data.box;
            StringBuffer urlString = new StringBuffer(NominatimPOIProvider.NOMINATIM_POI_SERVICE);
//            try {
                urlString.append("search?")
                        .append("format=jsonv2").append("&q=[").append(URLEncoder.encode(data.keyword)).append("]")
                        .append("&limit=").append(data.maxResults)
                        .append("&extratags=1")
                        .append("&bounded=1")
                        .append("&viewbox=")
                        .append(box.getLonWestE6()*1E-6).append(",")
                        .append(box.getLatNorthE6()*1E-6).append(",")
                        .append(box.getLonEastE6()*1E-6).append(",")
                        .append(box.getLatSouthE6()*1E-6);
//            } catch (UnsupportedEncodingException e) {
//                Log.e(NAME, "", e);
//                continue;
//            }

            ArrayList<Place> poi = getThem(urlString.toString());

            int size = poi.size();
            Log.d(NAME,"poi count" + size);

            if(size == data.maxResults){
                Log.d(NAME,"Maybe need more request");
            }

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
//            Bitmap thumbnail = null;
            for (int i=0; i<n; i++){
                JSONObject jPlace = jPlaceIds.getJSONObject(i);
                int id = jPlace.getInt("place_id");
                if(hash.add(id)){
                    pois.add(new Place(jPlace));
                }
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
