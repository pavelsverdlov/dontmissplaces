package com.svp.infrastructure;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PreferenceSettings {
    private final Activity activity;

    public PreferenceSettings(Activity activity) {
        this.activity = activity;
        /*
        //http://stackoverflow.com/questions/8855069/android-sharedpreferences-best-practices
        SharedPreferences preferences = activity.getSharedPreferences("com.example.app", Context.MODE_PRIVATE);
        // get values from Map
        preferences.getBoolean("key", defaultValue)
        preferences.get..("key", defaultValue)

        // you can get all Map but be careful you must not modify the collection returned by this
        // method, or alter any of its contents.
        Map<String, ?> all = preferences.getAll();

        // get Editor object
        SharedPreferences.Editor editor = preferences.edit();
        */
    }

    public void putString(String key, String value){
        SharedPreferences.Editor editor = setter();
        editor.putString(key, value);
        editor.commit();
    }
    public <T> T get(String key, T def){
        SharedPreferences getter = getter();
        if(getter.contains(key)){
            return def;
        }
        return (T)getter.getAll().get(key);
    }

    private SharedPreferences getter(){
        return PreferenceManager.getDefaultSharedPreferences(activity,);
    }
    private SharedPreferences.Editor setter() {
        return getter().edit();
    }
}
