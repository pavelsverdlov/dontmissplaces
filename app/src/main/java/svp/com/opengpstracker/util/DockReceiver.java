package svp.com.opengpstracker.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import svp.com.opengpstracker.logger.GPSLoggerService;

public class DockReceiver extends BroadcastReceiver
{
    private final static String TAG = "OGT.DockReceiver";

    @Override
    public void onReceive( Context context, Intent intent )
    {
        String action = intent.getAction();
        if( action.equals( Intent.ACTION_DOCK_EVENT ) )
        {
            Bundle extras = intent.getExtras();
            boolean start = false;
            boolean stop = false;
            if( extras != null && extras.containsKey(Intent.EXTRA_DOCK_STATE ) )
            {
                int dockstate = extras.getInt(Intent.EXTRA_DOCK_STATE, -1);
                if( dockstate == Intent.EXTRA_DOCK_STATE_CAR )
                {
                    start = PreferenceManager.getDefaultSharedPreferences( context ).getBoolean( Constants.LOGATDOCK, false );
                }
                else if( dockstate == Intent.EXTRA_DOCK_STATE_UNDOCKED )
                {
                    stop = PreferenceManager.getDefaultSharedPreferences( context ).getBoolean( Constants.STOPATUNDOCK, false );
                }
            }
            if( start )
            {
                Intent serviceIntent = GPSLoggerService.createServiceIntent(context);
                serviceIntent.putExtra(GPSLoggerService.COMMAND, GPSLoggerService.EXTRA_COMMAND_START);
                context.startService( serviceIntent );
            }
            else if( stop )
            {
                Intent serviceIntent = GPSLoggerService.createServiceIntent(context);
                serviceIntent.putExtra(GPSLoggerService.COMMAND, GPSLoggerService.EXTRA_COMMAND_STOP);
                context.startService( serviceIntent );
            }
        }
        else
        {
            Log.w( TAG, "OpenGPSTracker's BootReceiver received " + action + ", but it's only able to respond to " + Intent.ACTION_BOOT_COMPLETED + ". This shouldn't happen !" );
        }
    }
}
