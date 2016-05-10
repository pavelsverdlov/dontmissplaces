package com.svp.infrastructure.mvpvs.bundle;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import java.util.UUID;

public class BundleProvider implements IBundleProvider {
    public static Bundle getBundle(Bundle savedInstanceState, Activity activity){
        return savedInstanceState == null ?
                activity.getIntent().getExtras() :
                savedInstanceState;
    }
    public static IBundleProvider create(Bundle bundle){
        return new BundleProvider(bundle);
    }
    public static IBundleProvider create(){
        return new BundleProvider(new Bundle());
    }

    private static final String previousActionKey = UUID.randomUUID().toString();

    protected final Bundle bundle;
    protected BundleProvider(Intent intent) {
        this(intent.getExtras());
    }
    protected BundleProvider(Bundle b) {
        this.bundle = b == null ? new Bundle() : b;
    }
    public void putInto(Intent intent){
        intent.putExtras(bundle);
    }


    public CharSequence getPreviousActionText(){
        return bundle.getCharSequence(previousActionKey, null);
    }
    public IBundleProvider putActionText(CharSequence text){
        bundle.putCharSequence(previousActionKey,text);
        return this;
    }

}
