package svp.com.dontmissplaces.model.nominatim;

import android.os.AsyncTask;

import org.osmdroid.bonuspack.location.NominatimPOIProvider;
import org.osmdroid.bonuspack.location.POI;
import org.osmdroid.util.BoundingBoxE6;

import java.util.ArrayList;

public abstract class PointsOfInterestInsiteBoxTask extends AsyncTask<ArrayList<PointsOfInterestInsiteBoxTask.InputData>, Void, Void> {
    //mServiceId

    protected abstract void processing(ArrayList<POI> poi, InputData data) ;

    @Override
    protected Void doInBackground(ArrayList<InputData>... params) {
        ArrayList<InputData> datas = params[0];
        for (InputData data : datas) {
            NominatimPOIProvider poiProvider = new NominatimPOIProvider(NominatimPOIProvider.NOMINATIM_POI_SERVICE);
            ArrayList<POI> poi = poiProvider.getPOIInside(data.box, data.keyword, data.maxResults);
            processing(poi,data);
        }
        return null;
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
