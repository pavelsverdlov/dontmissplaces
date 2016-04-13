package svp.com.dontmissplaces.model.StepSensor;

// State of application, used to register for sensors when app is restored
public enum StepSensorTypes {
    OTHER(0),
    /**
     * This sensor also triggers an event upon each detected step,
     * but instead delivers the total accumulated number of steps
     * since this sensor was first registered by an app.
     * */
    COUNTER(1),
    /**
     * This sensor triggers an event each time the user takes a step.
     * Upon each user step, this sensor delivers an event with a value of 1.0
     * and a timestamp indicating when the step occurred.
     * */
    DETECTOR(2);


    private final int code;

    private StepSensorTypes(int code) {
        this.code = code;
    }
    public int toInt() {
        return code;
    }
}

