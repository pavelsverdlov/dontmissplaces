package com.svp.infrastructure;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

public class PreferenceSettings {
    private final static String KEY_PREFERENCES_VERSION = "key_preferences_version";
    private final static int PREFERENCES_VERSION = 1;

    private final SharedPreferences preferences;
    private final Activity activity;

    public PreferenceSettings(Activity activity) {
        this.activity = activity;
        preferences = activity.getSharedPreferences(activity.getPackageName(), Context.MODE_PRIVATE);
        checkPreferences(preferences);
    }

    public void putString(String key, String value){
        SharedPreferences.Editor editor = setter();
        editor.putString(key, value);
        editor.commit();
    }
    public void putInt(String key, int value){
        SharedPreferences.Editor editor = setter();
        editor.putInt(key, value);
        editor.commit();
    }
    public <T> T get(String key, T def){
        SharedPreferences pref = getter();
        if(!pref.contains(key)){
            return def;
        }
        Object val = pref.getAll().get(key);
        return val == null ? def : (T)val;
    }

    protected Preference getPreference(PreferenceFragment fragment, String key) {
        return fragment.findPreference(key);
    }

    private SharedPreferences getter(){
        return preferences;//PreferenceManager.getDefaultSharedPreferences(activity,);
    }
    private SharedPreferences.Editor setter() {
        return preferences.edit();
    }

    /**
     *  Migration
     */

    private synchronized void checkPreferences(SharedPreferences thePreferences) {
        final double oldVersion = thePreferences.getInt(KEY_PREFERENCES_VERSION, 0);

        if(oldVersion == 0){//first application start
            SharedPreferences.Editor editor = setter();
            editor.putInt(KEY_PREFERENCES_VERSION, PREFERENCES_VERSION);
            editor.apply();
            return;
        }

        if (oldVersion < PREFERENCES_VERSION) {
            final SharedPreferences.Editor edit = thePreferences.edit();
            //save same data from old preferences
            edit.clear();
            edit.putInt(KEY_PREFERENCES_VERSION, PREFERENCES_VERSION);
            //resave data to new preferences
            edit.apply();
        }
    }
}
