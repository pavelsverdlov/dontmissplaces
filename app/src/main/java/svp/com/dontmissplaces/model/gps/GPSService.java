package svp.com.dontmissplaces.model.gps;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.*;
import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.LocationServices;
import com.svp.UnitConversions;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import svp.com.dontmissplaces.R;
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
//    private PeriodicTaskExecutor voiceExecutor;
//    private PeriodicTaskExecutor splitExecutor;
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
//    private TripStatisticsUpdater trackTripStatisticsUpdater;
//    private TripStatisticsUpdater markerTripStatisticsUpdater;
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

    private ServiceBinder binder = new ServiceBinder(this);
//    private IBinder binder = new IGPSService.Stub() {
//        @Override
//        public Location getLastLocation() throws RemoteException {
//            return null;
//        }
//    };

    private com.google.android.gms.location.LocationListener locationListener = new com.google.android.gms.location.LocationListener() {
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
        Log.d(TAG,"GPS service create");
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
//
//        try {
//            splitExecutor.shutdown();
//        } finally {
//            splitExecutor = null;
//        }
//
//        try {
//            voiceExecutor.shutdown();
//        } finally {
//            voiceExecutor = null;
//        }

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

    public boolean isRecording(){
        return true;
    }
    public boolean isPaused() {
        return recordingTrackPaused;
    }
    protected void startForegroundService(PendingIntent pendingIntent, int messageId) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this).setContentIntent(
                pendingIntent).setContentText(getString(messageId))
                .setContentTitle("my_tracks_app_name").setOngoing(true)
                .setSmallIcon(R.drawable.common_signin_btn_icon_disabled_light)
                .setWhen(System.currentTimeMillis());
        startForeground(1, builder.build());
    }
    protected void stopForegroundService() {
        stopForeground(true);
    }
    private void handleStartCommand(Intent intent, int startId) {
        // Check if the service is called to resume track (from phone reboot)
        if (intent != null && intent.getBooleanExtra(RESUME_TRACK_EXTRA_NAME, false)) {
            if (!shouldResumeTrack()) {
                Log.i(TAG, "Stop resume track.");
//                updateRecordingState(PreferencesUtils.RECORDING_TRACK_ID_DEFAULT, true);
                stopSelfResult(startId);
                return;
            }
        }
    }
    private boolean shouldResumeTrack() {
        /*
        Track track = myTracksProviderUtils.getTrack(recordingTrackId);

        if (track == null) {
            Log.d(TAG, "Not resuming. Track is null.");
            return false;
        }
        int retries = PreferencesUtils.getInt(this, R.string.auto_resume_track_current_retry_key,
                PreferencesUtils.AUTO_RESUME_TRACK_CURRENT_RETRY_DEFAULT);
        if (retries >= MAX_AUTO_RESUME_TRACK_RETRY_ATTEMPTS) {
            Log.d(TAG, "Not resuming. Exceeded maximum retry attempts.");
            return false;
        }
        PreferencesUtils.setInt(this, R.string.auto_resume_track_current_retry_key, retries + 1);

        if (autoResumeTrackTimeout == PreferencesUtils.AUTO_RESUME_TRACK_TIMEOUT_NEVER) {
            Log.d(TAG, "Not resuming. Auto-resume track timeout set to never.");
            return false;
        } else if (autoResumeTrackTimeout == PreferencesUtils.AUTO_RESUME_TRACK_TIMEOUT_ALWAYS) {
            Log.d(TAG, "Resuming. Auto-resume track timeout set to always.");
            return true;
        }

        if (track.getTripStatistics() == null) {
            Log.d(TAG, "Not resuming. No trip statistics.");
            return false;
        }
        long stopTime = track.getTripStatistics().getStopTime();
        return stopTime > 0
                && (System.currentTimeMillis() - stopTime) <= autoResumeTrackTimeout * ONE_MINUTE;
                */
        Log.d(TAG, "shouldResumeTrack");
        return false;
    }
    private void resumeCurrentTrack() {
            Location resume = new Location(LocationManager.GPS_PROVIDER);
            resume.setLongitude(0);
            resume.setLatitude(RESUME_LATITUDE);
            resume.setTime(System.currentTimeMillis());
        startRecording(false);
    }
    private void startRecording(boolean trackStarted) {

        // Update instance variables
        sensorManager = SensorManagerFactory.getSystemSensorManager(this);
        lastLocation = null;
        currentSegmentHasLocation = false;
        isIdle = false;

        startGps();
        sendTrackBroadcast(trackStarted ? "track_started_broadcast_action"
                : "track_resumed_broadcast_action", recordingTrackId);

        // Restore periodic tasks
//        voiceExecutor.restore();
//        splitExecutor.restore();
    }
    private void startGps() {
        wakeLock = SystemUtils.acquireWakeLock(this, wakeLock);
        registerLocationListener();
        showNotification(true);
    }
    private void endRecording(boolean trackStopped, long trackId) {

        // Shutdown periodic tasks
//        voiceExecutor.shutdown();
//        splitExecutor.shutdown();

        // Update instance variables
        if (sensorManager != null) {
            SensorManagerFactory.releaseSystemSensorManager();
            sensorManager = null;
        }
        lastLocation = null;

        sendTrackBroadcast(trackStopped ? "track_stopped_broadcast_action"
                : "track_paused_broadcast_action", trackId);
        stopGps(trackStopped);
    }
    private void stopGps(boolean stop) {
        unregisterLocationListener();
        showNotification(false);
        releaseWakeLock();
        if (stop) {
            stopSelf();
        }
    }

    private void sendTrackBroadcast(String actionId, long trackId) {
        Intent intent = new Intent().setAction(actionId)
                .putExtra("track_id_broadcast_extra", trackId);
        sendBroadcast(intent, "permission_notification_value");
//        if (PreferencesUtils.getBoolean(
//                this, R.string.allow_access_key, PreferencesUtils.ALLOW_ACCESS_DEFAULT)) {
            sendBroadcast(intent, "broadcast_notifications_permission");
//        }
    }
    private void onLocationChangedAsync(Location location) {
        try {
            if (!isRecording() || isPaused()) {
                Log.w(TAG, "Ignore onLocationChangedAsync. Not recording or paused.");
                return;
            }

//            Track track = myTracksProviderUtils.getTrack(recordingTrackId);
//            if (track == null) {
//                Log.w(TAG, "Ignore onLocationChangedAsync. No track.");
//                return;
//            }

            if (!LocationUtils.isValidLocation(location)) {
                Log.w(TAG, "Ignore onLocationChangedAsync. location is invalid.");
                return;
            }

            if (!location.hasAccuracy() || location.getAccuracy() >= recordingGpsAccuracy) {
                Log.d(TAG, "Ignore onLocationChangedAsync. Poor accuracy.");
                return;
            }

            // Fix for phones that do not set the time field
            if (location.getTime() == 0L) {
                location.setTime(System.currentTimeMillis());
            }

            Location lastValidTrackPoint = lastLocation;//getLastValidTrackPointInCurrentSegment(track.getId());
            long idleTime = 0L;
            if (lastValidTrackPoint != null && location.getTime() > lastValidTrackPoint.getTime()) {
                idleTime = location.getTime() - lastValidTrackPoint.getTime();
            }
            locationListenerPolicy.updateIdleTime(idleTime);
            if (currentRecordingInterval != locationListenerPolicy.getDesiredPollingInterval()) {
                registerLocationListener();
            }

//            SensorDataSet sensorDataSet = getSensorDataSet();
//            if (sensorDataSet != null) {
//                location = new MyTracksLocation(location, sensorDataSet);
//            }

            // Always insert the first segment location
            if (!currentSegmentHasLocation) {
//                insertLocation(track, location, null);
                currentSegmentHasLocation = true;
                lastLocation = location;
                return;
            }

            if (!LocationUtils.isValidLocation(lastValidTrackPoint)) {
                /*
                 * Should not happen. The current segment should have a location. Just
                 * insert the current location.
                 */
//                insertLocation(track, location, null);
                lastLocation = location;
                return;
            }

            double distanceToLastTrackLocation = location.distanceTo(lastValidTrackPoint);
            if (distanceToLastTrackLocation > maxRecordingDistance) {
//                insertLocation(track, lastLocation, lastValidTrackPoint);

                Location pause = new Location(LocationManager.GPS_PROVIDER);
                pause.setLongitude(0);
                pause.setLatitude(PAUSE_LATITUDE);
                pause.setTime(lastLocation.getTime());
//                insertLocation(track, pause, null);
//
//                insertLocation(track, location, null);
                isIdle = false;
            } else if (/*sensorDataSet != null || */distanceToLastTrackLocation >= recordingDistanceInterval) {
//                insertLocation(track, lastLocation, lastValidTrackPoint);
//                insertLocation(track, location, null);
                isIdle = false;
            } else if (!isIdle && location.hasSpeed() && location.getSpeed() < MAX_NO_MOVEMENT_SPEED) {
//                insertLocation(track, lastLocation, lastValidTrackPoint);
//                insertLocation(track, location, null);
                isIdle = true;
            } else if (isIdle && location.hasSpeed() && location.getSpeed() >= MAX_NO_MOVEMENT_SPEED) {
//                insertLocation(track, lastLocation, lastValidTrackPoint);
//                insertLocation(track, location, null);
                isIdle = false;
            } else {
                Log.d(TAG, "Not recording location, idle");
            }
            lastLocation = location;
        } catch (Error e) {
            Log.e(TAG, "Error in onLocationChangedAsync", e);
            throw e;
        } catch (RuntimeException e) {
            Log.e(TAG, "RuntimeException in onLocationChangedAsync", e);
            throw e;
        }
    }
    private void registerLocationListener() {
        if (myTracksLocationManager == null) {
            Log.e(TAG, "locationManager is null.");
            return;
        }
        try {
            long interval = locationListenerPolicy.getDesiredPollingInterval();
            myTracksLocationManager.requestLocationUpdates(
                    interval, locationListenerPolicy.getMinDistance(), locationListener);
            currentRecordingInterval = interval;
        } catch (RuntimeException e) {
            Log.e(TAG, "Could not register location listener.", e);
        }
    }
    private void unregisterLocationListener() {
        if (myTracksLocationManager == null) {
            Log.e(TAG, "locationManager is null.");
            return;
        }
        myTracksLocationManager.removeLocationUpdates(locationListener);
    }
    private void releaseWakeLock() {
        if (wakeLock != null && wakeLock.isHeld()) {
            wakeLock.release();
            wakeLock = null;
        }
    }
    private void showNotification(boolean isGpsStarted) {
        if (isRecording()) {
            if (isPaused()) {
                stopForegroundService();
            } else {
//                Intent intent = IntentUtils.newIntent(this, TrackDetailActivity.class)
//                        .putExtra(TrackDetailActivity.EXTRA_TRACK_ID, recordingTrackId);
//                PendingIntent pendingIntent = TaskStackBuilder.create(this)
//                        .addParentStack(TrackDetailActivity.class).addNextIntent(intent)
//                        .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
//                startForegroundService(pendingIntent, R.string.track_record_notification);
            }
            return;
        } else {
            // Not recording
            if (isGpsStarted) {
//                Intent intent = IntentUtils.newIntent(this, TrackListActivity.class);
//                PendingIntent pendingIntent = TaskStackBuilder.create(this)
//                        .addNextIntent(intent).getPendingIntent(0, 0);
//                startForegroundService(pendingIntent, R.string.gps_starting);
            } else {
                stopForegroundService();
            }
        }
    }
    private static class ServiceBinder extends IGPSService.Stub {
        private DeathRecipient deathRecipient;
        private GPSService gpsService;

        public ServiceBinder(GPSService trackRecordingService) {
            this.gpsService = trackRecordingService;
        }

        @Override
        public boolean isBinderAlive() {
            return gpsService != null;
        }

        @Override
        public boolean pingBinder() {
            return isBinderAlive();
        }

        @Override
        public void linkToDeath(DeathRecipient recipient, int flags) {
            deathRecipient = recipient;
        }

        @Override
        public boolean unlinkToDeath(DeathRecipient recipient, int flags) {
            if (!isBinderAlive()) {
                return false;
            }
            deathRecipient = null;
            return true;
        }

        public void startGps() {
            if (!canAccess()) {
                return;
            }
            if (!gpsService.isRecording()) {
                gpsService.startGps();
            }
        }

        public void stopGps() {
            if (!canAccess()) {
                return;
            }
            if (!gpsService.isRecording()) {
                gpsService.stopGps(true);
            }
        }

        public boolean isRecording() {
            if (!canAccess()) {
                return false;
            }
            return gpsService.isRecording();
        }

        public boolean isPaused() {
            if (!canAccess()) {
                return false;
            }
            return gpsService.isPaused();
        }

        public long getRecordingTrackId() {
            if (!canAccess()) {
                return -1L;
            }
            return gpsService.recordingTrackId;
        }

        public void insertTrackPoint(Location location) {
            if (!canAccess()) {
                return;
            }
            gpsService.locationListener.onLocationChanged(location);
        }

        /**
         * Returns true if the RPC caller is from the same application or if the
         * "Allow access" setting indicates that another app can invoke this
         * service's RPCs.
         */
        private boolean canAccess() {
            // As a precondition for access, must check if the service is available.
            if (gpsService == null) {
                throw new IllegalStateException("The track recording service has been detached!");
            }
            if (android.os.Process.myPid() == Binder.getCallingPid()) {
                return true;
            } else {
//                return PreferencesUtils.getBoolean(gpsService, R.string.allow_access_key,
//                        PreferencesUtils.ALLOW_ACCESS_DEFAULT);
                return true;
            }
        }
        /**
         * Detaches from the track recording service. Clears the reference to the
         * outer class to minimize the leak.
         */
        private void detachFromService() {
            gpsService = null;
            attachInterface(null, null);

            if (deathRecipient != null) {
                deathRecipient.binderDied();
            }
        }

        @Override
        public Location getLastLocation() throws RemoteException {
            return gpsService.lastLocation;
        }
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
