package svp.com.dontmissplaces.ui;

import android.os.Bundle;
import com.svp.infrastructure.mvpvs.bundle.BundleProvider;
import svp.com.dontmissplaces.db.Track;

public class BaseBundleProvider extends BundleProvider {
    private int requestCode;

    public BaseBundleProvider() {
        this(null);
    }
    public BaseBundleProvider(Bundle b) {
        super(b);
    }

    public boolean requestFromMainMenu() {
        return requestCode == ActivityCommutator.ActivityOperationResult.MainMenu.toInt();
    }

    public Track getTrack(){
        return (Track) bundle.getSerializable(Track.KEY);
    }
    public BundleProvider putTrack(Track track){
        bundle.putSerializable(Track.KEY, track);
        return this;
    }
}
