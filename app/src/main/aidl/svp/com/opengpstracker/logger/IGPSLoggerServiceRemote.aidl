// IGPSLoggerServiceRemote.aidl
package svp.com.opengpstracker.logger;

// Declare any non-default types here with import statements
import android.net.Uri;
import android.location.Location;

interface IGPSLoggerServiceRemote {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
//    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
//            double aDouble, String aString);
    int loggingState();
    long startLogging();
    void pauseLogging();
    long resumeLogging();
    void stopLogging();
    Uri storeMediaUri(in Uri mediaUri);
    boolean isMediaPrepared();
    void storeDerivedDataSource(in String sourceName);
    Location getLastWaypoint();
    float getTrackedDistance();
}
