package com.svp.infrastructure.mvpvs.bundle;

import android.os.Bundle;
import android.util.Log;

import com.svp.infrastructure.mvpvs.ICreator;
import java.util.HashMap;

public class BundleContainer {
    public interface IBundleCreator {
        IBundleProvider create(Bundle bundle);
    }
    private final static HashMap<Class<?>,IBundleCreator> creator;

    static {
        creator = new HashMap<>();
    }

    public static void register(Class<?> viewType,IBundleCreator cr){
        creator.put(viewType, cr);
    }

    public <P extends IBundleProvider> P get(Class<?> viewType,Bundle bundle){
        IBundleCreator p = creator.get(viewType);

        if(p == null){
            Log.w(this.getClass().getName(),"No any present creators for current bundle");
            return null;
        }
        IBundleProvider bp = p.create(bundle);

        return (P)bp;
    }
}
