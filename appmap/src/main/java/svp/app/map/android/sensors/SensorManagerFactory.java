package svp.app.map.android.sensors;

import android.content.Context;
import android.hardware.SensorManager;

public class SensorManagerFactory {
    private static SensorManager systemSensorManager = null;
    private static SensorManager tempSensorManager = null;

    private SensorManagerFactory() {}

    public static SensorManager getSystemSensorManager(Context context) {
        releaseTempSensorManager();
        releaseSystemSensorManager();
//        systemSensorManager = getSensorManager(context, true);
//        if (systemSensorManager != null) {
//            systemSensorManager.startSensor();
//        }
        return systemSensorManager;
    }

    public static void releaseSystemSensorManager() {
//        if (systemSensorManager != null) {
//            systemSensorManager.stopSensor();
//        }
        systemSensorManager = null;
    }

    public static SensorManager getTempSensorManager(Context context) {
        releaseTempSensorManager();
        if (systemSensorManager != null) {
            return null;
        }
//        tempSensorManager = getSensorManager(context, false);
//        if (tempSensorManager != null) {
//            tempSensorManager.startSensor();
//        }
        return tempSensorManager;
    }

    public static void releaseTempSensorManager() {
//        if (tempSensorManager != null) {
//            tempSensorManager.stopSensor();
//        }
        tempSensorManager = null;
    }

    private static SensorManager getSensorManager(Context context, boolean sendPageViews) {
//        String sensorType = PreferencesUtils.getString(
//                context, R.string.sensor_type_key, PreferencesUtils.SENSOR_TYPE_DEFAULT);
//
//        if (sensorType.equals(context.getString(R.string.sensor_type_value_ant))) {
//            if (sendPageViews) {
//                AnalyticsUtils.sendPageViews(context, AnalyticsUtils.SENSOR_ANT);
//            }
//            return new AntSensorManager(context);
//        } else if (sensorType.equals(context.getString(R.string.sensor_type_value_zephyr))) {
//            if (sendPageViews) {
//                AnalyticsUtils.sendPageViews(context, AnalyticsUtils.SENSOR_ZEPHYR);
//            }
//            return new ZephyrSensorManager(context);
//        } else if (sensorType.equals(context.getString(R.string.sensor_type_value_polar))) {
//            if (sendPageViews) {
//                AnalyticsUtils.sendPageViews(context, AnalyticsUtils.SENSOR_POLAR);
//            }
//            return new PolarSensorManager(context);
//        }
        return null;
    }
}
