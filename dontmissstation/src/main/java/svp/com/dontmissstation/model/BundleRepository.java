package svp.com.dontmissstation.model;

import android.content.Intent;

import svp.com.dontmissstation.db.Repository;
import svp.com.dontmissstation.presenters.SubwayBundleProvider;
import svp.com.dontmissstation.ui.model.SubwayStationView;
import svp.com.dontmissstation.ui.model.SubwayView;

public class BundleRepository {
    public static SubwayStationView getStation(Intent intent, Repository repository){
        SubwayBundleProvider bundle = new SubwayBundleProvider(intent);
        long id = bundle.getSubwayStationId();
        if(!SubwayBundleProvider.isValid(id)){
            return null;
        }
        return repository.getSubwayStationById(id);
    }
    public static SubwayView getSubway(Intent data, Repository repository){
        SubwayBundleProvider bundle = new SubwayBundleProvider(data);
        long id = bundle.getSubwayId();
        if(!SubwayBundleProvider.isValid(id)){
            return null;
        }
        return repository.getSubwayById(id);
    }
}
