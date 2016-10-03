package svp.com.dontmissstation.model;

import android.content.Intent;

import svp.com.dontmissstation.db.Repository;
import svp.com.dontmissstation.presenters.SubwayBundleProvider;
import svp.com.dontmissstation.ui.model.SubwayStationView;

public class BundleRepository {
    public static SubwayStationView getStation(Intent intent, Repository repository){
        SubwayBundleProvider bundle = new SubwayBundleProvider(intent);
        long id = bundle.getSubwayStationId();
        return repository.getSubwayStationById(id);
    }
}
