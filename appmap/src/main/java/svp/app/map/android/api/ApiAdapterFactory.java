package svp.app.map.android.api;

import android.annotation.TargetApi;

public class ApiAdapterFactory {
    private static ApiAdapter apiAdapter;

    /**
     * Gets the {@link ApiAdapter} for the current device.
     */
    public static ApiAdapter getApiAdapter() {
        if (apiAdapter == null) {
            apiAdapter = new Api19Adapter();
            /*
            if (Build.VERSION.SDK_INT >= 19) {
                apiAdapter = new Api19Adapter();
            } else if (Build.VERSION.SDK_INT >= 17) {
                apiAdapter = new Api17Adapter();
            } else if (Build.VERSION.SDK_INT >= 16) {
                apiAdapter = new Api16Adapter();
            } else if (Build.VERSION.SDK_INT >= 14) {
                apiAdapter = new Api14Adapter();
            } else if (Build.VERSION.SDK_INT >= 11) {
                apiAdapter = new Api11Adapter();
            } else if (Build.VERSION.SDK_INT >= 10) {
                apiAdapter = new Api10Adapter();
            } else if (Build.VERSION.SDK_INT >= 9) {
                apiAdapter = new Api9Adapter();
            } else {
                apiAdapter = new Api8Adapter();
            }
            */
        }
        return apiAdapter;
    }
    @TargetApi(19)
    public static class Api19Adapter implements ApiAdapter /*Api17Adapter */{

        public boolean hasLocationMode() {
            return true;
        }
    }
}
