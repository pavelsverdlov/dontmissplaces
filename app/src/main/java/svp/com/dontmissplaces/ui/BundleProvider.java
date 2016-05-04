package svp.com.dontmissplaces.ui;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import java.util.UUID;

import svp.com.dontmissplaces.db.Track;

public class BundleProvider {
    public static BundleProvider create(Bundle savedInstanceState, Activity activity){
        return savedInstanceState == null ?
            new BundleProvider(activity.getIntent().getExtras()) :
            new BundleProvider(savedInstanceState);
    }

    private static final String previousActionKey = UUID.randomUUID().toString();
    private final int requestCode;
    public final Bundle bundle;
    public BundleProvider() {
        this(-1,null);
    }
    public BundleProvider(Bundle b) {
        this(-1,b);
    }
    public BundleProvider(int requestCode, Bundle b) {
        this.requestCode = requestCode;
        this.bundle = b == null ? new Bundle() : b;
    }

    public void putInto(Intent intent){
        intent.putExtras(bundle);
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

    public CharSequence getPreviousAction(){
        return bundle.getCharSequence(previousActionKey);
    }
    public void putAction(CharSequence text){
        bundle.putCharSequence(previousActionKey,text);
    }
}
