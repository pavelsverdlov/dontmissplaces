package svp.com.dontmissplaces.model.nominatim;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.bonuspack.location.NominatimPOIProvider;
import org.osmdroid.bonuspack.location.POI;
import org.osmdroid.bonuspack.utils.BonusPackHelper;

import java.util.ArrayList;

import svp.com.dontmissplaces.db.Place;

public abstract class SearchByText extends AsyncTask<String, Void, Void> {
    private final String NAME = "SearchByText";

    protected abstract void processing(ArrayList<Place> poi) ;
    @Override
    protected Void doInBackground(String... params) {
        String text = params[0];
        StringBuffer urlString = new StringBuffer(NominatimPOIProvider.NOMINATIM_POI_SERVICE);
        urlString.append("search?")
                .append("format=jsonv2").append("&q=").append(text)
                .append("&limit=").append(100)
//                .append("&extratags=1")
//                .append("&bounded=1")
//                    .append("&viewbox=")
//                    .append(box.getLonWestE6() * 1E-6).append(",")
//                    .append(box.getLatNorthE6() * 1E-6).append(",")
//                    .append(box.getLonEastE6() * 1E-6).append(",")
//                    .append(box.getLatSouthE6() * 1E-6)
        ;


        ArrayList<Place> poi = getThem(urlString.toString());

        int size = poi.size();
        Log.d(NAME, "poi count" + size);

        if (size == 100) {
            Log.d(NAME, "Maybe need more request");
        }

        processing(poi);
        return null;
    }

    public ArrayList<Place> getThem(String url) {
        Log.d(BonusPackHelper.LOG_TAG, "NominatimPOIProvider:get:" + url);
        String jString = BonusPackHelper.requestStringFromUrl(url, NominatimPOIProvider.NOMINATIM_POI_SERVICE);
        if (jString == null) {
            Log.e(BonusPackHelper.LOG_TAG, "NominatimPOIProvider: request failed.");
            return null;
        }

        try {
            JSONArray jPlaceIds = new JSONArray(jString);
            int n = jPlaceIds.length();
            ArrayList<Place> pois = new ArrayList<Place>(n);
            for (int i = 0; i < n; i++) {
                JSONObject jPlace = jPlaceIds.getJSONObject(i);
                pois.add(new Place(jPlace));
            }
            return pois;
        } catch (JSONException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}