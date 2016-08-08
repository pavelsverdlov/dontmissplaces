package svp.app.map.policy;

public interface LocationListenerPolicy {

    /**
     * Returns the polling interval this policy would like at this moment.
     *
     * @return the polling interval
     */
    long getDesiredPollingInterval();

    /**
     * Returns the minimum distance between updates.
     */
    int getMinDistance();

    /**
     * Notifies the amount of time the user has been idle at his current location.
     *
     * @param idleTime the time that the user has been idle at his current
     *          location
     */
    void updateIdleTime(long idleTime);

}
