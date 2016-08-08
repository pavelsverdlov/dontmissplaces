package svp.app.map.policy;

public class AbsoluteLocationListenerPolicy implements LocationListenerPolicy {

    private final long interval;
    public AbsoluteLocationListenerPolicy(long interval) {
        this.interval = interval;
    }

    @Override
    public long getDesiredPollingInterval() {
        return interval;
    }

    @Override
    public int getMinDistance() {
        return 0;
    }

    @Override
    public void updateIdleTime(long idleTime) {
        // Ignore
    }
}