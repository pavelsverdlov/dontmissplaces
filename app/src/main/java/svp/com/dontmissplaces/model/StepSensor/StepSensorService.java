package svp.com.dontmissplaces.model.StepSensor;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

public class StepSensorService {
    public static final String TAG = "StepSensorService";
    private final Activity activity;
    private final StringBuffer mDelayStringBuffer = new StringBuffer();
    // Steps counted in current session
    private int mSteps = 0;
    // When a listener is registered, the batch sensor delay in microseconds
    private int mMaxDelay = 0;
    // Value of the step counter sensor when the listener was registered.
    // (Total steps are calculated from this value.)
    private int mCounterSteps = 0;
    // Steps counted by the step counter previously. Used to keep counter consistent across rotation
    // changes
    private int mPreviousCounterSteps = 0;
    // Number of events to keep in queue and display on card
    private static final int EVENT_QUEUE_LENGTH = 10;
    // List of timestamps when sensor events occurred
    private float[] mEventDelays = new float[EVENT_QUEUE_LENGTH];
    // pointer to next entry in sensor event list
    private int mEventData = 0;
    // number of events in event list
    private int mEventLength = 0;

    // State of the app (STATE_OTHER, STATE_COUNTER or STATE_DETECTOR)
    private StepSensorTypes mState = StepSensorTypes.OTHER;

    public StepSensorService(Activity activity) {
        this.activity = activity;
    }

    private void unregisterListeners() {
        // BEGIN_INCLUDE(unregister)
        SensorManager sensorManager =
                (SensorManager) activity.getSystemService(Activity.SENSOR_SERVICE);
        sensorManager.unregisterListener(mListener);
        Log.i(TAG, "Sensor listener unregistered.");

        // END_INCLUDE(unregister)
    }
    private void resetCounter() {
        // BEGIN_INCLUDE(reset)
        mSteps = 0;
        mCounterSteps = 0;
        mEventLength = 0;
        mEventDelays = new float[EVENT_QUEUE_LENGTH];
        mPreviousCounterSteps = 0;
        // END_INCLUDE(reset)
    }
    public void onActivityCreated() {
        resetCounter();
        // Register listeners again if in detector or counter states with restored delay
        if (mState == StepSensorTypes.DETECTOR) {
            registerEventListener(mMaxDelay, Sensor.TYPE_STEP_DETECTOR);
        } else if (mState == StepSensorTypes.COUNTER) {
            // store the previous number of steps to keep  step counter count consistent
            mPreviousCounterSteps = mSteps;
            registerEventListener(mMaxDelay, Sensor.TYPE_STEP_COUNTER);
        }
//        if (savedInstanceState != null) {
//            mSteps = savedInstanceState.getInt(BUNDLE_STEPS);
//            mState = savedInstanceState.getInt(BUNDLE_STATE);
//            mMaxDelay = savedInstanceState.getInt(BUNDLE_LATENCY);
//        }
        // END_INCLUDE(restore)
    }
    /**
     * Register a {@link android.hardware.SensorEventListener} for the sensor and max batch delay.
     * The maximum batch delay specifies the maximum duration in microseconds for which subsequent
     * sensor events can be temporarily stored by the sensor before they are delivered to the
     * registered SensorEventListener. A larger delay allows the system to handle sensor events more
     * efficiently, allowing the system to switch to a lower power state while the sensor is
     * capturing events. Once the max delay is reached, all stored events are delivered to the
     * registered listener. Note that this value only specifies the maximum delay, the listener may
     * receive events quicker. A delay of 0 disables batch mode and registers the listener in
     * continuous mode.
     * The optimium batch delay depends on the application. For example, a delay of 5 seconds or
     * higher may be appropriate for an  application that does not update the UI in real time.
     *
     * @param maxdelay
     * @param sensorType
     */
    private void registerEventListener(int maxdelay, int sensorType) {
        // BEGIN_INCLUDE(register)

        // Keep track of state so that the correct sensor type and batch delay can be set up when
        // the app is restored (for example on screen rotation).
        mMaxDelay = maxdelay;
        if (sensorType == Sensor.TYPE_STEP_COUNTER) {
            mState = StepSensorTypes.COUNTER;
            /*
            Reset the initial step counter value, the first event received by the event listener is
            stored in mCounterSteps and used to calculate the total number of steps taken.
             */
            mCounterSteps = 0;
            Log.i(TAG, "Event listener for step counter sensor registered with a max delay of "
                    + mMaxDelay);
        } else {
            mState = StepSensorTypes.DETECTOR;
            Log.i(TAG, "Event listener for step detector sensor registered with a max delay of "
                    + mMaxDelay);
        }

        // Get the default sensor for the sensor type from the SenorManager
        SensorManager sensorManager = (SensorManager) activity.getSystemService(Activity.SENSOR_SERVICE);
        // sensorType is either Sensor.TYPE_STEP_COUNTER or Sensor.TYPE_STEP_DETECTOR
        Sensor sensor = sensorManager.getDefaultSensor(sensorType);

        // Register the listener for this sensor in batch mode.
        // If the max delay is 0, events will be delivered in continuous mode without batching.
        final boolean batchMode = sensorManager.registerListener(
                mListener, sensor, SensorManager.SENSOR_DELAY_NORMAL, maxdelay);

        if (!batchMode) {
            // Batch mode could not be enabled, show a warning message and switch to continuous mode
//            getCardStream().getCard(CARD_NOBATCHSUPPORT)
//                    .setDescription(getString(R.string.warning_nobatching));
//            getCardStream().showCard(CARD_NOBATCHSUPPORT);
            Log.w(TAG, "Could not register sensor listener in batch mode, " +
                    "falling back to continuous mode.");
        }

        if (maxdelay > 0 && batchMode) {
            // Batch mode was enabled successfully, show a description card
//            getCardStream().showCard(CARD_BATCHING_DESCRIPTION);
        }

        // Show the explanation card
//        getCardStream().showCard(CARD_EXPLANATION);

        // END_INCLUDE(register)

    }


    private boolean isKitkatWithStepSensor() {
        // Require at least Android KitKat
        int currentApiVersion = android.os.Build.VERSION.SDK_INT;
        // Check that the device supports the step counter and detector sensors
        PackageManager packageManager = activity.getPackageManager();
        return currentApiVersion >= android.os.Build.VERSION_CODES.KITKAT
                && packageManager.hasSystemFeature(PackageManager.FEATURE_SENSOR_STEP_COUNTER)
                && packageManager.hasSystemFeature(PackageManager.FEATURE_SENSOR_STEP_DETECTOR);
    }

    private final SensorEventListener mListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            // BEGIN_INCLUDE(sensorevent)
            // store the delay of this event
            recordDelay(event);
            final String delayString = getDelayString();

            if (event.sensor.getType() == Sensor.TYPE_STEP_DETECTOR) {
                // A step detector event is received for each step.
                // This means we need to count steps ourselves

                mSteps += event.values.length;

                // Update the card with the latest step count
//                getCardStream().getCard(CARD_COUNTING)
//                        .setTitle(getString(R.string.counting_title, mSteps))
//                        .setDescription(getString(R.string.counting_description,
//                                getString(R.string.sensor_detector), mMaxDelay, delayString));

                Log.i(TAG,"New step detected by STEP_DETECTOR sensor. Total step count: " + mSteps);

            } else if (event.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {

                /*
                A step counter event contains the total number of steps since the listener
                was first registered. We need to keep track of this initial value to calculate the
                number of steps taken, as the first value a listener receives is undefined.
                 */
                if (mCounterSteps < 1) {
                    // initial value
                    mCounterSteps = (int) event.values[0];
                }

                // Calculate steps taken based on first counter value received.
                mSteps = (int) event.values[0] - mCounterSteps;

                // Add the number of steps previously taken, otherwise the counter would start at 0.
                // This is needed to keep the counter consistent across rotation changes.
                mSteps = mSteps + mPreviousCounterSteps;

                // Update the card with the latest step count
//                getCardStream().getCard(CARD_COUNTING)
//                        .setTitle(getString(R.string.counting_title, mSteps))
//                        .setDescription(getString(R.string.counting_description,
//                                getString(R.string.sensor_counter), mMaxDelay, delayString));
                Log.i(TAG, "New step detected by STEP_COUNTER sensor. Total step count: " + mSteps);
                // END_INCLUDE(sensorevent)
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    private void recordDelay(SensorEvent event) {
        // Calculate the delay from when event was recorded until it was received here in ms
        // Event timestamp is recorded in us accuracy, but ms accuracy is sufficient here
        mEventDelays[mEventData] = System.currentTimeMillis() - (event.timestamp / 1000000L);

        // Increment length counter
        mEventLength = Math.min(EVENT_QUEUE_LENGTH, mEventLength + 1);
        // Move pointer to the next (oldest) location
        mEventData = (mEventData + 1) % EVENT_QUEUE_LENGTH;
    }

    /**
     * Returns a string describing the sensor delays recorded in
     * {@link #recordDelay(android.hardware.SensorEvent)}.
     *
     * @return
     */
    private String getDelayString() {
        // Empty the StringBuffer
        mDelayStringBuffer.setLength(0);

        // Loop over all recorded delays and append them to the buffer as a decimal
        for (int i = 0; i < mEventLength; i++) {
            if (i > 0) {
                mDelayStringBuffer.append(", ");
            }
            final int index = (mEventData + i) % EVENT_QUEUE_LENGTH;
            final float delay = mEventDelays[index] / 1000f; // convert delay from ms into s
            mDelayStringBuffer.append(String.format("%1.1f", delay));
        }

        return mDelayStringBuffer.toString();
    }

}
