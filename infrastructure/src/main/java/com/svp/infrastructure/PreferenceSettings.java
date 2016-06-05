package com.svp.infrastructure;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PreferenceSettings {
    private final Activity activity;

    public PreferenceSettings(Activity activity) {
        this.activity = activity;
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
        return PreferenceManager.getDefaultSharedPreferences(activity);
    }
    private SharedPreferences.Editor setter() {
        return getter().edit();
    }
}
