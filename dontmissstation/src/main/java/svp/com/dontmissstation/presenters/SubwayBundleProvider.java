package svp.com.dontmissstation.presenters;

import android.content.Intent;
import android.os.Bundle;

import com.svp.infrastructure.mvpvs.bundle.BundleProvider;
import com.svp.infrastructure.mvpvs.bundle.IBundleProvider;

public class SubwayBundleProvider extends BundleProvider {
    private static final String STATION_ID_KEY = "STATION_ID_KEY";
    private static final String LINE_ID_KEY = "LINE_ID_KEY";
    private static final String SUBWAY_ID_KEY = "SUBWAY_ID_KEY";

    public SubwayBundleProvider(Intent intent) {
        super(intent);
    }

    public SubwayBundleProvider() {
        this(new Bundle());
    }

    public SubwayBundleProvider(Bundle b) {
        super(b);
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
        return bundle.getLong(SUBWAY_ID_KEY);
    }

    public long getSubwayLineId() {
        return bundle.getLong(LINE_ID_KEY);
    }
}
