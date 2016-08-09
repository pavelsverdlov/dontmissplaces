package svp.app.map.android.gps;


import android.location.Location;
import android.os.RemoteException;

public interface IGPSProvider {
    Location getLocation() throws RemoteException;

}
