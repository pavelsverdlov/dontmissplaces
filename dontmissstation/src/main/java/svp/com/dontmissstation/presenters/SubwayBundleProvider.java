package svp.com.dontmissstation.presenters;

import android.content.Intent;
import android.os.Bundle;

import com.svp.infrastructure.mvpvs.bundle.BundleProvider;
import com.svp.infrastructure.mvpvs.bundle.IBundleProvider;

public class SubwayBundleProvider extends BundleProvider {
    private static final String STATION_ID_KEY = "STATION_ID_KEY";
    private static final String LINE_ID_KEY = "LINE_ID_KEY";
    private static final String SUBWAY_ID_KEY = "SUBWAY_ID_KEY";
    private static final long defaultId = -1;

    public static boolean isValid(long id){
        return id != defaultId;
    }

    public SubwayBundleProvider(Intent intent) {
        super(intent);
    }

    public SubwayBundleProvider() {
        this(new Bundle());
    }

    public SubwayBundleProvider(Bundle b) {
        super(b);
    }

    public IBundleProvider putSubwayId(long id) {
        bundle.putLong(SUBWAY_ID_KEY,id);
        return this;
    }

    public IBundleProvider putLineId(long id) {
        bundle.putLong(LINE_ID_KEY,id);
        return this;
    }

    public IBundleProvider putStationId(long id) {
        bundle.putLong(STATION_ID_KEY,id);
        return this;
    }

    public long getSubwayId() {
        return bundle.getLong(SUBWAY_ID_KEY,defaultId);
    }

    public long getSubwayLineId() {
        return bundle.getLong(LINE_ID_KEY,defaultId);
    }

    public long getSubwayStationId() {
        return bundle.getLong(STATION_ID_KEY,defaultId);
    }


}
