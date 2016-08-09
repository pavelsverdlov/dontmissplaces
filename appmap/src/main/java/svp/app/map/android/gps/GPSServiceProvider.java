package svp.app.map.android.gps;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.location.Location;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

public class GPSServiceProvider<TService extends GPSService> implements IGPSProvider{
    private static final String TAG = "GPSServiceProvider";
    private final Class<TService> serviceClass;
//    private TrackTimer timer;
    private ServiceConnection serviceConnection;
    private Vector<IGPSLocationListener> listeners;

    public Intent createServiceIntent(Context ctx){
        return new Intent(ctx,serviceClass);
    }

    public final Object lock;
    public boolean isRunning;
    private IGPSService serviceRemote;
    private final Context context;

    public GPSServiceProvider(Context context, Class<TService> serviceClass) {
        listeners = new Vector<>();
        this.serviceClass = serviceClass;
        lock = new Object();
        this.context = context;
        context.startService(createServiceIntent(context));
    }

    public Location getLocation() throws RemoteException {
        return serviceRemote.getLastLocation();
    }
    public void clearListeners(){
        listeners.clear();
    }
    public void addOnLocationChangeListener(IGPSLocationListener listener){
        if(serviceRemote == null) {
            listeners.add(listener);
        }else{
            try {
                serviceRemote.addLocationListener(listener);
            } catch (RemoteException e) {
                Log.e(TAG,"addOnLocationChangeListener",e);
            }
        }
    }

    public void startup(final Runnable onServiceConnected ){
        synchronized (lock) {
            if(!isRunning){
                serviceConnection = new ServiceConnection() {
                    public void onServiceConnected( ComponentName className, IBinder service ) {
                        synchronized (lock) {
                            serviceRemote = IGPSService.Stub.asInterface( service );
                            for (IGPSLocationListener listener : listeners) {
                                try {
                                    serviceRemote.addLocationListener(listener);
                                } catch (RemoteException e) {
                                    Log.e(TAG,"onServiceConnected",e);
                                }
                            }
                            listeners.clear();
                            isRunning = true;
                           // timer = startTimer(2000);
                        }
                        if(onServiceConnected != null ) {
                            onServiceConnected.run();
                        }
                    }
                    public void onServiceDisconnected( ComponentName className ) {
                        synchronized (lock) {
                            isRunning = false;
                            //context.stopService()
                        }
                    }
                };
                context.bindService(createServiceIntent(context), this.serviceConnection, Context.BIND_AUTO_CREATE );
            }
        }
    }
    public void shutdown(){
        synchronized (lock) {
            try {
                if(isRunning) {
                    context.unbindService(serviceConnection );
                    isRunning = false;
                }
            } catch (IllegalArgumentException e) {
                Log.w(TAG, "shutdown: Failed to unbind a service, prehaps the service disapearded?", e );
            }
        }
    }

//    private TrackTimer startTimer(long intervalMillisec){
//        if(timer != null){
//            return timer;
//        }
//        return new TrackTimer(intervalMillisec);
//    }
//    private class TrackTimer extends TimerTask {
//        private long elapsedMses;
//        private TrackTimer(long intervalMillisec) {
//            Timer timer = new Timer();
//            timer.scheduleAtFixedRate(this, intervalMillisec, intervalMillisec);
//        }
//
//        public void stop() {
//            this.cancel();
//        }
//
//        @Override
//        public void run() {
//            if(listeners.isEmpty()){
//                return;
//            }
//            try {
//                Location l = serviceRemote.getLastLocation();
//                Log.d(TAG, "OnLocationChanged: " +  l);
//                for (OnLocationChangeListener listener : listeners) {
//                    listener.OnLocationChanged(l);
//                }
//            } catch (RemoteException ex) {
//                Log.e(TAG, "OnLocationChanged: ", ex);
//            }
//        }
//    }
}
