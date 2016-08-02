package svp.com.dontmissplaces.ui;

import android.app.Activity;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import com.svp.infrastructure.PreferenceSettings;

import svp.com.dontmissplaces.R;
import svp.app.map.MapViewTypes;

public class UserPreferenceSettings extends PreferenceSettings {
    public final String mapProviderKey;
    private MapViewTypes mapProvider;

    public UserPreferenceSettings(Activity activity) {
        super(activity);
        mapProviderKey = activity.getString(R.string.pref_settings_title_map_provider_key);
    }

    public MapViewTypes getMapProvider() {
        return MapViewTypes.Osmdroid;
//        int val = get(mapProviderKey,-1);
//        MapViewTypes t = MapViewTypes.get(val);
//        return t == MapViewTypes.None ? MapViewTypes.Osmdroid : t;
    }

    public void setMapProvider(MapViewTypes val) {
        putInt(mapProviderKey,val.toInt());
    }

    public Preference getMapProviderPreference(PreferenceFragment fragment) {
        return getPreference(fragment, mapProviderKey);
    }



}
