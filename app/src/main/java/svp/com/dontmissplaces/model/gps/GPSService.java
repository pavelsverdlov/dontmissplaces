package svp.com.dontmissplaces.model.gps;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.*;
import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.LocationServices;
import com.svp.UnitConversions;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import svp.com.dontmissplaces.model.sensors.SensorManagerFactory;

public class GPSService extends Service {
    private static final String TAG = "GPSService";
    private static final int LOCATION_INTERVAL = 50000;
    private static final float LOCATION_DISTANCE = 10f;

    /**
     * The name of extra intent property to indicate whether we want to resume a
     * previously recorded track.
     */
    public static final String RESUME_TRACK_EXTRA_NAME = "com.google.android.apps.mytracks.RESUME_TRACK";

    public static final double PAUSE_LATITUDE = 100.0;
    public static final double RESUME_LATITUDE = 200.0;

    /**
     * Anything faster than that (in meters per second) will be considered moving.
     */
    public static final double MAX_NO_MOVEMENT_SPEED = 0.224;

    // private static final String TAG = TrackRecordingService.class.getSimpleName();

    // 1 second in milliseconds
    private static final long ONE_SECOND = (long) UnitConversions.S_TO_MS;

    // 1 minute in milliseconds
    private static final long ONE_MINUTE = (long) (UnitConversions.MIN_TO_S
            * UnitConversions.S_TO_MS);

    // The following variables are set in onCreate:
    private ExecutorService executorService;
    private Context context;
    //private MyTracksProviderUtils myTracksProviderUtils;
    private Handler handler;
    private MyTracksLocationManager myTracksLocationManager;
    private PendingIntent activityRecognitionPendingIntent;
    //OLD private ActivityRecognitionClient activityRecognitionClient;
    private GoogleApiClient activityRecognitionClient;
    private PeriodicTaskExecutor voiceExecutor;
    private PeriodicTaskExecutor splitExecutor;
    //    private SharedPreferences sharedPreferences;
    private long recordingTrackId;
    private boolean recordingTrackPaused;
    private LocationListenerPolicy locationListenerPolicy;
    private int recordingDistanceInterval;
    private int maxRecordingDistance;
    private int recordingGpsAccuracy;
    private int autoResumeTrackTimeout;
    private long currentRecordingInterval;
    private double weight;

    // The following variables are set when recording:
    private TripStatisticsUpdater trackTripStatisticsUpdater;
    private TripStatisticsUpdater markerTripStatisticsUpdater;
    private PowerManager.WakeLock wakeLock;
    private SensorManager sensorManager;
    private Location lastLocation;
    private boolean currentSegmentHasLocation;
    private boolean isIdle; // true if idle

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    private IBinder binder = new IGPSService.Stub() {
        @Override
        public Location getLastLocation() throws RemoteException {
            return null;
        }
    };

    private LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {
            if (myTracksLocationManager == null || executorService == null
                    || !myTracksLocationManager.isAllowed() || executorService.isShutdown()
                    || executorService.isTerminated()) {
                return;
            }
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    onLocationChangedAsync(location);
                }
            });
        }
    };

    private final GoogleApiClient.ConnectionCallbacks activityRecognitionCallbacks = new GoogleApiClient.ConnectionCallbacks() {
        @Override
        public void onConnected(Bundle bundle) {
            //OLD
            //activityRecognitionClient.requestActivityUpdates(ONE_MINUTE, activityRecognitionPendingIntent);
            PendingResult<Status> result = ActivityRecognition.ActivityRecognitionApi
                    .requestActivityUpdates(
                            activityRecognitionClient,
                            ONE_MINUTE,
                            activityRecognitionPendingIntent);
        }

        @Override
        public void onConnectionSuspended(int i) {
        }
    };
    private final GoogleApiClient.OnConnectionFailedListener activityRecognitionFailedListener =
            new GoogleApiClient.OnConnectionFailedListener() {
                @Override
                public void onConnectionFailed(ConnectionResult connectionResult) {
                }
            };
    private final Runnable registerLocationRunnable = new Runnable() {
        @Override
        public void run() {
            if (isRecording() && !isPaused()) {
                registerLocationListener();
            }
            handler.postDelayed(this, ONE_MINUTE);
        }
    };


    @Override
    public void onCreate() {
        super.onCreate();
        executorService = Executors.newSingleThreadExecutor();
        context = this;
//        myTracksProviderUtils = MyTracksProviderUtils.Factory.get(this);
        handler = new Handler();
        myTracksLocationManager = new MyTracksLocationManager(this, handler.getLooper(), true);
        activityRecognitionPendingIntent = PendingIntent.getService(context, 0,
                new Intent(context, ActivityRecognitionIntentService.class),
                PendingIntent.FLAG_UPDATE_CURRENT);
        //OLD
        // activityRecognitionClient = new ActivityRecognitionClient(context, activityRecognitionCallbacks, activityRecognitionFailedListener);
        activityRecognitionClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(activityRecognitionCallbacks)
                .addOnConnectionFailedListener(activityRecognitionFailedListener)
                .build();
        activityRecognitionClient.connect();

//        voiceExecutor = new PeriodicTaskExecutor(this, new AnnouncementPeriodicTaskFactory());
//        splitExecutor = new PeriodicTaskExecutor(this, new SplitPeriodicTaskFactory());

        handler.post(registerLocationRunnable);

    /*
     * Try to restart the previous recording track in case the service has been
     * restarted by the system, which can sometimes happen.
     */
//        Track track = myTracksProviderUtils.getTrack(recordingTrackId);
//        if (track != null) {
//            restartTrack(track);
//        } else {
//            if (isRecording()) {
//                Log.w(TAG, "track is null, but recordingTrackId not -1L. " + recordingTrackId);
//                updateRecordingState(PreferencesUtils.RECORDING_TRACK_ID_DEFAULT, true);
//            }
//            showNotification(false);
//        }
    }

    @Override
    public void onStart(Intent intent, int startId) {
        handleStartCommand(intent, startId);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        handleStartCommand(intent, startId);
        // We want this service to continue running until it is explicitly
        // stopped, so return sticky.
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy: ");
        if (sensorManager != null) {
            SensorManagerFactory.releaseSystemSensorManager();
            sensorManager = null;
        }

        // Reverse order from onCreate
        showNotification(false);

        handler.removeCallbacks(registerLocationRunnable);
        unregisterLocationListener();

        try {
            splitExecutor.shutdown();
        } finally {
            splitExecutor = null;
        }

        try {
            voiceExecutor.shutdown();
        } finally {
            voiceExecutor = null;
        }

        if (activityRecognitionClient.isConnected()) {
            //OLD activityRecognitionClient.removeActivityUpdates(activityRecognitionPendingIntent);
            ActivityRecognition.ActivityRecognitionApi
                    .removeActivityUpdates(activityRecognitionClient, activityRecognitionPendingIntent);
        }
        activityRecognitionClient.disconnect();
        activityRecognitionPendingIntent.cancel();

        myTracksLocationManager.close();
        myTracksLocationManager = null;
//        myTracksProviderUtils = null;

        binder.detachFromService();
        binder = null;

        // This should be the next to last operation
        releaseWakeLock();

        /*
         * Shutdown the executorService last to avoid sending events to a dead
         * executor.
         */
        executorService.shutdown();
        super.onDestroy();
    }


}
/*
public class GPSService extends Service {
    private static final String TAG = "GPSService";
    private static final int LOCATION_INTERVAL = 50000;
    private static final float LOCATION_DISTANCE = 10f;

    private LocationManager locationManager;
    private final HashMap<String, LocationListener> listeners;
    private final LocationFilter filter;

    private boolean isNetworkProvider;
    private boolean isGPSProvider;

    public GPSService() {
        filter = new LocationFilter();
        listeners = new HashMap<>();
        listeners.put(LocationManager.GPS_PROVIDER, new LocationListener(LocationManager.GPS_PROVIDER, filter));
        listeners.put(LocationManager.NETWORK_PROVIDER, new LocationListener(LocationManager.NETWORK_PROVIDER, filter));
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    private IBinder binder = new IGPSService.Stub() {
        @Override
        public Location getLastLocation() throws RemoteException {
            return filter.getPrevLocation();
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        if (locationManager == null) {
            locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
            locationManager.addGpsStatusListener(new GpsStatus.Listener() {
                @Override
                public void onGpsStatusChanged(int event) {
                    Log.d(TAG,"onGpsStatusChanged: " + event);
                }
            });
        }
        isNetworkProvider = requestLocationUpdates(LocationManager.NETWORK_PROVIDER);
        isGPSProvider = requestLocationUpdates(LocationManager.GPS_PROVIDER);

        Log.d(TAG, "onCreate: GPSProvider = " + isGPSProvider);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // We want this service to continue running until it is explicitly
        // stopped, so return sticky.
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy: ");
        super.onDestroy();
        locationManager.removeUpdates(listeners.get(LocationManager.GPS_PROVIDER));
        locationManager.removeUpdates(listeners.get(LocationManager.NETWORK_PROVIDER));
    }

    private boolean requestLocationUpdates(String provider){
        try {
            if(!locationManager.isProviderEnabled(provider)){
                Log.d(TAG, "requestLocationUpdates: " + provider + " disabled.");
                return false;
            }

            Location last = locationManager.getLastKnownLocation(provider);
            Log.d(TAG, "LastKnownLocation: " + (last == null ? "NULL" : last.toString()));
            filter.addLocation(last);

            locationManager.requestLocationUpdates(provider, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    listeners.get(provider));

            Log.d(TAG, provider + " true");

            return true;
        } catch (java.lang.SecurityException ex) {
            Log.i(TAG, "requestLocationUpdates: fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "requestLocationUpdates: gps provider does not exist " + ex.getMessage());
        }
        return false;
    }
    private boolean isNetworkConnected() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connMgr.getActiveNetworkInfo();

        return (info != null && info.isConnected());
    }
}
*/
