package svp.com.dontmissplaces.model.gps;

import android.database.ContentObserver;
import android.os.Handler;

public class MyTracksLocationManager {

    private class GoogleSettingsObserver extends ContentObserver {

        public GoogleSettingsObserver(Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange) {
            isAllowed = GoogleLocationUtils.isAllowed(context);
        }
    }
}
