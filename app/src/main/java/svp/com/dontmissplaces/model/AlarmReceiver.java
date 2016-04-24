package svp.com.dontmissplaces.model;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

import java.util.Vector;

public class AlarmReceiver extends WakefulBroadcastReceiver {
    public interface IAlarmObserver{
        void onReceive(Context context);
    }

    private static final String TAG = "AlarmReceiver";
    private final Vector<IAlarmObserver> observers;

    public AlarmReceiver() {
        this.observers = new Vector<>();
    }

    public void addObserver(IAlarmObserver observer){
        observers.add(observer);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        for (IAlarmObserver observer : observers){
            observer.onReceive(context);
        }
        //context.startService(new Intent(context, LocationService.class));
    }
}