package svp.com.dontmissplaces.model.gps;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.location.Location;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

public class GPSServiceProvider {
    private static final String TAG = "GPSServiceProvider";
    private ServiceConnection serviceConnection;

    public static Intent createServiceIntent(Context ctx){
        return new Intent(ctx,GPSService.class);
    }

    public final Object lock;
    public boolean isRunning;
    private IGPSService serviceRemote;

    public GPSServiceProvider(Context ctx) {
        lock = new Object();
        ctx.startService(createServiceIntent(ctx));
    }

    public Location getLocation() throws RemoteException {
        return serviceRemote.getLastLocation();
    }

    public void startup( Context context, final Runnable onServiceConnected ){
        synchronized (lock) {
            if(!isRunning){
                serviceConnection = new ServiceConnection() {
                    public void onServiceConnected( ComponentName className, IBinder service ) {
                        synchronized (lock) {
                            serviceRemote = IGPSService.Stub.asInterface( service );
                            isRunning = true;
                        }
                        if(onServiceConnected != null ) {
                            onServiceConnected.run();
                        }
                    }
                    public void onServiceDisconnected( ComponentName className ) {
                        synchronized (lock) { isRunning = false; }
                    }
                };
                context.bindService(createServiceIntent(context), this.serviceConnection, Context.BIND_AUTO_CREATE );
            }
        }
    }
    public void shutdown(Context context){
        synchronized (lock) {
            try {
                if(isRunning) {
                    context.unbindService(serviceConnection );
                    isRunning = false;
                }
            } catch (IllegalArgumentException e) {
                Log.w(TAG, "Failed to unbind a service, prehaps the service disapearded?", e );
            }
        }
    }
}
