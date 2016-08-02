package svp.com.dontmissstation.ui;

import android.app.Activity;

import com.svp.infrastructure.PreferenceSettings;

import svp.app.map.MapViewTypes;
import svp.com.dontmissstation.R;

public class UserPreferenceSettings extends PreferenceSettings {
    private final String mapProviderKey;

    public UserPreferenceSettings(Activity activity) {
        super(activity);
        mapProviderKey = activity.getString(R.string.pref_settings_title_map_provider_key);
    }

    public MapViewTypes getMapProvider() {
        return MapViewTypes.Osmdroid;
    }

}
