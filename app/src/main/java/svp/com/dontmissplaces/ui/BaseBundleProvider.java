package svp.com.dontmissplaces.ui;

import android.content.Intent;
import android.os.Bundle;
import com.svp.infrastructure.mvpvs.bundle.BundleProvider;
import svp.com.dontmissplaces.db.Track;

public class BaseBundleProvider extends BundleProvider {
    private int requestCode;

    public BaseBundleProvider(Intent intent) {
        super(intent);
    }
    public BaseBundleProvider() {
        this(new Bundle());
    }
    public BaseBundleProvider(Bundle b) {
        super(b);
    }

    public Track getTrack(){
        return (Track) bundle.getSerializable(Track.KEY);
    }
    public BaseBundleProvider putTrack(Track track){
        bundle.putSerializable(Track.KEY, track);
        return this;
    }
}
