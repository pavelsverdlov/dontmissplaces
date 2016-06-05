package svp.com.dontmissplaces.ui;

import android.app.Activity;

import com.svp.infrastructure.PreferenceSettings;

import svp.com.dontmissplaces.R;
import svp.com.dontmissplaces.ui.map.MapViewTypes;

public class UserPreferenceSettings extends PreferenceSettings {
    public final String mapProviderKey;
    private MapViewTypes mapProvider;

    public UserPreferenceSettings(Activity activity) {
        super(activity);
        mapProviderKey = activity.getString(R.string.pref_settings_title_map_provider_key);
    }

    public MapViewTypes getMapProvider() {
        String val = get(mapProviderKey,null);
        return val == null ? MapViewTypes.Osmdroid : MapViewTypes.Google;
    }
}
